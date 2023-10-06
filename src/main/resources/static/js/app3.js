const container = document.querySelector(".container");
const slideContainer = document.querySelector(".slide-container");
const upBtn = document.querySelector(".upBtn");
const downBtn = document.querySelector(".downBtn");
const autoModeBtn = document.querySelector(".autoModeBtn");
const closeBtn = document.querySelector(".close-btn");
const sideBar = document.querySelector(".sidebar");
const sidebarToggle = document.querySelector(".fa-bars");
let navLinks = document.querySelector(".links")
let sidebarBtn = document.querySelector(".sidebarBtn")
let bgmPrevBtn = document.querySelector(".prevArrow")
let bgmNextBtn = document.querySelector(".nextArrow")
let singer = document.querySelector(".singer")
let songTitle = document.querySelector(".song-title")
const playBtn = document.querySelector(".playBtn")
const audioEle = document.querySelector(".audioEle")
let bgmPagination = document.querySelector(".bgm-pagination")
let randomBtn = document.querySelector(".randomBtn")
let dim = document.querySelector(".dim")
const modalBackground = document.querySelector(".modal-background")
const reviewModalBackground = document.querySelector(".review-modal-background");
const modalCloseBtn = document.querySelector(".modal-closeBtn")
const addReviewModal = document.querySelector(".addReviewModal")
const modal = document.querySelector(".messageModal")
const addPlaylistForm = document.querySelector(".addPlaylist")
const addPlyInput = addPlaylistForm.querySelector(".addPly-input")
const emotionSelectBox = document.querySelector(".emotion-select-box")
const emotionSelectUl = document.querySelector(".emotion-select-ul")
const plySubmitBtn = document.querySelector(".plySubmitBtn")
let emoTags = document.querySelectorAll(".emoTag")
const playlistBtns = document.querySelectorAll(".playlistBtn")
let logStatus = document.querySelector(".login").getAttribute("data-logstatus")
let modalEmoTags
let userNo
//////////////// 두 번 이상 사용하는 경우만 위에 정의하기 /////////////////


let slide_index = 0
let slides
let interval
let timeout
let url
let urlPath

let bgmIndex = 0
let play = false
let clickCnt = 0

let autoMode = false

let musicList


let reviewNo
let plyEmoNo
let reviewList

let projectName

let page_val = 1;
let sort_val;
let id_val;

///////////////////////////////////
// EVENT 
///////////////////////////////////

window.addEventListener("DOMContentLoaded", function(){
	
	userNo = document.querySelector(".login").dataset.userno
	
	const urlObj = new URL(location.href)
	const urlParams = urlObj.searchParams
	
	projectName = urlObj.pathname.split("/")[1]
	urlPath = "/" + projectName + "/main"	
	const playlistNo = urlParams.get("playlistNo")
	
	// 서평뮤직 리스트
	if(playlistNo === null) {
		sort_val = "emotion"
		readySlides(sort_val, null, page_val)
	}
	else {
		sort_val = "playlist"
		id_val = playlistNo
		readySlides(sort_val, id_val, page_val)
	}
	
	// 감정 태그 이벤트 추가
	// 1. 사이드바
	emoTags.forEach(emoTag => {
		emoTag.onclick = playNewEmoList
	})

	// 2. 모달
	modalEmoTags = document.querySelectorAll(".tag")
	modalEmoTags.forEach(emoTag => {
		emoTag.onclick = function(){
			
			for(let tag of modalEmoTags) {
				tag.classList.remove("selected")
			}
			emoTag.classList.add("selected")
			
			addPlyInput.setAttribute("data-plyEmoNo", this.getAttribute("data-emono"))
		}		
	})
	
	// 로그인 상태 -> 플리 이벤트 추가
	if(logStatus === "login") {
		playlistBtns.forEach(plybtn => {
			plybtn.onclick = playNewPlaylist
		})	
	}	
})

//////////////////////////// KEY //////////////////////////////
/* keyup, keydown */
window.addEventListener("keydown", function(e) {
    if(e.key == 'ArrowUp') {
		if(slide_index == 0) {
			slide_index = 0
		} else {
			slide_index--;	
		}
        carousel();
    }
})

