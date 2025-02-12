/*
var query
var pageNo = 1 // crt_page
*/
var reviewNoQuery

var userNo = $(".userNo").val()
var bookNo
var styleNo
var reviewContent
var bookTitle
var author
var bookURL
var genreName
var coverURL
var genreNo

let nickname

let projectName
	
////////////////////////////////////////////////////
// EVENT LISTENER
////////////////////////////////////////////////////	

/* 화면 로드되기 전, 감정태그 출력 */
$("document").ready(function(){

	const urlObj = new URL(location.href)
	const urlParams = urlObj.searchParams
	projectName = "/" + urlObj.pathname.split("/")[1]
	
	const bookTitleQuery = urlParams.get("bookTitle")
	reviewNoQuery = urlParams.get("reviewNo")

	// 감정버튼, 스타일 버튼 
/*	getEmotion().then(data => {
		// 화면 출력 
		renderEmotionBtn(data)

		// 이벤트 핸들러 넣기 
		var btnlist = $("#btn_mood").children()
		btnlist.each(function(index, btn) {
			btn.onclick = clickEmoBtn				
		})			
	})*/
	/*
	// 이벤트 핸들러 넣기 
	var btnlist = $("#btn_mood").children()
	btnlist.each(function(index, btn) {
		btn.onclick = clickEmoBtn				
	})
	*/
	// case. 서평 수정 (수정하기)
	if(reviewNoQuery !== null && bookTitleQuery === null) {	
		console.log(">>서평 정보 불러오기")
			
		$("#btn_admit").text("수정하기")

		getReview(reviewNoQuery).then(data => {

			bookNo = data.isbn13
			bookURL = data.link
			coverURL = data.cover
			author = data.author
			genreNo = data.categoryId
			genreName = data.categoryName
			bookTitle = data.title
			
			emoNo = data.emoNo
			reviewContent = data.reviewContent
			styleNo = data.styleNo
			
			let styleName = data.styleName
			let totalCnt = data.totalCnt
			let background = data.imgURL

			// 1. 책 렌더
			renderSelectedBook({bookNo, coverURL, bookTitle, author, totalCnt})
			
			// 선택 섹션 화면에서 안보이게 & modal 닫기 
			$(".jumbotron").css("display", "none")
			
			// 2. 감정 버튼 선택 (emoNo)
			$("#"+emoNo).addClass("active")
			
			// 3. 스타일 버튼 나열
			setStyleBtn(emoNo)
		
			// 4. textarea 글  (reviewContent)
			$("#review_textarea").val(reviewContent)
			$("#limit-text").text(reviewContent.length + "/200")	
			
			// 5. 스타일 적용
			let arr = styleName.split(",")
			let backgroundColor = arr[0]
			let fontFamily = arr[1]
			
			if(background != null) {
				background = "url(" + projectName + "/asset/img/review_card/" + background + ") no-repeat"
				$("#review_box").css("background", background)						
			}
			$("#review_box").css("background-color", backgroundColor)
			$("#review_box>textarea").css("font-family", fontFamily)
			$("#review_box").css("background-size", "cover")

			// 5. 진행바
			setProgressBar(5)			
		})
	}
	// case. 책 상세 
	else if(bookTitleQuery !== null && reviewNoQuery === null){
		/* bookNo로 db에서 책 정보불러와서 선택된 책 구역에 렌더 & 진행바*/
		console.log(">>책 정보 불러오기")
		
		getBook(bookTitleQuery).then(data => {
			console.log(data)
			
			let item = data[0]

			// 저장할 때 필요한 정보 저장
			bookNo = item.isbn13
			bookTitle = item.title
			author = item.author
			bookURL = item.link
			coverURL = item.cover
			genreNo = item.categoryId
			genreName = item.categoryName
			
			let totalCnt = item.totalCnt
			// 화면 렌더 
			renderSelectedBook({bookNo, coverURL, bookTitle, author, totalCnt})

			// 선택 섹션 화면에서 안보이게 
			$(".jumbotron").css("display", "none")			
		})
	}
})

