let CRT_PAGE
let QUERY


/* 책 검색 API */
$("#searchbook-form").on("submit", function(e){
	e.preventDefault()

	QUERY = $("#searchbook-query").val()
	CRT_PAGE = 1
	
	setSearchedBook(QUERY, CRT_PAGE)
})

/* 모달 > 페이징 링크 클릭시 */
$(".pagination").on("click", ".page-item", function(e){
	e.preventDefault()
	
	var $this = $(this)
	var page = $this[0].innerText

	CRT_PAGE = parseInt(CRT_PAGE)
	if(page === '«') {
		CRT_PAGE -= 1
		
	} 
	else if(page === '»') {
		CRT_PAGE += 1
	} 
	else {
		CRT_PAGE = page
	}
		
	setSearchedBook(QUERY, CRT_PAGE)
})

async function setSearchedBook(query, page){
	
	searchBook(query, page).then(response => {
		
		var totalPageNo = response.totalPageNo
		var startBtnNo = response.startPage
		var endBtnNo = response.endPage
		var bookList = response.bookList
		
		// 응답 정보 출력 전, 현재 리스트 & 페이징 초기화 
		$("#modal-playlist").children().remove()
		$(".pagination").children().remove()
		
		// response 출력 
		renderBooks(bookList)
		
		// 페이징 출력 
		renderPaging(totalPageNo, startBtnNo, endBtnNo, page)	
	}).catch(error => {
		console.log(error)
	})
}

// 알라딘 api, 책 검색
async function searchBook(query, page) {
	try {
		let url = "/searchbook?query=" + query
		
		if(page === undefined || page === null) {
			url += "&page=1"
		} else {
			url += "&page=" + page
		}
		
		let response = await fetch(url)
		return response.json()
		
	} catch(error) {
		console.log("Failed to fetch: " + error)
		return error;	
	}
}	

function renderBooks(array) {
	
	array.forEach(function(item) {
		var str = ""
		str += '<li class="list" data-dismiss="modal" data-cover="'+ item.cover +'" data-author="'+ item.author +'" data-title = "'+ item.title +'"data-isbn="'+ item.isbn13 +'" data-url="'+ item.link +'" data-category="'+ item.categoryName +'" data-categoryId="'+ item.categoryId +'">'
		str += '	<div class="book-img-container">'
		str += '		<img src="'+ item.cover +'" alt="" class="img-thumbnail">'
		str += '	</div>'
		str += '	<div class="info-container">'
		str += '		<button class="book-title">'+ item.title +'</button>'
		str += '		<div class="book-author">'+ item.author +'</div>'
		/*str += '		<div class="review-count">서평수 '+ item.totalCnt +'</div>'*/
		str += '	</div>'
		str += '</li>'

		$("#modal-playlist").append(str)		
	})
}

function renderPaging(totalPageNo, startNo, endNo, pageNo) {
	
	var str = ''
	if(pageNo > 1) { 
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
	
	if(pageNo < totalPageNo) {
		str += '<li class="page-item"><a class="page-link" href="" aria-label="Next"> <span aria-hidden="true">&raquo;</span></a></li>'
	} 
	
		
	$(".pagination").append(str)
}

/* 모달 > 책 선택시 */
$("#modal-playlist").on("click", ".list", function() {
	var $this = $(this)

	REQUEST_INFO = $this.context.dataset
	
	/* 선택 섹션 화면에서 안보이게 & modal 닫기 */
	$(".jumbotron").css("display", "none")
	/*$("#modal_searchbook").modal("hide")*/
	
	/* 모달> input, search result 다 지우기 (~~~~~~~~~~~ 추가 ~~~~~~~~~)*/
	//$(".search_box").val("")
	//$("#modal-playlist").children().remove()		/*  에러 발생(모달이 안닫힘)  */
	
	/* 출력 전, 초기화 (처음 책 선택하는 건데도 출력된다...?) */
/*	if($(".selected-book") !== null) {
		$(".selected-book").remove()			
	}*/
	
	/* 선택된 책 정보 섹션 출력 */
	renderSelectedBook(REQUEST_INFO)

	/* 진행 바 */
	setProgressBar(1)
})	

function renderSelectedBook(map) {

	var str = ''
	str += '<div id="contents" class="clearfix selected-book" data-isbn="'+ map.isbn13 +'">'
	str += '	<div id="select_box">'
	str += '		<div id="bookVo">'
	str += '			<img id="book_img" src="'+ map.cover +'" alt="..." class="img-thumbnail">'
	str += '			<div id="book_detail">'
	str += '				<h1>'+ map.title +'</h1>'
	str += '				<h3>저자 '+ map.author +'</h3>'
	str += '				<div id="book_review">'
	//str += '					<span class="review_count">서평 수 </span><span class="review_num">'+ map.totalCnt +'</span>'
	str += '				</div>'
	str += '			</div>'
	str += '			<button id="btn_delete" type="button" class="btn btn-light">삭제</button>'
	//str += '			<button id="btn_modify" type="button" class="btn btn-light">수정</button>'
	str += '		</div>'	
	str += '	</div>'
	str += '</div>'
	
	$(".selectbook-header").after(str)
	
	/* 삭제 버튼 > 이벤트 핸들러 넣기 */
	$("#btn_delete").on("click", function(){ // 삭제
		$(".selected-book").remove()
		$(".jumbotron").css("display", "block")
		
		// 진행 바 
		$(".progressbar li").removeClass("active")
	})
	
	/* 수정 버튼 > 이벤트 핸들러 넣기 */
	/*$("#btn_modify").on("click", function(){
		$("#modal_searchbook").modal("show")
	})*/	
}