window.addEventListener("keydown", function(e) {
    if(e.key == 'ArrowDown') {
        slide_index++;
        carousel();
    }
})


/* 위아래 화살표 버튼 */
upBtn.addEventListener("click", function(){
	
	if(slide_index == 0) {
		slide_index = 0
	} else {
		slide_index--;	
	}

    carousel();
})

downBtn.addEventListener("click", function(){
    slide_index++;
    carousel();
})

/* 스페이스 바, 슬라이드 재생&중지 */
window.addEventListener("keydown", enterEventHandler)

function enterEventHandler(e) {
	if(e.code === 'Space') {
        changeMode()
    }
}

///////////////////////////// BUTTON /////////////////////////////
/* 랜덤 버튼 */
randomBtn.onclick = function() {
	
	slide_index = 0
	
	// 감정 태그 checked 다 제거
	emoTags.forEach(tag => {
		tag.classList.remove("checked")
	})

	// 사이드 바 닫힘
	closeSideBar()
	
	sort_val = "emotion"
	readySlides(sort_val, null, page_val)
	
	// 자동 전환 carousel, 음악 재생
	interval = null
	timeout = null
	
	changeMode()	
	
	/* 적용 안됨 */
	randomBtn.blur()
	window.focus()
	
	window.addEventListener("keydown", enterEventHandler)
}

// 전체 화면 버튼 > 슬라이드 전환 방식 변경
autoModeBtn.onclick = function(){
    changeMode()
}

///////////////////////////// SIDE-BAR /////////////////////////////
// 사이드 바 열기, 닫기
sidebarToggle.addEventListener("click", function() {
    sideBar.classList.add("show-sidebar");

    dim.classList.remove("unstaged")
    dim.classList.add("show-dim")

	window.removeEventListener("keydown", enterEventHandler)
})

closeBtn.addEventListener("click", function() {
    sideBar.classList.remove("show-sidebar");

    dim.classList.remove("show-dim")

    dim.addEventListener("transitionend", function(){
        this.classList.add("unstaged")
        this.removeEventListener("transitionend", arguments.callee)
    })

	window.addEventListener("keydown", enterEventHandler)
})

dim.onclick = function(){
    sideBar.classList.remove("show-sidebar");

    dim.classList.remove("show-dim")

    dim.addEventListener("transitionend", function(){
        this.classList.add("unstaged")
        this.removeEventListener("transitionend", arguments.callee)
    })

	window.addEventListener("keydown", enterEventHandler)
}

//////////////////////////// MODAL //////////////////////////////

// 모달 > 추가모달 나오게 하기 
addPlyInput.onclick = function() {
	emotionSelectBox.classList.add("show")
	addReviewModal.classList.add("move")
}

// 모달 > 새 플리 저장
plySubmitBtn.addEventListener("click", function(e){
	e.preventDefault()

	if(addPlyInput.value == "") {
		alert("제목을 입력해주세요")
	}
	else {			
		postNewPlaylist(addPlyInput.value, addPlyInput.dataset.plyemono).then(data => {
			// 모달 내 플리 업데이트

			if(data == -1) alert("다시 로그인 해주세요")
			
			/* 사이드바, 모달 플레이리스트 초기화 */
			document.querySelector(".playlist-box").querySelector("ul").innerHTML = ""
			document.querySelector(".playlist-list").innerHTML = ""
			
			/* 새 플레이리스트 불러오기 */
			reviewNo = slides[slide_index].getAttribute("data-reviewno")
			getPlaylist(reviewNo).then(data => {
				renderModalPly(data)
				renderSidebarPly(data)
			})			
		})
		
		addPlyInput.value = null
		
		/* 플리 추가 섹션 안보이도록 */
		emotionSelectBox.classList.remove("show")
		addReviewModal.classList.remove("move")
		
		/* 감정 태그 초기화 */
		for(let tag of modalEmoTags) {
			tag.classList.remove("selected")
		}		
	}
})

