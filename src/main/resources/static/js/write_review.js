let REQUEST_INFO
let NICKNAME

// 화면 로드되기 전, 감정태그 출력 
$("document").ready(function(){
	
	NICKNAME = $("#user-nickname").text()
	
	// 감정 버튼 - 이벤트 핸들러 넣기
	var btnlist = $("#btn_mood").children()
	btnlist.each(function(index, btn) {
		btn.onclick = clickEmotionBtn				
	})
	
	// 스타일 버튼 - 이벤트 핸들러 넣기
	var btnList = $(".btn_style")
	btnList.each(function(index, btn) {
		btn.onclick = clickStyleBtn					
	})
})

function clickEmotionBtn(){

	/* 스타일 옵션 출력 (초기화, 렌더)*/
	//$(".btn-group").children().remove()
	
	if(REQUEST_INFO !== undefined) {
		REQUEST_INFO.emotionId = this.id
	}
	
	//setStyleBtn(emoNo)			
	
	/* 하나만 선택 가능 */
	$("#btn_mood").children().each((index, item) => {
		item.classList.remove("active")
	})
	$(this).addClass("active")
	
	/* 진행 바 */
	setProgressBar(2)	
}

function clickStyleBtn(){

	var $this = $(this)
	var wallpaperId = $this.data("wallpaperid")
	
	// 텍스트아리아 스타일 변경
	if(wallpaperId === undefined) {
		var fontName = $this.css("font-family")
		var fontId = $this.data("fontid")
		
		$("#review_textarea").css("font-family", fontName)
		
		if(REQUEST_INFO !== undefined) {
			//REQUEST_INFO.fontName = fontName
			REQUEST_INFO.fontId = fontId
		}
	} else {
		var wallpaperUrl = $this.css("background-image")
			
		//$("#review_box").css("background-color", backgroundColor)
		$("#review_box").css("background-image", wallpaperUrl)
		$("#review_box").css("background-size", "cover")
		
		if(REQUEST_INFO !== undefined) {
			//REQUEST_INFO.wallpaperName = wallpaperName
			REQUEST_INFO.wallpaperId = wallpaperId
		}
	}
	
	// 진행 바 
	setProgressBar(3)
}

/* textarea 자동높이조절 */
$("#review_textarea").on("propertychange change keyup paste input",function(){
   $(this)[0].style.height='auto'
   $(this).height( $(this).prop('scrollHeight'))
})

$("#review_textarea").on("keyup", function(){
	let length = $(this).val().length
	
	if(length > 0) {
		$("#limit-text").text(length + "/200")
		
		/* 진행 바 */
		setProgressBar(4)
	}
	else if(length > 200) {
		$(this).val($(this).val().substring(0, 200))
	}
	else {
		/* 진행 바 */
		setProgressBar(-4)	
	}
})

function setProgressBar(stageNum){

	if(stageNum > 4) {
		$(".progressbar li").each(function(index, item){
			item.classList.add("active")
		})
	}
	else if(stageNum > 0) {
		$(".progressbar li:nth-child(" + stageNum + ")").addClass("active")
	}
	else if(stageNum < 0) {
		$(".progressbar li:nth-child(" + Math.abs(stageNum) + ")").removeClass("active")	
	}
	else {
		$(".progressbar li").each(function(index, item){
			item.classList.remove("active")
		})		
	}
}

/* 저장하기 & 수정하기 */
$("#btn_admit").on("click", function(){

 	let reviewContent = $("#review_textarea").val()
	if(reviewContent !== "" 
		|| reviewContent !== null 
		|| reviewContent !== undefined
		) {
		REQUEST_INFO.reviewContent = $("#review_textarea").val()
	}
	
	if(isReadyToPost() === false) {
		alert("입력하지 않은 부분이 있습니다")
	}
	else {
		let mode = $(this).text()
		
		postReview(mode, REQUEST_INFO).then(data => {
			/*
			if(mode == "수정하기") {
				nickname = data.redirect
				reviewNo = reviewNoQuery
				
				$(".modal_question>p").text("수정되었습니다. 플레이리스트에 추가하시겠습니까?")
				
			} else { // 저장하기
				nickname = data.redirect
				reviewNo = data.reviewNo // 저장한 서평 넘버
			}
			*/
			REQUEST_INFO.reviewId = data
			
			// 플리에 저장할거냐고 질문 모달 
			$(".modal_question").removeClass("unstaged")
			$(".modal_question").addClass("opaque")	
					
		}).catch(error => {
			console.log(error)
		})
	}
})

