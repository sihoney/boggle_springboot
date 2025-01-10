const slideContainer = document.querySelector(".slide-container");
const upBtn = document.querySelector(".upBtn");
const slide = document.querySelector(".slide");
const downBtn = document.querySelector(".downBtn");
const autoModeBtn = document.querySelector(".autoModeBtn");
let navLinks = document.querySelector(".links");
let sidebarBtn = document.querySelector(".sidebarBtn");
const modal = document.querySelector(".messageModal");
const reviewModalBackground = document.querySelector(".review-modal-background");
//let heartBtn

let SLIDE_INDEX = 0;
let IS_AUTO_MODE = false;
let REVIEW_LIST = [];
let TOTAL_REVIEW_COUNT = 0;
let PLAYLIST_ID;
let INTERVAL;
let TIME_OUT;
let END_PAGE;

//서평을 플리에 추가/취소 (모달)
async function clickAddBtn() {
	
	if(IS_AUTO_MODE == true) { 
		changeMode()
	}

    reviewModalBackground.classList.toggle("show-modal")// 모달 보임

	// 모달이 보이면 window가 Enter event 못 감지하도록 (재생 못하도록)
	window.removeEventListener("keydown", spaceDownWindow)

	REVIEW_ID = this.dataset.review_id;
	let playlists = await getMyPlaylists(REVIEW_ID);
	
	// 초기화
	//document.querySelector(".playlist-box").querySelector("ul").innerHTML = ""
	
	// 렌더
	renderModalPly(playlists);
}

// 서평 좋아요
async function postLikeReview(reviewNo) {

	try{
		const response = await 	fetch(`/reviewUser`, {
				method: "POST",
				headers: {"Content-Type": "application/json"},
				body: JSON.stringify({
					reviewId: reviewNo
				})
			})

		return response.status;
	} catch(error) {
		console.log("Failed to fetch like_review", error)
		
		return -1
	}
}

// 서평 좋아요 버튼
function clickHeartBtn() {
	console.log("좋아요 클릭")

	postLikeReview(this.dataset.review_id).then(data => {
		
		let heartIcon = this.querySelector(".fa-heart")

		if(status == 200) {
            //this.innerHTML = '좋아요<i class="fa-solid fa-heart"></i>'
			heartIcon.classList.toggle("fa-regular")
			heartIcon.classList.toggle("fa-solid")
        } else {
            //this.innerHTML = '좋아요<i class="fa-regular fa-heart"></i>'
			heartIcon.classList.toggle("fa-regular")
			heartIcon.classList.toggle("fa-solid")
        }		
	})
}