/* 책 검색 API */
/*$("#query-form").on("submit", function(e){
	e.preventDefault()

	let arr = $("#query-form").serializeArray()
	query = arr[0].value
	let obj = {
		query : query
	}
	
	setSearchedBook(obj)
})*/

/* 모달 > 페이징 링크 클릭시 */
/*$(".pagination").on("click", ".page-item", function(e){
	e.preventDefault()
	
	var $this = $(this)
	var target = $this[0].innerText

	pageNo = parseInt(pageNo)
	
	if(target === '«') {
		pageNo -= 1
		
	} 
	else if(target === '»') {
		pageNo += 1
	} 
	else {
		pageNo = target
	}
	
	console.log(pageNo + "번 페이지 클릭")
	
	var obj = {
		query: query,
		crtPage: pageNo
	}
	
	setSearchedBook(obj)
})*/

/* 모달 > 책 선택시 */
/*
$("#modal-playlist").on("click", ".list", function() {
	var $this = $(this)

	// isbn, title, author, imgURL, link, category
	bookNo = $this.data("isbn")
	coverURL = $this[0].children[0].children[0].currentSrc
	bookTitle = $this[0].children[1].children[0].innerText
	author = $this[0].children[1].children[1].innerText
	bookURL = $this.data("url")					// db에 저장할 때 필요한 책 정보 (!)
	genreName = $this.data("category")
	genreNo = $this.data("categoryid")
	
	var totalCnt = $this[0].children[1].children[2].innerText
	totalCnt = totalCnt.split(" ")[1]
	
	// 선택 섹션 화면에서 안보이게 & modal 닫기 
	$(".jumbotron").css("display", "none")
	$("#modal_searchbook").modal("hide")
	
	// 모달> input, search result 다 지우기 (~~~~~~~~~~~ 추가 ~~~~~~~~~)
	$(".search_box").val("")
	//$("#modal-playlist").children().remove()		//  에러 발생(모달이 안닫힘)  
	
	// 출력 전, 초기화 (처음 책 선택하는 건데도 출력된다...?) 
	if($(".selected-book") !== null) {
		$(".selected-book").remove()			
	}
	
	// 선택된 책 정보 섹션 출력
	renderSelectedBook({bookNo, coverURL, bookTitle, author, totalCnt})

	// 진행 바
	setProgressBar(1)
})	
*/
/*
$("#review_textarea").on("keyup", function(){
	let length = $(this).val().length
	
	if(length > 0) {
		$("#limit-text").text(length + "/200")
		
		// 진행 바 
		setProgressBar(4)
	}
	else if(length > 200) {
		$(this).val($(this).val().substring(0, 200))
	}
	else {
		// 진행 바
		setProgressBar(-4)	
	}
})
*/
/* textarea 자동높이조절 
$("#review_textarea").on("propertychange change keyup paste input",function(){
   $(this)[0].style.height='auto'
   $(this).height( $(this).prop('scrollHeight'))
})
*/
	
