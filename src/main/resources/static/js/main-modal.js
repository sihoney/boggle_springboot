const modalCloseBtn = document.querySelector(".modal-closeBtn")
const emotionSelectBox = document.querySelector(".emotion-select-box")
const addReviewModal = document.querySelector(".addReviewModal")
const addPlaylistForm = document.querySelector(".addPlaylist");
const addPlyInput = addPlaylistForm.querySelector(".addPly-input")
const plySubmitBtn = document.querySelector(".plySubmitBtn")
const modalBackground = document.querySelector(".modal-background")

let emotions_modal
let REVIEW_ID

function renderPlaylistSidebar(list) {	
	
	const ul_ply = document.querySelector(".playlist-list")
	
	list.forEach(plyVo => {
		let li = document.createElement("li")
		let p = document.createElement("p")
		
		li.setAttribute("data-playlistno", plyVo.playlistId)
		li.classList.add("playlistBtn")
		p.innerText = plyVo.playlistName
		
		li.onclick = clickPlaylist;
		
		li.append(p)
		ul_ply.append(li)
	})
}

// 새로운 플리 추가하기
async function postNewPlaylist(playlistName, emoNo){
	
	try {
		let obj = {
			playlistName : playlistName,
			//emotionName: emoNo
		}
		
		const response = await fetch("/api/playlist", {
			method: "POST",
			headers: {
				"Content-Type" : "application/json"
			},
			body : JSON.stringify(obj)
		})
		
		//return response.json();
		return response;	
	} catch(error) {
		console.log(error);
	}

}

async function clickMakePlaylistBtn(e) {
	e.preventDefault()
	
	if(addPlyInput.value == "") {
		alert("제목을 입력해주세요")
	} else {		
		let postResult = await postNewPlaylist(addPlyInput.value, addPlyInput.dataset.plyemono)
		
		// 플리 추가 섹션 안보이도록
		emotionSelectBox.classList.remove("show")
		addReviewModal.classList.remove("move")
		
		addPlyInput.value = null
		// 감정 태그 초기화
		for(let tag of emotions_modal) {
			tag.classList.remove("selected")
		}		

		// 모달 내 플리 업데이트	
		// 사이드바, 모달 플레이리스트 초기화
		document.querySelector(".playlist-box").querySelector("ul").innerHTML = ""
		document.querySelector(".playlist-list").innerHTML = ""
		let playlists = await getMyPlaylists(REVIEW_ID)	
		renderPlaylistSidebar(playlists)
		renderModalPly(playlists)	
	}	
}


// 모달 > 새 플리 저장
plySubmitBtn.addEventListener("click", clickMakePlaylistBtn)

// 모달 > 추가모달 나오게 하기 
addPlyInput.onclick = function() {
	emotionSelectBox.classList.add("show")
	addReviewModal.classList.add("move")
}

// 모달 닫기 버튼
modalCloseBtn.onclick = function(){
    modalBackground.classList.remove("show-modal")

	/* 모달 하단 집어넣기 */
	emotionSelectBox.classList.remove("show")
	addReviewModal.classList.remove("move")
	
	/* 감정 태그 초기화 */
	for(let tag of emotions_modal) {
		tag.classList.remove("selected")
	}
	
	/* 삭제한 enter key event 리스너 다시 더하기 */
	window.addEventListener("keydown", spaceDownWindow)
	
	/* input 값 초기화 */
	addPlyInput.value = null;
	
	/* 다시 자동 재생 */
	if(IS_AUTO_MODE == true) {
		changeMode()
	}
}

// 플리에 서평 추가
async function toggleReviewToPly(obj) {
	
	const response = await fetch("/api/review_playlist", {
		method: "POST", 
		headers: {"Content-Type" : "application/json"},
		body : JSON.stringify(obj)
	})
	
	//return response.json()	
	return response
}

function clickPlaylistModal() {
	
	let playlistNo = this.dataset.playlist_id
	let icon = this.children[1].children[0].classList[1]
	
	////// 화면 ICON 변경 ///////	
	if(icon == 'fa-plus') {
	    this.children[1].innerHTML = '<i class="fa-solid fa-check"></i>'
		this.classList.add("selected")
	} else { 
	    this.children[1].innerHTML = '<i class="fa-solid fa-plus"></i>'
		this.classList.remove("selected")
	}
	
	let obj = {
		playlistId: playlistNo,
		reviewId : REVIEW_ID
	}
	
	toggleReviewToPly(obj).then(data => {
		console.log(data.status + "건 | 서평을 플리에 저장 또는 삭제")
	})
}

function renderModalPly(list) {

	// 초기화
	document.querySelector(".playlist-box").querySelector("ul").innerHTML = ""

	list.forEach(item => {

		let li = document.createElement("li")
		let p = document.createElement("p")
		let btn = document.createElement("button")
		
		li.classList.add("playlist-li")
		li.classList.add("reviewAddBtn")
		p.innerText = item.playlistName
		li.dataset.playlist_id = item.playlistId;
		//li.dataset.review_id = reviewId;
		//btn.classList.add("reviewAddBtn")
		
		if(item.hasReview) { // 내가 만든 플리인 경우 -> 이미 추가했는지 확인
			btn.innerHTML = '<i class="fa-solid fa-check"></i>'
			li.classList.add("selected")	
		} else {
			btn.innerHTML = '<i class="fa-solid fa-plus"></i>'
		}		
		
		p.style.pointerEvents = 'none';
		btn.style.pointerEvents = 'none';
		
		li.onclick = clickPlaylistModal;
		
		li.append(p, btn)
	
		document.querySelector(".playlist-box").querySelector("ul").append(li)		
	})
}

async function getMyPlaylists(reviewNo) {
	
	try {
		let response = await fetch(`/playlists?reviewId=${reviewNo}`, {
			method: "GET", 
			headers: {
				"Content-Type" : "application/json"}
		})
		
		let data = await response.json();
		
		return data;		
	} catch(error) {
		console.log(error);
	}
}