// 모달 닫기 버튼
modalCloseBtn.onclick = function(){
    modalBackground.classList.remove("show-modal")

	/* 모달 하단 집어넣기 */
	emotionSelectBox.classList.remove("show")
	addReviewModal.classList.remove("move")
	
	/* 감정 태그 초기화 */
	for(let tag of modalEmoTags) {
		tag.classList.remove("selected")
	}
	
	/* 삭제한 enter key event 리스너 다시 더하기 */
	window.addEventListener("keydown", enterEventHandler)
	
	/* input 값 초기화 */
	addPlyInput.value = null;
	
	/* 다시 자동 재생 */
	if(autoMode == true) {
		changeMode()
	}
}

///////////////////////////// BGM /////////////////////////////
// 노래 끝났을 때 자동으로 다음 노래로 넘어감
audioEle.addEventListener("ended", function(){
    console.log("music ended")

    moveNextBgm()
})

// play 버튼 토글, 노래 재생&중지
playBtn.onclick = function(){

    if(play === false) { // 음악 재생
		
        audioEle.play()

        playBtn.innerHTML = '<i class="fa-solid fa-pause"></i>'

        play = !play;
    } else {
        //bgmCurrentTime = audioEle.currentTime

        audioEle.pause()

        playBtn.innerHTML = '<i class="fa-solid fa-play"></i>'

        play = !play;
    }
    
}

/* bgm carousel 기능 */
bgmPrevBtn.addEventListener("click", function(){
    
    if(play === true) {
        clickCnt++

        if(clickCnt === 1 ) { // 처음부터 재생
            audioEle.currentTime = 0
    
        } else if(clickCnt === 2) { // 전으로 넘어가기
            clickCnt = 0
    
            if(bgmIndex > 0) {
                bgmIndex--
        
                updateBgm()
            } else {
                bgmIndex = musicList.length - 1
        
                updateBgm()
            }
        }
    } else {
        if(bgmIndex > 0) {
            bgmIndex--
    
            updateBgm()
        } else {
            bgmIndex = musicList.length - 1
    
            updateBgm()
        }
    }
})

bgmNextBtn.addEventListener("click", moveNextBgm)

function moveNextBgm(){
	
    if(bgmIndex < musicList.length - 1) {
        bgmIndex++

        updateBgm()        
    } else {
        bgmIndex = 0

        updateBgm() 
    }
}

//////////////////////////////////////////////////////////
// EVENT HANDLER
//////////////////////////////////////////////////////////
function playNewEmoList () {
	// 슬라이드 인덱스 초기화
	slide_index = 0

	// 감정태그 초기화
	for(let tag of emoTags) {
		tag.classList.remove("checked")
	}
	this.classList.add("checked")
	
	// 사이드 바 닫힘
	closeSideBar()

	// 감정태그에 해당하는 서평, 음악 리스트 불러오기 + 자동 전환 & 음악 재생
	sort_val = "emotion"
	id_val = this.id
	page_val = 1
	readySlides(sort_val, id_val, page_val)
	
	afterLoadAutoMode()
	
	window.addEventListener("keydown", enterEventHandler)
	
	this.blur()
	slideContainer.click()	
}

function playNewPlaylist() {
	// 클릭 시 색 스타일 
	for(let plyBtn of playlistBtns) {
		plyBtn.classList.remove("selected")
	}
	this.classList.add("selected")

	// 현재 서평 + 음악 초기화
	slide_index = 0
	
	//  사이드 바 닫힘
	closeSideBar()		

	// 클릭한 플레이리스트 서평 리스트 + 음악 목록 db에서 가져오기
	let playlistNo = this.getAttribute("data-playlistno")
	
	sort_val = "playlist"
	id_val = playlistNo
	page_val = 1
	readySlides(sort_val, id_val, page_val)
	//afterLoadAutoMode()
	
	// 자동 전환 carousel, 음악 재생
	interval = null
	timeout = null
	
	changeMode()
	
	window.addEventListener("keydown", enterEventHandler)	
}

