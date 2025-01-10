let bgmPrevBtn = document.querySelector(".prevArrow")
let bgmNextBtn = document.querySelector(".nextArrow")
let singer = document.querySelector(".singer")
let songTitle = document.querySelector(".song-title")
const playBtn = document.querySelector(".playBtn")
const audioEle = document.querySelector(".audioEle")
let bgmPagination = document.querySelector(".bgm-pagination")
let randomBtn = document.querySelector(".randomBtn")
let dim = document.querySelector(".dim")
const closeBtn = document.querySelector(".close-btn");
const sideBar = document.querySelector(".sidebar");
const sidebarToggle = document.querySelector(".fa-bars");

let musicupdated = false
let BGM_INDEX = 0
let play = false
let CLICK_CNT = 0
let MUSIC_LIST
let PLAY_ICON
///////////////////////////// BGM /////////////////////////////
function audioPlay(){
	console.log("canplaythrough event trigger...")
	audioEle.play()
}

function renderBgmPaging(count) {

	// 예전 paging 삭제
	bgmPagination.innerHTML = ""

    let ul = document.createElement("ul")

    for(let index = 0; index < count; index++){
        let div = document.createElement("div")
        let li = document.createElement("li")

        div.classList.add("dot")
        if(index == BGM_INDEX) {
            div.classList.add("active")
        }

        li.append(div)
        ul.append(li)
    }

    bgmPagination.append(ul)
}

function updateBgm() {
	
	//canplayToggle = !canplayToggle
	
    // audio 객체 업데이트 (음악 시작, 아이콘 업데이트)
    singer.textContent = MUSIC_LIST[BGM_INDEX].artist
    songTitle.textContent = MUSIC_LIST[BGM_INDEX].musicName
    audioEle.src = "/resources/static/music/" + MUSIC_LIST[BGM_INDEX].musicPath + ".mp3"
	audioEle.load();	
	
    // 해당 페이지 위치를 페이징에 반영하기
    let dots = document.querySelectorAll(".dot");
	dots.forEach(dot => {
		dot.classList.remove("active")
	})
    dots[BGM_INDEX].classList.add("active")	
}

/* bgm carousel 기능 */
bgmPrevBtn.addEventListener("click", function(){
 
    if(BGM_INDEX > 0) {
        BGM_INDEX--;
    } else {
        BGM_INDEX = MUSIC_LIST.length - 1;
    }	
	
	updateBgm()

	if(PLAY_ICON === "fa-play") {
		playAudio();
	}
})

function clickNextBgm(){

    if(BGM_INDEX >= MUSIC_LIST.length - 1) {
        BGM_INDEX = 0        
    } else {
        BGM_INDEX++ 
    }

	updateBgm()

	if(PLAY_ICON === "fa-play") {
		playAudio();
	}
}

// 노래 끝났을 때 자동으로 다음 노래로 넘어감
audioEle.addEventListener("ended", clickNextBgm)
bgmNextBtn.addEventListener("click", clickNextBgm)

function pauseAudio() {
	audioEle.pause();
	playBtn.innerHTML = '<i class="fa-solid fa-play"></i>'	
}

function playAudio() {
	audioEle.play();
	playBtn.innerHTML = '<i class="fa-solid fa-pause"></i>'
}

playBtn.onclick = function(){
	
	PLAY_ICON = this.children[0].classList[1];
	
	if(PLAY_ICON === "fa-play") {
		// 일시 중지 -> 재생
		playAudio();
	} else {
		// 재생 중 -> 정지
		pauseAudio();
	}
}

///////////////////////////// RANDOM BTN /////////////////////////////
/* (슬라이드 렌더 후) 자동 전환 및 음악 재생 */
function afterLoadAutoMode(){
	// 자동 전환 carousel, 음악 재생
	INTERVAL = null
	TIME_OUT = null
	
	changeMode()
}

