let EMOTION_BTN = document.querySelectorAll(".emoTag")
const playlistBtns = document.querySelectorAll(".playlistBtn")
let logStatus = document.querySelector(".login").getAttribute("data-logstatus")

//////////////// 두 번 이상 사용하는 경우만 위에 정의하기 /////////////////
let PAGE = 0;
let REQUEST_PARAM;
let ID;

/////////////////////// EVENT ///////////////////////

window.addEventListener("DOMContentLoaded", async function(){
	
	const urlObj = new URL(location.href)
	const urlParams = urlObj.searchParams
	
	PLAYLIST_ID = urlParams.get("playlistId")
			
	// 위 화살표 아이콘 안보이게
	upBtn.style.display = "none";
	
	// 서평뮤직 리스트
	if(PLAYLIST_ID === null) {
		REQUEST_PARAM = "emotionId"
		ID = Math.floor(Math.random() * 8);
		PAGE = 0
		
		fetchAndRenderSlide(REQUEST_PARAM, ID, PAGE)
	}
	else {
		REQUEST_PARAM = "playlistId"
		ID = PLAYLIST_ID
		PAGE = 0
		
		fetchAndRenderSlide(REQUEST_PARAM, ID, PAGE)
	}
	
	// 감정 태그 이벤트 추가
	// 1. 사이드바
	EMOTION_BTN.forEach(emoTag => {
		emoTag.onclick = clickEmotionBtn
	})

	// 로그인 상태 -> 플리 이벤트 추가
	if(logStatus === "login") {
		playlistBtns.forEach(plybtn => {
			plybtn.onclick = clickPlaylist
		})	
	}

	// 2. 모달
	emotions_modal = document.querySelectorAll(".tag")
	emotions_modal.forEach(emoTag => {
		emoTag.onclick = function(){
			
			for(let tag of emotions_modal) {
				tag.classList.remove("selected")
			}
			emoTag.classList.add("selected")
			
			addPlyInput.setAttribute("data-plyEmoNo", this.getAttribute("data-emono"))
		}		
	})	
})

//////////////////////////// KEY //////////////////////////////
/* 스페이스 바, 슬라이드 재생&중지 */
window.addEventListener("keydown", spaceDownWindow)

function spaceDownWindow(e) {
	if(e.code === 'Space') {
        changeMode()
    }
}