//서평을 플리에 추가/취소(모달)
function addReviewBtnHandler() {
	
	if(autoMode == true) { 
		changeMode()
	}

	reviewNo = slides[slide_index].getAttribute("data-reviewno")

	fetch(urlPath + "/playlists?review_id=" + reviewNo, {
		method: "GET", 
		headers: {
			"Content-Type" : "application/json"}
	}).then(response => response.json())
	  .then(data => {

		// 초기화
		document.querySelector(".playlist-box").querySelector("ul").innerHTML = ""
		
		// 렌더
		renderModalPly(data)
		//renderPlaylist(data, "modal")
		
		// 모달 보임
        reviewModalBackground.classList.toggle("show-modal")

		// 모달이 보이면 window가 Enter event 못 감지하도록 (재생 못하도록)
		window.removeEventListener("keydown", enterEventHandler)
	})	
}

// 서평 좋아요 버튼
function likeReviewBtnHandler() {
	console.log("좋아요 클릭")

	reviewNo = slides[slide_index].getAttribute("data-reviewNo")

	postLikeReview(reviewNo).then(data => {
		console.log("좋아요 결과: " + data.result)
		
        if(data.result === "좋아요") {
            this.innerHTML = '좋아요<i class="fa-solid fa-heart"></i>'
        } else {
            this.innerHTML = '좋아요<i class="fa-regular fa-heart"></i>'
        }		
	})
}


//////////////////////////////////////////////////////////
// UTIL
//////////////////////////////////////////////////////////
function closeSideBar() {
    sideBar.classList.remove("show-sidebar");
    dim.classList.remove("show-dim")

    dim.addEventListener("transitionend", function(){
        this.classList.toggle("unstaged")
        this.removeEventListener("transitionend", arguments.callee)
    })	
}

/* (슬라이드 렌더 후) 자동 전환 및 음악 재생 */
function afterLoadAutoMode(){
	// 자동 전환 carousel, 음악 재생
	interval = null
	timeout = null
	
	changeMode()
}

function showMsg(){
	
    clearTimeout(timeout)

    modal.classList.add("opaque")
    modal.classList.remove("unstaged")

    timeout = setTimeout(function(){
        modal.classList.remove("opaque")

        modal.addEventListener("transitionend", function(){
            this.classList.add("unstaged")

    		modal.innerHTML = "<p>슬라이드 전환 방식을 변경하고 싶으면 '스페이스바'를 눌러주세요</p>"

            this.removeEventListener("transitionend", arguments.callee)
        })
    }, 2000)
}

function audioPlay(){
	console.log("canplaythrough event trigger...")
	audioEle.play()
}

function changeMode(){

    showMsg()

    if(autoMode === false) { // 수동 -> 자동 전환 모드
        console.log("자동 전환")
		
		if(musicupdated === true) { // 새로운 음악 파일이 로드됐다면 기다렸다가 재생
			console.log("music updated:" + musicupdated)
		
			audioEle.addEventListener("canplaythrough", audioPlay)

		}
		else {
			audioEle.play()
		}

		playBtn.innerHTML = '<i class="fa-solid fa-pause"></i>'
		play = true;

        autoCarousel()

        autoMode = true;
    } else {                // 자동 -> 수동 전환 모드
        console.log("수동 전환")

		if(musicupdated === true) {
			audioEle.removeEventListener("canplaythrough", audioPlay)
			
			musicupdated = false
			
			console.log("changeMode > removeEventListener > musicupdated: " + musicupdated)
		}
		

		audioEle.pause()
		playBtn.innerHTML = '<i class="fa-solid fa-play"></i>'
		play = false;

        clearInterval(interval)

        navLinks.style.visibility = 'visible'
        sidebarBtn.style.visibility = 'visible'
        autoModeBtn.style.display = 'block'

        autoMode = false

        carousel()
    }
}

function autoCarousel(){
    
    upBtn.style.display = "none";
    downBtn.style.display = "none";

	if(window.innerWidth < 495 || window.innerHeight < 420) {
		autoModeBtn.style.display = 'block';
	} else {
		autoModeBtn.style.display = 'none';	
	}

    navLinks.style.visibility = 'hidden'
    sidebarBtn.style.visibility = 'hidden'

    interval = setInterval(
        function(){
            slide_index++
            carousel()
        },
        8000
    )
}