/* 저장하시겠습니까 모달 - 예, 아니오 버튼 클릭 */
/*
$(".mqBtn").on("click", function(){
	// 페이드 효과
	$(".modal_question").removeClass("opaque")
	
	$(".modal_question").one("transitionend", function(){
		$(".modal_question").addClass("unstaged")
	})
	
	let answer = $(this).attr("class").split(/\s+/)[0]
	
	if(answer == "yes") {
		getMyPlaylist(userNo).then(data => {
			console.log(data)
			
			renderMyPlaylist(data)
			
			// 이벤트 추가
			$(".modal_ply_ul").on("click", ".list", function(){			
				$(this).addClass("selected")
				
				let playlistno = $(this).data("playlistno")
				
				addReviewToPly(playlistno, reviewNo).then(data => {
					console.log("플리에 저장했습니다. 결과: " + data)
					
					if(data == 1) {
						// 페이드 효과 (모달 닫기)
						$(".msg_modal").removeClass("unstaged")
						$(".msg_modal").addClass("opaque")
						
						setTimeout(function(){
							$(".msg_modal").removeClass("opaque")
							
							$(".msg_modal").one("transitionend", function(){
								$(".msg_modal").addClass("unstaged")
							})
						}, 2000)				
					} 
					else {
						alert('통신에 문제가 있습니다. 다시 시도해 주세요.')
					}					
				})
			})			
		})
		
		// 플리 모달 열기
		$(".modal_myply").removeClass("unstaged")
		$(".modal_myply").addClass("opaque")
		
	} else {
		location.href = projectName + '/' + nickname
	}
})
*/
/*
// 기록하기 & 수정하기 
$("#btn_admit").on("click", function(){
	
	console.log("등록하기")
	
	var obj = {
			// book 관련 정보
			bookNo: bookNo, 
			bookTitle: bookTitle, 
			author: author, 
			bookURL: bookURL, 
			genreNo: genreNo, 
			coverURL: coverURL, 
			genreName: genreName, 
			
			// 서평 관련 정보
			userNo: userNo, 
			styleNo: styleNo, 
			reviewContent: $("#review_textarea").val(),
			reviewNo: reviewNoQuery 
		}
		
	if(userNo === null || userNo === 0) {
		alert("로그인 후 이용해 주세요")
	} 
	else if(isReadyToPost() === false) {
		alert("입력하지 않은 부분이 있습니다")
	}
	else {
		let mode = $(this).text()
		
		postReview(mode, obj).then(data => {
			console.log(data)

			if(mode == "수정하기") {
				nickname = data.redirect
				reviewNo = reviewNoQuery
				
				$(".modal_question>p").text("수정되었습니다. 플레이리스트에 추가하시겠습니까?")
				
			} else { // 저장하기
				nickname = data.redirect
				reviewNo = data.reviewNo // 저장한 서평 넘버
			}
			
			// 플리에 저장할거냐고 질문 모달 
			$(".modal_question").removeClass("unstaged")
			$(".modal_question").addClass("opaque")				
		})
	}
})

// 작성 취소
$("#btn_cancle").on("click", function(){
	
	history.back(-1)
})
*/

/* 
//플리 모달 > 내 서평 보러가기 
$(".modal_myply_btn").on("click", function(){
	
	location.href = projectName + '/' + nickname
})
*/
////////////////////////////////////////////////////
// EVENT HANDLER
////////////////////////////////////////////////////
/*
function clickEmoBtn(){
	// 스타일 옵션 출력 (초기화, 렌더)
	$(".btn-group").children().remove()
	
	var emoNo = this.id
	
	setStyleBtn(emoNo)			
	
	// 하나만 선택 가능 
	$("#btn_mood").children().each((index, item) => {
		item.classList.remove("active")
	})
	$(this).addClass("active")
	
	// 진행 바 
	setProgressBar(2)	
}

function clickStyleBtn(){
	// 진행 바 
	setProgressBar(3)
	
	// 텍스트아리아 스타일 변경
	var background = $(this).css("background")
	var backgroundColor = $(this).css("background-color")
	var fontFamily = $(this).css("font-family")
	styleNo = $(this).data("styleno")

	$("#review_box").css("background-color", backgroundColor)
	$("#review_box").css("background", background)
	$("#review_textarea").css("font-family", fontFamily)	
}
*/
	
////////////////////////////////////////////////////////
// UTIL
////////////////////////////////////////////////////////
/*
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
*/
/*
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
*/
/*
async function setStyleBtn(emoNo){
	getStyle(emoNo).then(data => {
		// 스타일 버튼 > 화면 렌더
		renderStyleBtn(data)

		// 스타일 버튼 > 이벤트 추가
		var btnList = $(".btn_style")
		btnList.each(function(index, btn) {
			btn.onclick = clickStyleBtn					
		})				
	})	
}
*/
/*async function setSearchedBook(obj){
	searchBook(obj).then(data => {
			
		var startBtnNo = data.startBtnNo
		var endBtnNo = data.endBtnNo
		var array = data.list
		
		// 응답 정보 출력 전, 현재 리스트 & 페이징 초기화 
		$("#modal-playlist").children().remove()
		$(".pagination").children().remove()
		
		// response 출력 
		renderBooks(array)
		
		// 페이징 출력 
		renderPaging(startBtnNo, endBtnNo, 1)		
	})	
}*/
		