//renderSlide
function renderSlide(list) {
	
	list.forEach(item => {
	
	    const card = document.createElement("div");
	    const link = document.createElement("a") // a
		const content = document.createElement("p"); //review
	    const writer = document.createElement("p"); //username
	    const btnContainer = document.createElement("div");
	    const heartBtn = document.createElement("button");
	    const addBtn = document.createElement("button"); 
		const video = document.createElement("video")
		
	    card.classList.add("card"); // slide
		link.classList.add("link")
		content.classList.add("content");
	    writer.classList.add("writer");
		btnContainer.classList.add("btn-container");
	    btnContainer.classList.add("slideBtnContainer");
	    heartBtn.classList.add("heartBtn");
	    addBtn.classList.add("addBtn");
		video.classList.add("video")
		
		card.setAttribute("data-reviewNo", item.reviewId)
	    content.textContent = item.content;
		content.style.fontFamily = item.fontEntity.fontName;
	    writer.textContent = item.nickname;
		heartBtn.innerHTML = '좋아요<i class="fa-regular fa-heart"></i>';
		heartBtn.dataset.review_id = item.reviewId;
	    addBtn.innerHTML = '플레이리스트<i class="fa-solid fa-plus"></i>';
		addBtn.dataset.review_id = item.reviewId;
		video.setAttribute("muted", "muted")
		video.setAttribute("autoplay", "autoplay")		
		video.setAttribute("loop", "loop")
		let wallpaperName = item.wallpaperEntity.wallpaperName
		video.src = `/resources/static/images/review_card/${wallpaperName.slice(0, -4)}.mp4`
	
		// 로그인 & 로그아웃
		if(logStatus === "logout") { // 로그아웃
			heartBtn.onclick = function() {
				location.href = '/user/loginForm'
			}
			addBtn.onclick = function() {
				location.href = '/user/loginForm'
			}
			
			content.setAttribute("title", "로그인 후 이용 가능합니다.")
			heartBtn.setAttribute("title", "로그인 후 이용 가능합니다.")
			addBtn.setAttribute("title", "로그인 후 이용 가능합니다.")
		} else { // 로그인
			link.setAttribute("href", `/book/${item.bookEntity.isbn13}`);
				
			writer.onclick = function(){
				location.href = `/${item.nickname}/mybook`;
			}
			
			if(item.likeByAuthUser == false) {
				heartBtn.innerHTML = '좋아요<i class="fa-regular fa-heart"></i>';
			} else {
				heartBtn.innerHTML = '좋아요<i class="fa-solid fa-heart"></i>';
			}
			
		    heartBtn.onclick = clickHeartBtn
		    addBtn.onclick = clickAddBtn // 서평 플리에 추가 모달 버튼 (reviewNo, playlistNo, userNo)
		}
		
		link.append(content, writer);
	    btnContainer.append(heartBtn, addBtn);
		card.append(video, link, btnContainer);

	    slide.append(card);  
	})
}

function renderNewList(reviewList) {
	renderSlide(reviewList)
}

function showMsg(message){
	
	modal.innerHTML = message;
	
    clearTimeout(TIME_OUT)

    modal.classList.add("opaque")
    modal.classList.remove("unstaged")

    TIME_OUT = setTimeout(function(){
        modal.classList.remove("opaque")

        modal.addEventListener("transitionend", function(){
            this.classList.add("unstaged")

    		modal.innerHTML = "<p>슬라이드 전환 방식을 변경하고 싶으면 '스페이스바'를 눌러주세요</p>"

            this.removeEventListener("transitionend", arguments.callee)
        })
    }, 2000)
}

async function fetchSlideAndMusic(requestParam, id, page) {
	try {
		let url = `/api/review?page=${page}`;
		
		if(requestParam != null & id != null) {
			url += `&${requestParam}=${id}`
		}
		
		const response = await fetch(url, {
		    method: 'GET',
    		headers: {
        		'Accept': 'application/json',
    		}
		});
		
		return response.json()		
	} catch(error) {
		console.log("Failed to fetch review, music list")
	}
}

async function fetchAndRenderSlide(requestParam, id, page){
	
	const data = await fetchSlideAndMusic(requestParam, id, page) 
	
	console.log(data)
	
	REVIEW_LIST = data.reviewList;	
	MUSIC_LIST = data.musicList;
	TOTAL_REVIEW_COUNT += REVIEW_LIST.length;
	END_PAGE = data.pagination.endPage;

	if(REVIEW_LIST.length > 0) {
		
		renderNewList(REVIEW_LIST)// 슬라이드 출력
		renderBgmPaging(MUSIC_LIST.length)	// bgm paging	
		
		// bgm 첫 번째 출력
		BGM_INDEX = 0
		updateBgm()	
  		//playAudio();			
			
	} else {
		console.log("저장된 서평이 없습니다.")
		
		showMsg("<p>저장된 서평이 없습니다</p>")
		
		changeMode()
	}		
}