function carousel() {
	
	// 마지막 페이지일 때 -> 플리의 다음 페이지 목록을 가져온다.
    if(slide_index == slides.length) { 
        //renderNewList(reviewList); // 마지막 슬라이드 일떄 ?????? (일단 돌리는 걸로)

		page_val++;
		
		// 기존(전 페이지) 슬라이드 삭제 또는 냅두기 (일단 삭제하는 걸로)
		slide_index = 0;
		
		readySlides(sort_val, id_val, page_val)
    }

	// 수동모드 경우 -> index가 -1로 가지 못하도록
    if(autoMode === false) { 
        if(slide_index > 0) {
            upBtn.style.display = "block";
        } else {
            upBtn.style.display = "none";
        }
        downBtn.style.display = "block"        
    }

	// 슬라이드 이동
    slides.forEach(slide => {
        slide.style.transform = `translateY(-${slide_index * 100}%)`;
    })

	console.log("slide_index: " + slide_index)
}

let musicupdated = false

function updateBgm() {

	//canplayToggle = !canplayToggle

    // 바로 재생되게 하기
    if(play === true) { // 재생되고 있는 음악 멈춤
        audioEle.pause() 
    }

    // audio 객체 업데이트 (음악 시작, 아이콘 업데이트)
    singer.textContent = musicList[bgmIndex].artist
    songTitle.textContent = musicList[bgmIndex].musicTitle
    audioEle.src = "/"+ projectName +"/asset/music/" + musicList[bgmIndex].musicPath + ".mp3"

	musicupdated = true

    // play 상태에서 next btn 누르면 play
    // pause 상태에서 next btn 누르면 pause 상태로 넘어감
    if(play === true) {
        audioEle.play()
        playBtn.innerHTML = '<i class="fa-solid fa-pause"></i>'
    }

    // 해당 페이지 위치를 페이징에 반영하기
    let dots = document.querySelectorAll(".dot");
	
	dots.forEach(dot => {
		dot.classList.remove("active")
	})
    dots[bgmIndex].classList.add("active")
}

async function readySlides(sort, id, page){
	
	const datalist = await getReviewMusicList(sort, id, page) 
	
	reviewList = datalist.reviewList
	musicList = datalist.musicList
	
	// 먼저 현재 있던 슬라이드 초기화
	slideContainer.innerHTML = ""

	if(reviewList.length > 0) {
		
		// 위 화살표 아이콘 안보이게
		upBtn.style.display = "none";
		
		// 슬라이드 출력
		renderNewList(reviewList)
		//renderNewArray(reviewList)
		
		// bgm paging
		renderBgmPaging(musicList.length)	
		
		// bgm 첫 번째 출력
		bgmIndex = 0
		updateBgm()	
			
	} else {
		console.log("저장된 서평이 없습니다.")
		
		modal.innerHTML = "<p>저장된 서평이 없습니다</p>"
		showMsg()
		
		changeMode()
	}		
}


//////////////////////////////////////////////////////////
// FETCH
//////////////////////////////////////////////////////////

// 플리 목록 가져오기
async function getReviewMusicList(sort, id, page) {
	try {
		url = urlPath + "/playlist?sort=" + sort + "&page=" + page
		
		if(id !== null) {
			url += "&id=" + id
		}
		
		const response = await fetch(url)
		
		return response.json()		
	} catch(error) {
		console.log("Failed to fetch review, music list")
	}
}

// 나의 플리 목록 가져오기
async function getPlaylist(reviewNo) {
	
	const response = await fetch(urlPath + "/playlists?review_id=" + reviewNo, {
		method: "GET",
		headers: {
			"Content-Type" : "application/json"
		}
	})
	
	return response.json()
}

// 새로운 플리 추가하기
async function postNewPlaylist(playlistName, emoNo){
	
	let obj = {
		playlistName : playlistName,
		emoNo: emoNo
	}
	
	const response = await fetch(urlPath + "/playlists", {
		method: "POST",
		headers: {
			"Content-Type" : "application/json"
		},
		body : JSON.stringify(obj)
	})
	
	return response.json()
}