////////////////////////////////////////////////////////
// FETCH
////////////////////////////////////////////////////////
/*
async function getEmotion(){
	try {
		let response = await fetch(projectName + "/emotion")
		
		return response.json()
	} catch(error) {
		console.log("Failed to fetch: " + error)
	}
}

async function getStyle(emoNo) {
	try {
		let response = await fetch(projectName + "/review/getStyle?emoNo=" + emoNo)
		
		return response.json()		
	} catch(error) {
		console.log("Failed to fetch: " + error)
	}
}
*/

async function getBook(bookname) {
	try {
		let response = await fetch(projectName + "/review/getBookInfo?bookTitle=" + bookname)
		
		return response.json()		
	} catch(error) {
		console.log("Failed to fetch: " + error)	
	}
}

async function getReview(reviewNo) {
	try {
		let response = await fetch(projectName + "/review/getPrevReviewInfo?reviewNo=" + reviewNo)
		
		return response.json()		
	} catch(error) {
		console.log("Failed to fetch: " + error)
	}
}

// 알라딘 api, 책 검색
/*async function searchBook(obj) {
	try {
		let url = projectName + "/searchbook?query=" + obj.query
		if(obj.crtPage !== undefined) {
			url += "&page=" + crtPage
		}
		
		let response = await fetch(url)
		
		return response.json()
	} catch(error) {
		console.log("Failed to fetch: " + error)	
	}
}*/	

