
//===============[플레이리스트 서평추가 모달]===============================
//변수선언
//- 모달 페이징 요청
var crtPage = 1;
var reviewChkBoxArr = [];

var playlistNo = $('#playlistLike').data('playlistno');
var userNo = $('#playlistLike').data('userno');			// ply maker's userNo
var nickname = $('#playlistLike').data('nickname');		// ply maker's nickname

//===============[페이지 로딩]=========================================
$(document).ready(function(){
	console.log('페이지 로딩 성공');
	
	playlistLike();
	
})

//===============[플레이리스트 모달 ready/close]==========================
/*모달 ready*/
$('#playlist-add').on('click', function(){

	console.log('추가 클릭');
	
	$('#review-add').modal({backdrop: 'static', keyboard: false});
	$('#review-add').modal('show'); 
	$('#reviewAll').data('playlistno')

	/* 페이지 리스트 요청*/
	fetchList(crtPage, playlistNo);

/*	renderPaging();
	console.log('페이지 요청');*/

})

/*모달창 닫기*/
$('.modal-close').on('click', function(){
	
	$('#review-add').modal('hide');
	$('#reviewAll').empty();
	
	crtPage = 1;
	reviewChkBoxArr = [];

});

//===============[데이터 요청]=========================================
//페이지 클릭 해당 버튼 리스트 요청
$('#addModal-pagination').on('click',  '.page-item',function(e){
	e.preventDefault()
	
	let dataset = this.children[0].dataset
	
	// 초기화
	$('#reviewAll').empty();
	
	if(dataset.item != undefined) { // item(prev, next)
		if(dataset.item == 'prev') {
			// int로 변환
			crtPage *= 1
			crtPage -= 1
			fetchList(crtPage, playlistNo)
		}
		else {
			// int로 변환
			crtPage *= 1
			crtPage += 1
			fetchList(crtPage, playlistNo)
		}
	}
	else { 							// crtpage
		crtPage = dataset.crtpage	
		fetchList(crtPage, playlistNo)
	}
})
	

//데이터 요청(페이지,리스트)
function fetchList(crtPage, playlistNo){

	console.log("요청:"+crtPage);
	
	$.ajax({
		url : "modalListPage",
		type : "get",
		data : {
			crtPage: crtPage,
			playlistNo : playlistNo
		},
	
		dataType : "json",
		success : function(playlistMap){
		/*성공시 처리해야될 코드 작성*/
			console.log(playlistMap);
			
			// 페이징 렌더
			renderPaging(playlistMap.prev, playlistMap.next, playlistMap.startPageBtnNo, playlistMap.endPageBtnNo, crtPage);
			
			// 서평 리스트 렌더
			for(var i = 0; i < playlistMap.modalList.length; i++){
				//그리기
				render(playlistMap.modalList[i]);
			}
		
		},
		error : function(XHR, status, error) {
			console.error(status + " : " + error);
		}
		
	});
	
	
}

//검색 내용 요청
function getSearch(searchTxt, playlistNo, crtPage){
	console.log('검색요청');
	
/*	var inputTxt = $('#reviewSearch').val();
	console.log('검색:'+inputTxt);*/
	
	$.ajax({
		url : "reviewSearch",
		type : "post",
		data : {
			SearchTxt: searchTxt,
			playlistNo : playlistNo,
			crtPage, crtPage
		},
		
		dataType : "json",
		success : function(map){
			/*성공시 처리해야될 코드 작성*/
			prev = map.prev
			next = map.next
			startPageBtnNo = map.startPageBtnNo
			endPageBtnNo = map.endPageBtnNo
			searchResult = map.searchResult
			
			console.log(searchResult)
			
			//초기화(리스트,페이징)
			$('#reviewAll').empty();
			$('#addModal-pagination').html('');
			
			if(searchResult.length == 0){
				emptyResult();
			}
			else{
				//검색 결과 리스팅
				for(var i =0; i<searchResult.length; i++){
					render(searchResult[i]);
				}
				
				renderPaging(prev, next, startPageBtnNo, endPageBtnNo, crtPage)
			}

			$('#reviewSearch').val('');
		},
		error : function(XHR, status, error) {
		console.error(status + " : " + error);
		}
	});
	
	
}