// 플리에 서평 추가
async function toggleReviewToPly(obj) {
	console.log("add review to ply")
	
	url = urlPath + "/playlist" //toggleReviewToPly
	
	const response = await fetch(url, {
		method: "POST", 
		headers: {"Content-Type" : "application/json"},
		body : JSON.stringify(obj)
	})
	
	return response.json()	
}

// 서평 좋아요
async function postLikeReview(reviewNo) {

	try{
		const postResponse = await 	fetch(urlPath + "/reviews/" + reviewNo + "/like", {
				method: "POST",
				headers: {"Content-Type": "application/json"},
			})
			
		const post = await postResponse.json()	
		
		if(post == null) alert("다시 로그인 해주세요")
		
		return post	
	} catch(error) {
		console.log("Failed to fetch like_review", error)
		
		return -1
	}
}


//////////////////////////////////////////////////////////
// RENDER
//////////////////////////////////////////////////////////

function renderBgmPaging(count) {

	// 예전 paging 삭제
	bgmPagination.innerHTML = ""

    let ul = document.createElement("ul")

    for(let index = 0; index < count; index++){
        let div = document.createElement("div")
        let li = document.createElement("li")

        div.classList.add("dot")
        if(index == bgmIndex) {
            div.classList.add("active")
        }

        li.append(div)
        ul.append(li)
    }

    bgmPagination.append(ul)
}

function renderModalPly(list) {

	list.forEach(item => {

		let li = document.createElement("li")
		let p = document.createElement("p")
		let btn = document.createElement("button")
		
		p.innerText = item.playlistName
	
		if(item.userNo == userNo) { // 내가 만든 플리인 경우 -> 이미 추가했는지 확인

			if(item.cnt > 0) { // 추가했음
				btn.innerHTML = '<i class="fa-solid fa-check"></i>'
				li.classList.add("selected")				
			} 
			else { // 지금 서평 플레이리스트에 추가 안했음
				btn.innerHTML = '<i class="fa-solid fa-plus"></i>'
			}	
		}
		
		li.setAttribute("data-playlistNo", item.playlistNo)
		
		li.classList.add("playlist-li")
		
		//btn.classList.add("reviewAddBtn")
		li.classList.add("reviewAddBtn")
		
		p.style.pointerEvents = 'none';
		btn.style.pointerEvents = 'none';
		
		li.append(p, btn)
	
		document.querySelector(".playlist-box").querySelector("ul").append(li)		
	})

	// 이벤트 리스너 넣기 (서평을 플리에 추가)
	const reviewAddBtns = addReviewModal.querySelectorAll(".reviewAddBtn")
	reviewAddBtns.forEach(addBtn => {
	    addBtn.onclick = function(){

			let playlistNo = this.dataset.playlistno
			let icon = this.children[1].children[0].classList[1]
			reviewNo = slides[slide_index].getAttribute("data-reviewNo")
			
			let obj = {
				playlistNo: playlistNo,
				reviewNo : reviewNo,
				icon: icon
			}
			////// DB 변경 ///////
			toggleReviewToPly(obj).then(data => {
				console.log(data + "건 | 서평을 플리에 저장 또는 삭제")
			})
			
			////// 화면 ICON 변경 ///////	
	        if(icon == 'fa-plus') {
	            this.children[1].innerHTML = '<i class="fa-solid fa-check"></i>'
				this.classList.add("selected")
	        } else { 
	            this.children[1].innerHTML = '<i class="fa-solid fa-plus"></i>'
				this.classList.remove("selected")
	        }
	    }		
	})
}


function renderSidebarPly(list) {	
	
	const ul_ply = document.querySelector(".playlist-list")
	
	list.forEach(plyVo => {
		let li = document.createElement("li")
		let p = document.createElement("p")
		
		li.setAttribute("data-playlistno", plyVo.playlistNo)
		li.classList.add("playlistBtn")
		
		p.innerText = plyVo.playlistName
		
		li.append(p)
		
		ul_ply.append(li)
	})
	
	// 이벤트 추가(클릭 -> 플리 재생)
	const plylist = document.getElementsByClassName("playlistBtn")
	
	for(let item of plylist) {
		item.onclick = playNewPlaylist
	}
}