/*
async function postReview(mode, obj) {
	try {
		if(mode === "수정하기") {
			console.log("수정하기")
			var url = projectName + "/review/modifyReview"	
		} else {
			console.log("저장하기")
			var url = projectName + "/review/addReview"	
		}	
		
		let response = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(obj)
		})
		
		return response.json()
			
	} catch (error) {
		console.log("Failed to fetch: " + error)
	}	
}
*/
/*
async function getMyPlaylist(userNo) {
	try {
		let response = await fetch(projectName + '/review/getMyPlaylist?userNo=' + userNo)
		
		return response.json()
	}catch (error) {
		console.log("Failed to fetch: " + error)
	}
}

async function addReviewToPly(playlistNo, reviewNo) {
	try {
		let response = await fetch(projectName + "/review/addReviewToPly?playlistNo=" + playlistNo + "&reviewNo=" + reviewNo)
		
		return response.json()
		
	} catch(error) {
		console.log("Failed to fetch: " + error)
	}
}
*/	
///////////////////////////////////////////////
// RENDER
///////////////////////////////////////////////
/*
function renderEmotionBtn(arr) { 
	
	arr.forEach(function(item, index) {
		
		var str = '<button type="button" class="btn btn-primary" id="'+ item.emoNo +'">'+ item.emoName +'</button>'

		$("#btn_mood").append(str)			
	})
}	
	
function renderStyleBtn(list) {
	
	list.forEach(function(item) {
		
		var arr = item.styleName.split(",")
		
		var background = arr[0]
		var font = arr[1]

		if(item.imgurl == null){
			var str = '<button data-styleNo="'+ item.styleNo +'"class="btn_style btn-outline-secondary" style="background: '+background+'; font-family: '+ font +'">폰트</button>'	
		}else{
			var str = '<button data-styleNo="'+ item.styleNo +'"class="btn_style btn-outline-secondary" style="background: url('+ projectName +'/asset/img/review_card/'+ item.imgurl +') no-repeat; background-size: cover; font-family: '+ font +'">폰트</button>'
		}
		
		$(".btn-group").append(str)		
	})
}	
*/
/*
function renderSelectedBook(obj) {

	var str = ''
	str += '<div id="contents" class="clearfix selected-book" data-isbn="'+ obj.bookNo +'">'
	str += '	<div id="select_box">'
	str += '		<div id="bookVo">'
	str += '			<img id="book_img" src="'+ obj.coverURL +'" alt="..." class="img-thumbnail">'
	str += '			<div id="book_detail">'
	str += '				<h1>'+ obj.bookTitle +'</h1>'
	str += '				<h3>저자 '+ obj.author +'</h3>'
	str += '				<div id="book_review">'
	str += '					<span class="review_count">서평 수 </span><span class="review_num">'+ obj.totalCnt +'</span>'
	str += '				</div>'
	str += '			</div>'
	str += '			<button id="btn_delete" type="button" class="btn btn-light">삭제</button>'
	str += '			<button id="btn_modify" type="button" class="btn btn-light">수정</button>'
	str += '		</div>'	
	str += '	</div>'
	str += '</div>'
	
	$(".selectbook-header").after(str)
	
	// 삭제 버튼 > 이벤트 핸들러 넣기 
	$("#btn_delete").on("click", function(){ // 삭제
		$(".selected-book").remove()
		$(".jumbotron").css("display", "block")
		
		// 진행 바 
		$(".progressbar li").removeClass("active")
	})
	// 수정 버튼 > 이벤트 핸들러 넣기 
	$("#btn_modify").on("click", function(){
		$("#modal_searchbook").modal("show")
	})		
}

function renderMyPlaylist(list) {
	list.forEach(function(item, index){
		str = ""
		str += '<li class="list" data-playlistNo="'+ item.playlistNo +'">'
		str += '	<div class="info-container">'
		str += '		<button class="tagBtn">'+ item.emoName +'</button>'
		str += '		<div class="playlist-title">' + item.playlistName + '</div>'
		str += '		<div class="username">' + item.nickname + '</div>'
		str += '	</div>'
		str += '</li>'
		
		$(".modal_ply_ul").append(str)		
	})
}	
*/
/*function renderBooks(array) {
	
	array.forEach(function(item) {
		var str = ""
		str += '<li class="list" data-dismiss="modal" data-isbn="'+ item.isbn13 +'" data-url="'+ item.link +'" data-category="'+ item.categoryName +'" data-categoryId="'+ item.categoryId +'">'
		str += '	<div class="book-img-container">'
		str += '		<img src="'+ item.cover +'" alt="" class="img-thumbnail">'
		str += '	</div>'
		str += '	<div class="info-container">'
		str += '		<button class="book-title">'+ item.title +'</button>'
		str += '		<div class="book-author">'+ item.author +'</div>'
		str += '		<div class="review-count">서평수 '+ item.totalCnt +'</div>'
		str += '	</div>'
		str += '</li>'

		$("#modal-playlist").append(str)		
	})
}*/

/*function renderPaging(startNo, endNo, pageNo) {
	
	var str = ''
	if(pageNo == 1) {
		str += '<li class="page-item" style="pointer-events: none"><a class="page-link" href="" aria-label="Previous"> <span aria-hidden="true">&laquo;</span></a></li>'	
	}
	else {
		str += '<li class="page-item"><a class="page-link" href="" aria-label="Previous"> <span aria-hidden="true">&laquo;</span></a></li>'
	}
	
	for(var i = startNo; i <= endNo; i++) {
		if(i == pageNo) {
			str += '<li class="page-item active"><a class="page-link" href="">' + i + '</a></li>'
		}
		else {
			str += '<li class="page-item"><a class="page-link" href="">' + i + '</a></li>'	
		}
	}
	
	str += '<li class="page-item"><a class="page-link" href="" aria-label="Next"> <span aria-hidden="true">&raquo;</span></a></li>'
	
	$(".pagination").append(str)
}*/
	