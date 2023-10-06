//=====[변수 선언]===============================================================
//로그인한 유저넘버
var loginUserNo = $('#listing').data('userno');
//해당 책 번호
var bookNo = $('#listing').data('bookno');
//===========================================================================
//로딩시 최신순으로 리스트 출력
$(document).ready(function(){
	console.log('로딩 성공');
	
	fetchLatest();
	bookMark();
	
});
// 최신순 인기순 버튼 클릭
// 최신순
$('#latest-order').on('click',function(){
    console.log('최신순 클릭');

    $('#latest-order').attr('class','txt-b');
    $('#best-order').attr('class','');

	$("#reviewlistVo").empty();
	fetchLatest();

});

// 인기순
$('#best-order').on('click',function(){
    console.log('인기순 클릭');

    $('#best-order').attr('class','txt-b');
    $('#latest-order').attr('class','');

	$("#reviewlistVo").empty();
	fetchBest();

});

//--------------------------------------------------------------
//최신순 데이터 요청
function fetchLatest(){
	
	console.log('최신순 데이터 요청');
	console.log(bookNo);
	
	$('#latest-order').attr('class','txt-b');
	
    $.ajax({
        url :"bookdetail/reviewLatest",
        type : "get",
        contentType : "application/json",
        data : {bookNo: bookNo},

        dataType : "json",
        success : function(latestList){
        /*성공시 처리해야될 코드 작성*/
			console.log(latestList);
			
			if(latestList.length == 0){
				emptyReview();
			}else{
				//객체 리스트
				for(var i = 0; i<latestList.length; i++){
					rendering(latestList[i]);
				}
			}

        },

        error : function(XHR, status, error) {
        console.error(status + " : " + error);
        }
    });

}

//인기순 데이터 요청
function fetchBest(){
	
	console.log('인기순 데이터 요청');
	console.log(bookNo);
	
    $.ajax({
        url :"bookdetail/reviewBest",
        type : "get",
		contentType : "application/json",
        data : {bookNo: bookNo},

        dataType : "json",
        success : function(bestList){
        /*성공시 처리해야될 코드 작성*/
			console.log(bestList);
			
			if(bestList.length == 0){
				emptyReview();
			}else{
				//객체 리스트
				for(var i = 0; i<bestList.length; i++){
					rendering(bestList[i]);
				}
			}

        },

        error : function(XHR, status, error) {
        console.error(status + " : " + error);
        }
    });
	
}

//render
function rendering(reviewList){
	
	console.log(reviewList);
	console.log(loginUserNo);

	var str = '';
	str += ' <div class="jumbotron"> ';
	str += ' 	<div id="review_first"> ';
	str += ' 		<h3>'+ reviewList.bookTitle +'</h3> ';
	if(reviewList.userNo == loginUserNo){
		str += ' 			<a id="reviewDelete" class="review_modify" data-reviewno="'+reviewList.reviewNo+'">삭제</a> ';
		str += ' 			<a href="review/write?reviewNo='+reviewList.reviewNo+'" class="review_modify">수정</a> ';
	}
	str += ' 		<a href="'+reviewList.nickname+'" class="review_nick">'+ reviewList.nickname +'<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span></a> ';
	str += ' 		<div class="multiline-ellipsis">'+reviewList.reviewContent+'</div> ';
	str += ' 	</div> ';
	str += ' 	<div id="review_second"> ';
	str += ' 		<span id="btn_like" class="glyphicon glyphicon-heart icon-success" aria-hidden="true"></span> ';
	str += ' 		<span class="review_like">'+reviewList.likecnt+'</span><span class="review_like">'+reviewList.reviewDate+'</span> ';
	str += ' 		<span id="tag_btn">#'+reviewList.emoName+'</span> ';
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
	
	
	$('#reviewlistVo').append(str);
	
}