function renderNewList(reviewlist) {
	
	// 먼저 현재 있던 슬라이드 초기화
	slideContainer.innerHTML = ""
	
	// 위 화살표 아이콘 안보이게
	upBtn.style.display = "none";
	
	reviewlist.forEach(item => {
		renderSlide(item)
	})
	
	slides = document.querySelectorAll(".slide")
	
    slides.forEach((slide, index) => { 
        slide.style.top = `${index * 100}%`;
    });
}

let heartBtn

function renderSlide(item) {
	
	let styles = item.styleName.split(",")
	let color = styles[0]
	let fontFamily = styles[1]
			
    const slide = document.createElement("div");
    const review = document.createElement("p");
    const username = document.createElement("p");
    const btnContainer = document.createElement("div");
    heartBtn = document.createElement("button");
    const addBtn = document.createElement("button"); 
	const a = document.createElement("a")
	
	// 배경화면 비디오 vs 이미지
	let contentBox
	let video
	if(item.videourl != null) {
		video = document.createElement("video")
		video.classList.add("video")
		video.src = "/" + projectName + "/asset/img/review_card/" + item.videourl
		video.setAttribute("muted", "muted")
		video.setAttribute("autoplay", "autoplay")		
		video.setAttribute("loop", "loop")
		
		contentBox = document.createElement("div")
		contentBox.classList.add("contentBox")
	} else {
		slide.style.backgroundColor = color
	}
	
	a.classList.add("reviewLink")
    slide.classList.add("slide");
	review.classList.add("review");
    username.classList.add("username");
	btnContainer.classList.add("btn-container");
    btnContainer.classList.add("slideBtnContainer");
    heartBtn.classList.add("heartBtn");
    addBtn.classList.add("addBtn");
	
	slide.setAttribute("data-reviewNo", item.reviewNo)
    review.textContent = item.reviewContent;
	review.style.fontFamily = fontFamily;
    username.textContent = item.nickname;
	heartBtn.innerHTML = '좋아요<i class="fa-regular fa-heart"></i>';
    addBtn.innerHTML = '플레이리스트<i class="fa-solid fa-plus"></i>';

	 
	// 로드아웃 & 로그인
	if(logStatus === "logout") { // 로그아웃
		heartBtn.onclick = function() {
			location.href = "/" + projectName + '/user/loginForm'
		}
		addBtn.onclick = function() {
			location.href = "/" + projectName + '/user/loginForm'
		}
		
		review.setAttribute("title", "로그인 후 이용 가능합니다.")
		heartBtn.setAttribute("title", "로그인 후 이용 가능합니다.")
		addBtn.setAttribute("title", "로그인 후 이용 가능합니다.")
	} 	
	else { // 로그인
		a.setAttribute("href", "/" + projectName + "/bookdetail?bookNo=" + item.bookNo + "&userNo=" + userNo)		
	
		
		username.onclick = function(){
			location.href = "/" + projectName + "/" + item.nickname
		}
		
		if(item.alreadyLikedCnt == 0) {
			heartBtn.innerHTML = '좋아요<i class="fa-regular fa-heart"></i>';
		} else {
			heartBtn.innerHTML = '좋아요<i class="fa-solid fa-heart"></i>';
		}
		
	    heartBtn.onclick = likeReviewBtnHandler
	    addBtn.onclick = addReviewBtnHandler // 서평 플리에 추가 모달 버튼 (reviewNo, playlistNo, userNo)
	}
	
    btnContainer.append(heartBtn, addBtn);
	a.append(review);
	if(item.videourl != null) {
		contentBox.append(a, username, btnContainer);
		slide.append(video, contentBox);
	} else {
		slide.append(a, username, btnContainer);
	}	
    slideContainer.append(slide);
    container.append(slideContainer);
}