//체크박스 등록 요청 (모달>서평들 플리에 저장)
function addReviewToPly(reviewChkBoxArr){
	
	var playlistNo = $('#reviewAll').data('playlistno');
	var userNo = $('#reviewAll').data('userno');

	// 모달 닫기
	$('#review-add').modal('hide');
	// 모달 > 리스트 초기화
	$('#reviewAll').empty();
	
	$.ajax({
		url : "addReviews",
		type : "post",
		//contentType : "application/json",
		data : {
				reviewChkBoxArr: reviewChkBoxArr,
				playlistNo: playlistNo,
				userNo: userNo
		},
		
		dataType : "json",
		success : function(result){
		/*성공시 처리해야될 코드 작성*/
			
			console.log('데이터 추가 성공');
			console.log(result);
			
			if(result == 1){
				location.href="folder?playlistNo="+playlistNo+"&userNo="+userNo+"&nickname="+nickname+"&crtPage=1";
			}else{
				alert('앗! 다시 시도해주세요! :-)')
			}
		},
		
		error : function(XHR, status, error) {
		console.error(status + " : " + error);
		}
	});
	
}

//로딩시 좋아요 데이터 요청 (배너, 좋아요)
function playlistLike(){
	
	console.log('로딩시 좋아요 체크');
	
	var playlistNo = $('#playlistLike').data('playlistno');
	var userNo = $('#playlistLike').data('userno');
	
	console.log('플리'+playlistNo);
	console.log('유저'+userNo);
	
	$.ajax({
		url : "checkPlyAlreadyLiked",
		type : "get",
		data : {
			playlistNo: playlistNo,
			userNo: userNo
		},
		
		dataType : "json",
		success : function(checkLike){
		/*성공시 처리해야될 코드 작성*/
			console.log('좋아요 체크:'+checkLike);

			//좋아요(화면 출력)
			if(checkLike == 0){
				$('#likeview').attr('class','glyphicon glyphicon-heart-empty');
			}else{
				$('#likeview').attr('class','glyphicon glyphicon-heart');
			}
		
		},
		error : function(XHR, status, error) {
		console.error(status + " : " + error);
		}		
	});
}

/*좋아요 취소*/
function playlistUnlike(){
	
	console.log('좋아요 취소');
	
	var playlistNo = $('#playlistLike').data('playlistno');
	var userNo = $('#playlistLike').data('userno');
	
	console.log('플리'+playlistNo);
	console.log('유저'+userNo);
	
	$.ajax({
		url : "playlistUnlike",
		type : "post",
		data : {
				playlistNo: playlistNo,
				userNo: userNo
		},
		
		dataType : "json",
		success : function(unlike){
		/*성공시 처리해야될 코드 작성*/
			console.log('좋아요 취소:'+unlike);

			//좋아요(화면 출력)
			if(unlike == 1){
				$('#likeview').attr('class','glyphicon glyphicon-heart-empty');
			}else{
				$('#likeview').attr('class','glyphicon glyphicon-heart')
			}
		
		},
		error : function(XHR, status, error) {
			console.error(status + " : " + error);
		}
		
	});
	
}


/*좋아요 등록*/
function addplaylistLike(){
	
	console.log('좋아요 등록');
	
	console.log('플리'+playlistNo);
	console.log('유저'+userNo);
	
	$.ajax({
		url : "addplaylistLike",
		type : "post",
		data : {
				playlistNo: playlistNo,
				userNo: userNo
		},
		
		dataType : "json",
		success : function(like){
		/*성공시 처리해야될 코드 작성*/
			console.log('플리 좋아요:'+like);
			console.log(typeof like);

			//좋아요(화면 출력)
			if(like == 1){
				$('#likeview').attr('class','glyphicon glyphicon-heart')
			}else{
				$('#likeview').attr('class','glyphicon glyphicon-heart-empty');
			}
		
		},
		error : function(XHR, status, error) {
		console.error(status + " : " + error);
		}
		
	});
	
}

