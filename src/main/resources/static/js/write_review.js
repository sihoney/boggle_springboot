let REVIEW_MAP = {};
let REVIEW_ID
let NICKNAME
let MODE

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
	
	// 폰트 버튼
	let btnFontList = $(".btn_font");
	btnFontList.each(function(index, btn) {
		btn.onclick = clickFontBtn
	})
	
	checkMode()
})

// 기록인지 수정인지 확인
function checkMode() {
	
	var currentURL = window.location.href;// 현재 페이지 URL을 가져옵니다.
	var queryString = currentURL.split('?')[1];// URL에서 쿼리 문자열(물음표 이후의 부분)을 추출합니다.
	
	// 쿼리 문자열이 존재하는지 확인합니다.
	if (queryString) {
	    
		console.log(queryString)

	    var params = new URLSearchParams(queryString);// 쿼리 문자열이 존재하면, 여기에서 쿼리 매개변수를 추출할 수 있습니다.

		REVIEW_ID = params.get("reviewId")
		REVIEW_MAP.reviewId = REVIEW_ID  

	    // 특정 매개변수의 유무를 확인합니다. 예를 들어, "paramName"이라는 매개변수가 있는지 확인하려면:
	    if (params.has("reviewId")) {
	        MODE = "수정하기"

			setProgressBar(1)
			setProgressBar(2)
			setProgressBar(3)
			setProgressBar(4)
			
			// 수정, 삭제 버튼에 이벤트 핸들러 부착  
			clickBook();
			
	    } else if(params.has("isbn")){
	        MODE = "기록하기"
			setProgressBar(1)
			
			// 수정, 삭제 버튼에 이벤트 핸들러 부착  
			clickBook();
	    }
	} else {
	    MODE = "기록하기"
	}

}

function clickEmotionBtn(){

	/* 스타일 옵션 출력 (초기화, 렌더)*/
	//$(".btn-group").children().remove()
	
	if(REVIEW_MAP !== undefined) {
		REVIEW_MAP.emotionId = this.id
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
	var wallpaperUrl = $this.css("background-image")
		
	//$("#review_box").css("background-color", backgroundColor)
	$("#review_box").css("background-image", wallpaperUrl)
	$("#review_box").css("background-size", "cover")
	
	if(REVIEW_MAP !== undefined) {
		//REVIEW_MAP.wallpaperName = wallpaperName
		REVIEW_MAP.wallpaperId = wallpaperId
	}	
	
	// 진행 바 
	if(REVIEW_MAP.fontId) {
		setProgressBar(3)
	}	
	/*	
	// 진행 바 
	setProgressBar(3)*/
	
/*	if(REVIEW_MAP !== undefined) {
		//REVIEW_MAP.wallpaperName = wallpaperName
		REVIEW_MAP.wallpaperId = wallpaperId
	}	
	
	// 텍스트아리아 스타일 변경
	if(wallpaperId === undefined) {
		var fontName = $this.css("font-family")
		var fontId = $this.data("fontid")
		
		$("#review_textarea").css("font-family", fontName)
		
		if(REVIEW_MAP !== undefined) {
			//REVIEW_MAP.fontName = fontName
			REVIEW_MAP.fontId = fontId
		}
	} else {
		var wallpaperUrl = $this.css("background-image")
			
		//$("#review_box").css("background-color", backgroundColor)
		$("#review_box").css("background-image", wallpaperUrl)
		$("#review_box").css("background-size", "cover")
		
		if(REVIEW_MAP !== undefined) {
			//REVIEW_MAP.wallpaperName = wallpaperName
			REVIEW_MAP.wallpaperId = wallpaperId
		}
	}*/
}

function clickFontBtn(e){
	e.preventDefault();
	
	var $this = $(this)
	var fontId = $this.data("fontid")
	var fontName = $this.text();
	
	// 텍스트아리아 스타일 변경
	$("#review_textarea").css("font-family", fontName)
	
	// 전송 데이터 맵에 정보 넣기
	if(REVIEW_MAP !== undefined) {
		REVIEW_MAP.fontId = fontId
	}
	
	// 진행 바 
	if(REVIEW_MAP.wallpaperId) {
		setProgressBar(3)
	}	
	
	/*	
	// 진행 바 
	setProgressBar(3)*/	
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
$("#btn_admit").on("click", async function(){

 	let reviewContent = $("#review_textarea").val()
	
	if(reviewContent === "" || isReadyToPost() === false) {
		alert("입력하지 않은 부분이 있습니다")
	}
	else {
		REVIEW_MAP.reviewContent = $("#review_textarea").val()
		
		let data = await postReview(REVIEW_MAP)
			
		console.log(data)
		
		REVIEW_MAP.reviewId = data	
		
		if(MODE == "수정하기") {
			$(".modal_question>p").text("수정되었습니다. 플레이리스트에 추가하시겠습니까?")
			
		}

		// 플리에 저장할거냐고 질문 모달 
		$(".modal_question").removeClass("unstaged")
		$(".modal_question").addClass("opaque")
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

/* 서평 저장 */
async function postReview(map) {
	
	try {
		let method;
		
		if(MODE === "수정하기") {
			console.log("수정하기")
			
			var url = "/reviews"
			method = "PUT"	
		} else {
			console.log("저장하기")
			
			//var url = "/registerReview"	
			var url = "/reviews"
			method = "POST"
		}	
		
		console.log(map)
		
		let response = await fetch(url, {
			method: method,
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
			
			console.log(data)
			
			renderMyPlaylist(data)
			
			// 이벤트 추가
			$(".modal_ply_ul").on("click", ".list", clickPlaylistModal)		
		})
		
		// 플리 모달 열기
		$(".modal_myply").removeClass("unstaged")
		$(".modal_myply").addClass("opaque")
		
	} else {
		//location.href = projectName + '/' + nickname
		location.href = '/my-reviews';
	}
})

// 서평을 플리에 저장, 이벤트 핸들러
async function clickPlaylistModal(){
	let $this = $(this)
	let playlistId = $(this).data("playlistid")
	
	$this.addClass("selected")
	
	//let playlistId = $(this).data("playlistId")
	//console.log(playlistId)
	
	postReviewPlaylist(playlistId, REVIEW_MAP.reviewId).then(data => {
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
		let response = await fetch(`/playlists/${playlistNo}/reviews/${reviewNo}`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			}
		})
		return response.json()
		
	} catch(error) {
		console.log("Failed to fetch: " + error)
	}
}