function emptyReview(){
	
	console.log('등록된 서평이 없습니다');
	
	var str = '';
	
	str += ' <div class="jumbotron"> ';
	str += ' 	<h3>등록된 서평이 없습니다 &#128531; </h3> ';
	str += ' </div> ';
	
	$('#reviewlistVo').append(str);
	
}

//-------------------------------------------------------------
//로딩시 북마크 이전 데이터 요청
function bookMark(){
	
	var userNo = $('#bookmark').data('userno');
	var bookNo = $('#bookmark').data('bookno');
	
	console.log('로딩시 북마크 체크');
	
	$.ajax({
		url : "bookdetail/checkbookMark",
		type : "post",
		data : {'userNo': userNo,
				'bookNo': bookNo
				},
		
		dataType : "json",
		success : function(checkbookMark){
		/*성공시 처리해야될 코드 작성*/
			console.log('북마크체크'+checkbookMark);

			//객체 리스트 돌리기(화면 출력)
			if(checkbookMark === true){
				addRender();
			}else{
				deleteRender();
			}
		
		},
		error : function(XHR, status, error) {
		console.error(status + " : " + error);
		}
		
	});
}

//북마크 제거
$('#bookmark').on('click','#deleteMark', function(){
    console.log('-mark 클릭');
	var markresult = $('#deleteMark').data('markresult');
	var userNo = $('#bookmark').data('userno');
	var bookNo = $('#bookmark').data('bookno');
	
	console.log(markresult);

	//북마크 제거 요청(delete)
	$.ajax({
		url : "bookdetail/bookmark",
		type : "get",
		contentType : "application/json",
		data : {'markresult': markresult,
				'userNo': userNo,
				'bookNo': bookNo
				},
		
		dataType : "json",
		success : function(deleteResult){
			/*성공시 처리해야될 코드 작성*/
			console.log('성공:'+deleteResult);

			if(deleteResult == false){
				$("#bookmark").empty();
				addRender();
			}
			
		},
		
		error : function(XHR, status, error) {
			console.error(status + " : " + error);
		}
	});
    
});


//북마크 추가
$('#bookmark').on('click','#addMark', function(){
    console.log('+mark 클릭');
	var markresult = $('#addMark').data('markresult');
	var userNo = $('#bookmark').data('userno');
	var bookNo = $('#bookmark').data('bookno');
	
	console.log(markresult);

	//북마크 추가 요청(add)
	$.ajax({
		url : "bookdetail/bookmark",
		type : "get",
		contentType : "application/json",
		data : {'markresult': markresult,
				'userNo': userNo,
				'bookNo': bookNo
				},
		
		dataType : "json",
		success : function(addResult){
			/*성공시 처리해야될 코드 작성*/
			console.log('확인'+addResult);
			console.log(typeof addResult);
			
			if(addResult == false){
				
				$("#bookmark").empty();
				deleteRender();
			}
			
		},
		
		error : function(XHR, status, error) {
			console.error(status + " : " + error);
		}
	});
    
});

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
				location.href="bookdetail?bookNo="+bookNo+"&userNo="+loginUserNo;
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

//------------------------------------------------------------------------
//+버튼 그리기
function addRender(){
	
	var str = '';
	str += ' <button id="addMark" type="button" class="btn Markbtn" data-markresult="true">관심가는 책 + </button> ';
	
	$('#bookmark').html(str);
	
}

//-버튼 그리기
function deleteRender(){
	
	var str = '';
	str += ' <button id="deleteMark" type="button" class="btn Markbtn" data-markresult="false">관심가는 책 - </button> ';
	
	$('#bookmark').html(str);
	
}

//서평 삭제 요청
$('#reviewlistVo').on('click','#reviewDelete', function(){
	
	console.log('책 상세 서평 삭제 클릭');
	
	var reviewNo = $(this).data('reviewno');
	console.log(reviewNo);
	
	reviewDelete(reviewNo);
	
})

//============================================================================