//서평 삭제
/*
function reviewDelete(){
	
	console.log('서평삭제 요청');
	var reviewNo = $('#reviewDelete').data('reviewno');
	console.log(reviewNo);
	
	$.ajax({
		url : "reviewDelete",
		type : "post",
		data : {reviewNo: reviewNo},
		
		dataType : "json",
		success : function(deleteResult){
		// 성공시 처리해야될 코드 작성
			
			if(deleteResult == 1){
				location.href="folder?playlistNo="+playlistNo+"&userNo="+userNo+"&crtPage=1";
				alert('서평이 삭제되었습니다! :-)');
			}else{
				alert('다시 시도해 주세요! :-/');
			}
		
		},
		error : function(XHR, status, error) {
		console.error(status + " : " + error);
		}
	});
}
*/

function toggleLikeReview(reviewNo, classname){
	
	let like = 1
	if(classname == 'glyphicon-heart-empty') {
		like = 0
	}
		
	$.ajax({
		url: "toggleLikeReview",
		type: "post",
		data: {
			reviewNo: reviewNo,
			like: like
		},
		dataType: "json",
		success: function(data) {
			
			console.log(data + "건 좋아요 토글 성공")
		},
		error : function(XHR, status, error) {
			console.log(status + " : " + error)
		}
	})
}

function fetchDeleteReview(reviewNo) {
	
	$.ajax({
		url: "deleteReviewFromPly",
		type: "post",
		data: {
			reviewNo: reviewNo,
			playlistNo : playlistNo,
			makerUserNo: userNo
		},
		dataType: "json",
		success: function(data) {
						
			console.log(data + "회, 삭제 완료")
			
			/*
			-> location.href 사용하면 session authUser 가 사라짐(서버에서 보내는 거기 때문)
			-> response body로 서평부분만 업데이트하려고 하면 jsp(html)를 수정해야함(c 태그 foreach를 없애야 함)
			-> /folder(서버) 로 보내기 (화면 업데이트가 안됨)
			-> 서버 쪽에서 삭제하고 click-playlist.jsp 화면으로 보냄 (화면 업데이트가 안됨)
			*/
			
			location.reload()
		},
		error: function(XHR, status, error) {
			console.log(status + " : " + error)
		}
	})
}

//===============[화면 출력]===================================================
//해당 페이지 서평 리스트
function render(vo){
	
	var str = '';
	str +='	<li> ';
	str +='		<div class="review-card"> ';
	str +=' 		<p class="bookname">'+vo.bookTitle+'</p> ';
	str +=' 		<p class="review-content">'+vo.reviewContent+'</p> ';
	str +=' 		<span class="tag">#'+vo.emoName+'</span> ';
	str +=' 		<span class="glyphicon glyphicon-unchecked btn-check" aria-hidden="true" data-reviewno="'+vo.reviewNo+'"></span> ';
	str +=' 	</div> ';
	str +=' </li> ';
	
	$('#reviewAll').append(str);
}

//플리 모달 페이징
function renderPaging(prev, next, startPageBtnNo, endPageBtnNo, crtPage){
	
	var str = '';
	if(prev == true){
		str += ' <li class="page-item"> ';
		str += ' 	<a class="page-link" data-item="prev" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a> ';
		str += ' </li> ';
	}
	
	for(var i = startPageBtnNo; i <= endPageBtnNo; i++){
		/* 현재페이지는 active 버튼데이터(요청한 파라미터) == 받아온 페이지 */
		if(i == crtPage){
			str += ' <li class="page-item"><a class="page-link b-blue" data-crtpage="'+i+'">'+i+'</a></li> ';
		}else{
			str += ' <li class="page-item"><a class="page-link" data-crtpage="'+i+'">'+i+'</a></li> ';
		}
		
	}

	if(next == true){
		str += ' <li class="page-item"> ';
		str += ' 	<a class="page-link" data-item="next" aria-label="Next"><span aria-hidden="true">&raquo;</span></a> ';
		str += ' </li> ';
	}
	
	$('#addModal-pagination').html(str);
	
	
}