/* 작성 취소 */
$("#btn_cancle").on("click", function(){
	history.back(-1)
	//location.href = nickname + "/mybook"
})

/* 플리 모달 > 내 서평 보러가기 */
$(".modal_myply_btn").on("click", function(){
	
	location.href ='/' + NICKNAME + '/mybook'
})

function isReadyToPost(){
	let result = true
	
	$(".progressbar").children().each(function(index, item) {
		
		if(item.classList.contains("active") === false) {
			result = false
			return false // break
		}
	})
	
	return result
}

async function postReview(mode, map) {
	
	try {
		if(mode === "수정하기") {
			console.log("수정하기")
			var url = "/review/modifyReview"	
		} else {
			console.log("저장하기")
			var url = "/registerReview"	
		}	
		
		let response = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(map)
		})
		
		return response.json()
			
	} catch (error) {
		console.log("Failed to fetch: " + error)
	}	
}

/* 저장하시겠습니까 모달 - 예, 아니오 버튼 클릭 */
$(".mqBtn").on("click", function(){
	
	// 페이드 효과
	$(".modal_question").removeClass("opaque")
	$(".modal_question").one("transitionend", function(){
		$(".modal_question").addClass("unstaged")
	})
	
	let answer = $(this).val()
	
	if(answer == "yes") {
		getPlaylistByUserId().then(data => {
			
			renderMyPlaylist(data)
			
			// 이벤트 추가
			$(".modal_ply_ul").on("click", ".list", clickPlaylistModal)		
		})
		
		// 플리 모달 열기
		$(".modal_myply").removeClass("unstaged")
		$(".modal_myply").addClass("opaque")
		
	} else {
		//location.href = projectName + '/' + nickname
		location.href = '/' + NICKNAME + '/mybook'
	}
})

// 서평을 플리에 저장, 이벤트 핸들러
async function clickPlaylistModal(){
	let $this = $(this)
	let playlistId = $(this).data("playlistid")
	
	$this.addClass("selected")
	
	//let playlistId = $(this).data("playlistId")
	//console.log(playlistId)
	
	postReviewPlaylist(playlistId, REQUEST_INFO.reviewId).then(data => {
		console.log("플리에 저장했습니다. 결과: " + data)
		
		// 페이드 효과 (모달 닫기)
		$(".msg_modal").removeClass("unstaged")
		$(".msg_modal").addClass("opaque")
		
		setTimeout(function(){
			$(".msg_modal").removeClass("opaque")
			
			$(".msg_modal").one("transitionend", function(){
				$(".msg_modal").addClass("unstaged")
			})
		}, 2000)	
					
	}).catch(error => {
		alert('통신에 문제가 있습니다. 다시 시도해 주세요.')
	})	
}

async function getPlaylistByUserId() {
	try {
		let response = await fetch('/getMyPlaylist');
		
		return response.json();
	}catch (error) {
		console.log("Failed to fetch: " + error)
	}
}

function renderMyPlaylist(list) {
	list.forEach(function(item, index){
		str = ""
		str += '<li class="list" data-playlistId="'+ item.playlistId +'">'
		str += '	<div class="info-container">'
		str += '		<div class="playlist-title">' + item.playlistName + '</div>'
		str += '	</div>'
		str += '</li>'
		
		$(".modal_ply_ul").append(str)		
	})
}

async function postReviewPlaylist(playlistNo, reviewNo) {
	try {
		let response = await fetch("/reviewPlaylist", {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				'playlistId': playlistNo,
				'reviewId': reviewNo
			})
		})
		return response.json()
		
	} catch(error) {
		console.log("Failed to fetch: " + error)
	}
}