/* 랜덤 버튼 */
randomBtn.onclick = async function() {
	console.log("랜덤 리스트 불러오기");

	// 감정 태그 checked 다 제거
	EMOTION_BTN.forEach(btn => {
		btn.classList.remove("checked")
	})
	for(let plyBtn of playlistBtns) {
		plyBtn.classList.remove("selected")
	}
	
	slide.innerHTML = "";
	SLIDE_INDEX = 0
	TOTAL_REVIEW_COUNT = 0;
	REQUEST_PARAM = "emotionId"
	ID = Math.floor(Math.random() * 8);
	PAGE = 0
	await fetchAndRenderSlide(REQUEST_PARAM, ID, PAGE)
	//playAudio();
	
	// 사이드 바 닫힘
	closeSideBar()
	
	changeMode()
	
	this.blur();
	/*slide.focus();*/
	
/*	// 자동 전환 carousel, 음악 재생
	INTERVAL = null
	TIME_OUT = null	*/
}

///////////////////////////// PLAYLISTS /////////////////////////////
async function clickPlaylist() {
	console.log("플레이리스트 불러오기");
	
	for(let tag of EMOTION_BTN) {
		tag.classList.remove("checked")
	}
	// 클릭 시 색 스타일 
	for(let plyBtn of playlistBtns) {
		plyBtn.classList.remove("selected")
	}
	this.classList.add("selected")

	// 현재 서평 + 음악 초기화
	slide.innerHTML = "";	
	SLIDE_INDEX = 0	
	TOTAL_REVIEW_COUNT = 0;
	REQUEST_PARAM = "playlistId"
	ID = this.getAttribute("data-playlistno")
	PAGE = 0
	await fetchAndRenderSlide(REQUEST_PARAM, ID, PAGE)
	//playAudio();
	
	//  사이드 바 닫힘
	closeSideBar()
	
	changeMode()
	
	this.blur();
	/*slide.focus();*/
}

///////////////////////////// EMOTION BTN /////////////////////////////
async function clickEmotionBtn () {
	console.log("감정 리스트 불러오기");
	
	// 플레이리스트 버튼 초기화
	for(let plyBtn of playlistBtns) {
		plyBtn.classList.remove("selected")
	}
	// 감정태그 초기화
	for(let tag of EMOTION_BTN) {
		tag.classList.remove("checked")
	}
	this.classList.add("checked")
	
	// 감정태그에 해당하는 서평, 음악 리스트 불러오기 + 자동 전환 & 음악 재생
	slide.innerHTML = "";
	SLIDE_INDEX = 0 // 슬라이드 인덱스 초기화
	TOTAL_REVIEW_COUNT = 0;
	REQUEST_PARAM = "emotionId"
	ID = this.id
	PAGE = 0
	await fetchAndRenderSlide(REQUEST_PARAM, ID, PAGE)
	//playAudio();
	
	// 사이드 바 닫힘
	closeSideBar()
	
	IS_AUTO_MODE = false;
	changeMode()
	
	this.blur();
	/*slide.focus();*/
}

///////////////////////////// SIDE-BAR /////////////////////////////
function closeSideBar() {
    sideBar.classList.remove("show-sidebar");

    dim.classList.remove("show-dim")
    dim.addEventListener("transitionend", function(){
        this.classList.add("unstaged")
        this.removeEventListener("transitionend", arguments.callee)
    })

	window.addEventListener("keydown", spaceDownWindow)	
}

function openSideBar(){
    sideBar.classList.add("show-sidebar");

    dim.classList.remove("unstaged")
    dim.classList.add("show-dim")

	// 사이드바 사용시, 모드 전환 안되게
	window.removeEventListener("keydown", spaceDownWindow)	
}

// 사이드 바 열기, 닫기
sidebarToggle.addEventListener("click", openSideBar)

dim.onclick = closeSideBar;

closeBtn.addEventListener("click", closeSideBar)