function setDisplayOfBtn(){

	if(IS_AUTO_MODE) { // 자동 모드
	    upBtn.style.display = "none";
	    downBtn.style.display = "none";
	
		if(window.innerWidth < 495 || window.innerHeight < 420) {
			autoModeBtn.style.display = 'block';
		} else {
			autoModeBtn.style.display = 'none';	
		}
	
	    navLinks.style.visibility = 'hidden'
	    sidebarBtn.style.visibility = 'hidden'	
	
	} else { // 수동 모드
        if(SLIDE_INDEX == 0) {
            upBtn.style.display = "none";
        } else {
			upBtn.style.display = "block";
		}		
  
		downBtn.style.display = "block";
		autoModeBtn.style.display = 'block'		
   		navLinks.style.visibility = 'visible'
        sidebarBtn.style.visibility = 'visible'
	}
}

/*function caroudel_(){
	console.log("SLIDE_INDEX: " + SLIDE_INDEX)

	setDisplayOfBtn()

	// 마지막 페이지일 때 -> 플리의 다음 페이지 목록을 가져온다.
    if(SLIDE_INDEX == TOTAL_REVIEW_COUNT - 1) { 
		PAGE++;

        console.log("마지막 SLIDE 입니다. 로드를 시작합니다")
		
		fetchAndRenderSlide(REQUEST_PARAM, ID, PAGE)
    }	
	
	// 슬라이드 이동
	slide.style.transform = `translateY(-${SLIDE_INDEX * 100}vh)`;	
}
*/
function carousel() {
	console.log("SLIDE_INDEX: " + SLIDE_INDEX)

	PAGE = Math.ceil(SLIDE_INDEX / 5);

	// 마지막 페이지일 때 -> 플리의 다음 페이지 목록을 가져온다.
    if(SLIDE_INDEX == TOTAL_REVIEW_COUNT) { 
		//PAGE++;
		
		console.log(PAGE)
		console.log(END_PAGE)
		
		if(PAGE == END_PAGE){
			console.log("마지막 PAGE 입니다. 처음 PAGE로 돌아갑니다.")
			
			PAGE = 0;
			SLIDE_INDEX = 0;
		} else {	
	        console.log("마지막 SLIDE 입니다. 로드를 시작합니다")
			
			fetchAndRenderSlide(REQUEST_PARAM, ID, PAGE)
		}	
    }

	slide.style.transform = `translateY(-${SLIDE_INDEX * 100}vh)`;	// 슬라이드 이동
	
	setDisplayOfBtn()
}

function autoCarousel(){

    INTERVAL = setInterval(
        function(){
            SLIDE_INDEX++
            carousel()
        },
        8000
    )
}

function changeMode(){

    if(IS_AUTO_MODE) { // 자동 -> 수동
     
        console.log("수동 전환")

		showMsg("수동 전환 모드로 변경합니다.")

		IS_AUTO_MODE = false

        clearInterval(INTERVAL)
		pauseAudio();
  
    } else { // 수동 -> 자동
        console.log("자동 전환")

		showMsg("자동 전환 모드로 변경합니다.")
	
		IS_AUTO_MODE = true;

		clearInterval(INTERVAL)
        autoCarousel();
		playAudio();

    }
	
	setDisplayOfBtn();
}

// 전체 화면 버튼 > 슬라이드 전환 방식 변경
autoModeBtn.onclick = function(){
    changeMode()
}

/* keyup, keydown */
window.addEventListener("keydown", function(e) {
    if(e.key == 'ArrowUp') {
		if(SLIDE_INDEX == 0) {
			SLIDE_INDEX = 0
		} else {
			SLIDE_INDEX--;	
		}
		
        carousel();
    }
})

window.addEventListener("keydown", function(e) {
    if(e.key == 'ArrowDown') {
        SLIDE_INDEX++;

        carousel();
    }
})


/* 위아래 화살표 버튼 */
upBtn.addEventListener("click", function(){
	
	if(SLIDE_INDEX == 0) {
		SLIDE_INDEX = 0
	} else {
		SLIDE_INDEX--;	
	}

    carousel();
})

downBtn.addEventListener("click", function(){
    SLIDE_INDEX++;

    carousel();
})



