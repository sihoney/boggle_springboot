//=====[변수 선언]===============================================================
var loginUserNo = $('#listing').data('userno');//로그인한 유저넘버
var ISBN// = $('#listing').data('bookno');//해당 책 번호

//===========================================================================
//로딩시 최신순으로 리스트 출력
$(document).ready(function(){
	console.log('로딩 성공');
	
	//fetchLatest();
	//bookMark();
	
	ISBN = window.location.pathname.substring(6)
	
	$('#bookmark').on('click', clickBookMark);
});
// 최신순 인기순 버튼 클릭
// 최신순
$('#latest-order').on('click',async function(){
    console.log('최신순 클릭');

    $('#latest-order').attr('class','txt-b');
    $('#best-order').attr('class','');

	//fetchLatest();
	let reviewList = await fetchReviewList(ISBN, 0, "createdAt,desc");
    if (reviewList.length === 0) {
      	emptyReview();
    } else {
        rendering(reviewList);
    }
});

// 인기순
$('#best-order').on('click', async function(){
    console.log('인기순 클릭');

    $('#best-order').attr('class','txt-b');
    $('#latest-order').attr('class','');

	//fetchBest();
	let reviewList = await fetchReviewList(ISBN, 0, "likeCount,desc");
    if (reviewList.length === 0) {
      	emptyReview();
    } else {
        rendering(reviewList);
    }
});

//--------------------------------------------------------------

async function fetchReviewList(isbn, page, sort){
	try {
		let response = await fetch(`/api/book/${isbn}?page=${page}&sort=${sort}`, {
			  method: "GET",
			  headers: {
			    "Content-Type": "application/json"
			  }
			});
			
		let data = await response.json();
		
		return data;		
	} catch(error) {
		console.log("서버 문제")
		emptyReview();
	}
}

//render
function rendering(reviewList){
	
	console.log(reviewList);
	console.log(loginUserNo);

	$("#reviewlistVo").empty();

	reviewList.forEach(item => {
		var str = '';
		str += ' <div class="jumbotron"> ';
		str += ' 	<div id="review_first"> ';
		str += ' 		<h3>'+ item.bookTitle +'</h3> ';
		if(item.userNo == loginUserNo){
			str += ' 			<a id="reviewDelete" class="review_modify" data-reviewno="'+item.reviewNo+'">삭제</a> ';
			str += ' 			<a href="reviews/'+item.reviewNo+'/new" class="review_modify">수정</a> ';
		}
		str += ' 		<a href="'+item.nickname+'" class="review_nick">'+ item.nickname +'<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span></a> ';
		str += ' 		<div class="multiline-ellipsis">'+item.reviewContent+'</div> ';
		str += ' 	</div> ';
		str += ' 	<div id="review_second"> ';
		str += ' 		<span id="btn_like" class="glyphicon glyphicon-heart icon-success" aria-hidden="true"></span> ';
		str += ' 		<span class="review_like">'+item.likecnt+'</span><span class="review_like">'+item.reviewDate+'</span> ';
		str += ' 		<span id="tag_btn">#'+item.emoName+'</span> ';
		str += ' 		<div class="dropup float-r"> ';
		str += ' 			<a id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">+ 더보기</a> ';
		str += ' 			<ul class="dropdown-menu radius-15" role="menu" aria-labelledby="dropdownMenu2"> ';
		str += ' 				<li role="presentation"><a id="add_pli" role="menuitem" tabindex="-1">플레이리스트에 추가<span id="plus">+</span></a></li> ';
		str += ' 				<li role="presentation" class="divider"></li> ';
		str += ' 				<li role="presentation"><a id="shr_review" role="menuitem" tabindex="-1">서평 공유하기<span class="glyphicon glyphicon-share" aria-hidden="true"></span></a></li> ';
		str += ' 				<li role="presentation" class="divider"></li> ';
		str += ' 				<li role="presentation"><li role="presentation"><a id="save_img" role="menuitem" target="_blank" tabindex="-1" href="${pageContext.request.contextPath}/imgpreview">이미지 저장하기<span class="glyphicon glyphicon-save" aria-hidden="true"></span></a></li> ';
		str += ' 			</ul> ';
		str += ' 		</div> ';	
		str += '	</div> ';		
	})

	$('#reviewlistVo').append(str);
}

function emptyReview(){
	
	console.log('등록된 서평이 없습니다');
	
	$("#reviewlistVo").empty();
	
	var str = '';
	
	str += ' <div class="jumbotron"> ';
	str += ' 	<h3>등록된 서평이 없습니다 &#128531; </h3> ';
	str += ' </div> ';
	
	$('#reviewlistVo').append(str);
	
}

//-------------------------------------------------------------
async function clickBookMark() {
	console.log('mark 클릭');
	
	try {
		let response = await fetch(`${window.location.pathname}/likes`, {
			method: 'get'
		});
		
		let data = await response.json();
		
		console.log(data)
		
		if(data == 0) { // 북마크 삭제
			renderAddMark();
		} else { // 북마크 추가
			renderDeleteMark();
		}
	
	} catch(error) {
		console.log(error)
	}
}
//+버튼 그리기
function renderAddMark(){
	
	$("#bookmark").empty();
	
	var str = '<button id="addMark" type="button" class="btn Markbtn">관심가는 책 + </button> ';
	
	$('#bookmark').html(str);
	
}

//-버튼 그리기
function renderDeleteMark(){
	
	$("#bookmark").empty();
	
	var str = '<button id="deleteMark" type="button" class="btn Markbtn">관심가는 책 - </button> ';
	
	$('#bookmark').html(str);
	
}

//------------------------------------------------------------------------
//서평 삭제 요청
function reviewDelete(reviewNo){
	
	console.log('책 상세 서평 삭제 요청');
	console.log(reviewNo);
	
	$.ajax({
		url : "bookdetail/delete",
		type : "post",
		data : {reviewNo: reviewNo},
		dataType : "json",
		success : function(deleteResult){
		/*성공시 처리해야될 코드 작성*/
			
			console.log('책 상세 서평 삭제 성공');
		
			if(deleteResult == 1){
				location.href="bookdetail?bookNo="+ISBN+"&userNo="+loginUserNo;
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

//서평 삭제 요청
$('#reviewlistVo').on('click','#reviewDelete', function(){
	
	console.log('책 상세 서평 삭제 클릭');
	
	var reviewNo = $(this).data('reviewno');
	console.log(reviewNo);
	
	reviewDelete(reviewNo);
	
})