//검색 결과 없음
function emptyResult(){
	
	console.log('검색된 서평이 없습니다');
	
	var str = '';
	
	str += ' <li> ';
	str += ' 	<div class="review-card"> ';
	str += ' 		<h3 class="empty-box">검색된 서평이 없습니다 &#128531; </h3> ';
	str += ' 	</div> ';
	str += ' </li> ';
	
	$('#reviewAll').append(str);
	
}

function toggleHeartIcon(reviewNo) {
		$('#btn_like_'+reviewNo).toggleClass('glyphicon-heart-empty')
		$('#btn_like_'+reviewNo).toggleClass('glyphicon-heart')
}

function changeLikeCount(reviewNo) {
	let crtLikeCnt = $('#btn_like_'+reviewNo).next().text() * 1
	
	if($('#btn_like_'+reviewNo).hasClass('glyphicon-heart-empty') == true) {

		$('#btn_like_'+reviewNo).next().text(crtLikeCnt + 1)		
	} else {
		$('#btn_like_'+reviewNo).next().text(crtLikeCnt - 1)
	}
}


//===============[event]==========================================================
//-------------------------------------------------------------
//모달 > 서평추가 체크박스 선택
$('#reviewAll').on('click','.glyphicon-unchecked', function(){
	console.log('서평 check');
	
	$(this).attr('class','glyphicon glyphicon-check btn-check');
})

//모달 > 서평 체크박스 취소
$('#reviewAll').on('click','.glyphicon-check', function(){
	console.log('서평 uncheck');
	
	$(this).attr('class','glyphicon glyphicon-unchecked btn-check')
})


//선택한 서평 해당 플리에 추가등록 (선택한 서평 담기 버튼)
$('.addReviewBtn').on('click', function(){
	
	console.log('선택 등록 버튼 클릭');
	var checkedReviews = $('.glyphicon-check');
	
	// 서평이 중복된다...?
	for(let item of checkedReviews){
		var checkdata = item.dataset;
		reviewChkBoxArr.push(checkdata.reviewno);
	}
	
	addReviewToPly(reviewChkBoxArr);
})


//키워드 검색
$('#reviewSearch').keydown(function(keyNum){
	
	if(keyNum.keyCode == 13){
		
		console.log('엔터!');
 
		searchTxt = $('#reviewSearch').val();
		crtPage = 1
		
		//검색 내용 요청
		getSearch(searchTxt, playlistNo, crtPage);
		
	}
	
})

//플리 좋아요 버튼
$('#playlistLike').on('click','.glyphicon-heart',function(){
	console.log('플레이리스트 좋아요-> 취소 클릭');
	playlistUnlike();
})

$('#playlistLike').on('click','.glyphicon-heart-empty',function(){
	console.log('플레이리스트 좋아요-> 추가 클릭');
	addplaylistLike();
})	

//서평 삭제 버튼
/* 
$('#reviewDelete').on('click', function(){		// 수정: id --> class
	
	console.log('서평 삭제 클릭');
	
	reviewDelete();
})
*/
$('.btn_delete_review').each(function(index, btn_delete){
	
	btn_delete.addEventListener("click", function() {
		
		let reviewNo = btn_delete.id
		
		fetchDeleteReview(reviewNo) // db에서 삭제

	})
})


// 서평 좋아요 기능 (usere_no, review_no)
$('.btn_like').each(function(index, btn_like){
	
	btn_like.addEventListener("click", function() {
	
		let reviewNo = btn_like.id.slice(9)
		let classname = btn_like.classList[2]
		
		toggleLikeReview(reviewNo, classname) // db 통신
		changeLikeCount(reviewNo) // 좋아요 수 변경
		toggleHeartIcon(reviewNo) // 하트 아이콘 변경
	})
})



