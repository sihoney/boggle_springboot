//===============[플레이리스트 서평추가 모달]===============================
//변수선언
var CRT_PAGE = 1;
var MODAL_CRT_PAGE = 1;

var CHECK_REVIEW = [];
var SAME_USER;
var AUTH_USER_ID
var QUERY

var PLAYLIST_ID = $('#playlistLike').data('playlistno');
var PLAYLIST_USER_ID = $('#playlistLike').data('userno'); // ply maker's userNo
var NICKNAME = $('#playlistLike').data('nickname');	// ply maker's nickname

//==========================================
//               [페이지 로딩]
//==========================================
$(document).ready(function(){
	console.log('페이지 로딩 성공');
	
	addEvLsToPaging();	// 페이징 이벤트 리스너
	
	addEvToHeart();	// 좋아요 이벤트 리스너
	
	addEvToDeleteBtn();	// 삭제 버튼 이벤트 리스너
})

//플리 좋아요 버튼
$('#playlistLike').on('click', function(){
	
	toggleLikePlaylist(PLAYLIST_ID);
})

// 서평 좋아요 기능 (usere_no, review_no)
function addEvToHeart(){
	$('.btn_like').each(function(index, btn_like){
		
		btn_like.addEventListener("click", function() {
		
			let reviewId = btn_like.id.slice(9)
			
			console.log(reviewId)
					
			toggleLikeReview(reviewId) // db 통신
			toggleHeartIcon(reviewId) // 하트 아이콘 변경
			//changeLikeCount(reviewId) // 좋아요 수 변경
		})
	})	
}

function toggleHeartIcon(reviewNo) {
	$('#btn_like_'+reviewNo).toggleClass('glyphicon-heart-empty')
	$('#btn_like_'+reviewNo).toggleClass('glyphicon-heart')
}

// 페이징 이벤트 리스너
async function addEvLsToPaging(){
	
	$(".page-btn").on('click', async function(e) {
		e.preventDefault()
		
		let page = $(this).text()
			
        try {
            const response = await getReviewList(page);
			SAME_USER = response.result
			AUTH_USER_ID = response.authUserId

			renderMainTag(response)				
			
        } catch (error) {
            console.error('Error:', error);
        }				
	})
}

function addEvLsToReviewList() {

	addEvToHeart();	// 좋아요 이벤트 리스너
	
	addEvToDeleteBtn();	// 삭제 버튼 이벤트 리스너
}

// 삭제 버튼 이벤트 리스너
function addEvToDeleteBtn() {
	$('.btn_delete_review').each(function(index, btn_delete){
		
		btn_delete.addEventListener("click", async function() {
			
			let reviewNo = btn_delete.id
			console.log(reviewNo)
			
			let response = await fetchDeleteReview(reviewNo, CRT_PAGE); // db에서 삭제
			SAME_USER = response.result
			console.log(response)
			
			renderMainTag(response)
		})
	})	
}

//===============[ 모달 ]==========================
/*모달 ready*/
$('#playlist-add').on('click', async function(){

	$('#review-add').modal({backdrop: 'static', keyboard: false});
	$('#review-add').modal('show'); 

	renderModalReviewList(null);
})

/*모달창 닫기*/
$('.modal-close').on('click', function(){
	closeModal();

});

function closeModal(){
	$('#review-add').modal('hide');// 모달 닫기
	$('#reviewAll').empty();// 모달 > 리스트 초기화
	$('#addModal-pagination').empty();
		
	MODAL_CRT_PAGE = 1;
	CHECK_REVIEW = [];
	QUERY='';
}

//모달 > 서평추가 체크박스 선택
$('#reviewAll').on('click','.glyphicon-unchecked', function(){

	$(this).attr('class','glyphicon glyphicon-check btn-check');
})

//모달 > 서평 체크박스 취소
$('#reviewAll').on('click','.glyphicon-check', function(){

	$(this).attr('class','glyphicon glyphicon-unchecked btn-check')
})

// 모달 서평 추가 버튼 이벤트 리스너
$('.addReviewBtn').on('click', async function(){
	
	var checkedReviews = $('.glyphicon-check');

	// 서평이 중복된다...?
	for(let item of checkedReviews){
		CHECK_REVIEW.push(item.dataset.reviewno);
	}

	let response = await addReviewToPly(CHECK_REVIEW);
	SAME_USER = response.result

	renderMainTag(response)
	
	closeModal();
})

async function renderModalReviewList(query) {
	let response = await getModalReviewList(MODAL_CRT_PAGE, query);
	
	let reviewList = response.reviewList;
	let startPage = response.startPage;
	let endPage = response.endPage
	
	//그리기
	renderModalReview(reviewList);
	
	// 페이징 렌더
	renderModalPaging(null, null, startPage, endPage, MODAL_CRT_PAGE);
	addEvLsToModalPaging();
}

// 모달 페이징 이벤트 리스너
function addEvLsToModalPaging(){
	
	$("#addModal-pagination").on("click", 'a', async function(e) {
		e.preventDefault();
		
		MODAL_CRT_PAGE = $(this).text()
		
		try {
			renderModalReviewList(QUERY);
			
		} catch (error) {
            console.error('Error:', error);
        }	
	})
}


//키워드 검색
$('#reviewSearch').keydown(async function(keyNum){
	
	if(keyNum.keyCode == 13){
		
		console.log('엔터!');
 		
		MODAL_CRT_PAGE = 1
		
		QUERY = $('#reviewSearch').val();
		if(QUERY == '') QUERY = null;
			
		renderModalReviewList(QUERY);
	}
})

/*
function changeLikeCount(reviewNo) {
	let crtLikeCnt = $('#btn_like_'+reviewNo).next().text() * 1
	
	if($('#btn_like_'+reviewNo).hasClass('glyphicon-heart-empty') == true) {
		$('#btn_like_'+reviewNo).next().text(crtLikeCnt + 1)		
	} else {
		$('#btn_like_'+reviewNo).next().text(crtLikeCnt - 1)
	}
}*/

function renderMainTag(response) {
	renderReviewList(response.reviewList);
	addEvLsToReviewList();
	
	renderPaging(response.startPage, response.endPage);
	addEvLsToPaging();
}

//========================================
//               [데이터 요청]
//=========================================
// 플리 좋아요 & 취소
async function toggleLikePlaylist(playlistId) {
    try {
        const response = await fetch(`/playlists/${playlistId}/likes`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ playlistId }),
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        
        if (data === 0) {
            $('#likeview').attr('class', 'glyphicon glyphicon-heart-empty');
        } else {
            $('#likeview').attr('class', 'glyphicon glyphicon-heart');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// 서평 좋아요 & 취소
async function toggleLikeReview(reviewNo) {
    try {
        const response = await fetch(`/reviews/${reviewNo}/likes`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
        });

    } catch (error) {
        console.error('Error:', error);
    }
}

async function getReviewList(page) {
    try {
		page -= 1;
	
        const response = await fetch(`/playlists/${PLAYLIST_ID}/reviews?page=${page}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json', // JSON 응답 기대
            },
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

// 서평 삭제
async function fetchDeleteReview(reviewNo, crtPage) {
    try {
        const response = await fetch(`/playlists/${PLAYLIST_ID}/reviews/${reviewNo}?page=${crtPage}`, {
            method: 'delete',
        });

        const data = await response.json();
        console.log('삭제 완료');
	
		return data;
    } catch (error) {
        console.error('Error:', error);
    }
}


//=======================[ 모달 ]==========================
async function getModalReviewList(crtPage, query) {
  try {
		crtPage -= 1;
		
		let url = `/reviews_modal?page=${crtPage}`;
	
		if(query !== null && query !== undefined) {
			url += `&query=${query}`;
		}
	
	    const response = await fetch(url, {
	    	method: 'GET',
	      	headers: {
        		'Accept': 'application/json', // JSON 응답을 예상합니다.
	      	},
	    });
	
	    if (!response.ok) {
	      	throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
	    }
	
	    const data = await response.json();

	    return data;
  } catch (error) {
    console.error(error);
  }
}
/*
//검색 내용 요청
async function getSearch(query, crtPage) {
	try {
		crtPage -= 1;
		
		  const response = await fetch(`/reviews_modal?query=${query}&page=${crtPage}`, {
			    method: "GET",
			  })
		
	    if (!response.ok) {
	      	throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
	    }
	
	    const responseData = await response.json();

		return responseData;		
	} catch (error) {
		console.error('에러 발생: ', error);
	}
}
*/
async function addReviewToPly(reviews) {
 	try {
	    const response = await fetch(`/playlistId/${PLAYLIST_ID}/reviews`, {
	      	method: "POST",
	      	headers: {
	        	"Content-Type": "application/x-www-form-urlencoded",
	      	},
	      	body: new URLSearchParams({
	        	checkedReview: reviews
	      	}),
	    });
	
	    if (!response.ok) {
	      	throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
	    }
	
	    const responseData = await response.json();
	
	    // 성공시 처리해야 할 코드 작성
	    console.log("데이터 추가 성공");
	
	    RESULT = responseData.result;
	
	    return responseData;

  	} catch (error) {
    	console.error(error);
  	}
}

//========================================
//               [화면 출력]
//========================================
async function renderReviewList(list) {
	
	$("#jumbo-wrap").empty();
	//$(".jumbotron").remove();
	
	try {
		for(var i = 0; i <= list.length; i++) {
			var item = list[i];
			/*var bookEntity = await getBookEntity(item)*/
			
			var str = `<!-- 서평 리스트 vo-->
				<div class="jumbotron">
					<div id="reviewVo-wrap">
						<div id="review_first">`;
	
			str += 	`<h3>
						<a href="/bookdetail?bookNo=${item.bookEntity.isbn13}&userId=${item.userId}">${item.bookEntity.title}</a>
					</h3>`;			
	
			/*if(SAME_USER == "sameUser") {*/
			if(PLAYLIST_USER_ID == AUTH_USER_ID) {
				str +=`<!-- 자기글에만 수정 삭제 노출 -->
					<a id="${item.reviewId }" class="review_modify btn_delete_review" data-reviewId="${item.reviewId}">삭제</a>
					<!-- 자기글에만 수정 삭제 노출 -->`;
					
				/*<a href="/review/write?reviewId=${item.reviewId}" class="review_modify">수정</a>*/			
			}				
		
			str +=`<a href="/${item.nickname}/mybook" class="review_nick">
								${item.nickname}
								<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
							</a>
		
							<div class="multiline-ellipsis">${item.content}</div>
						</div>
		
						<div id="review_second">
							<!-- 하트 아이콘 -->`;
							
			if(item.likeByAuthUser == true) {
				str += `<!-- 좋아요 활성화 -->
						<span id="btn_like_${item.reviewId }" class="btn_like glyphicon glyphicon-heart icon-success" data-reviewId="${item.reviewId }" aria-hidden="true"></span>
						`;
			} else {
				str += `<!-- 좋아요 비활성화 -->
						<span id="btn_like_${item.reviewId }" class="btn_like glyphicon glyphicon-heart-empty icon-success" data-reviewId="${item.reviewId }" aria-hidden="true"></span> 																							
						`;
			}		
	
			str += `		<!-- 하트 아이콘 -->
		
							<span class="review_like">${item.createdAt}</span> 
							<span id="tag_btn">#${item.emotionEntity.emotionName}</span>
							
							<!-- 더보기 클릭시 모달창 오픈 -->
							<!-- <button type="button" class="btn btn-default btn-sm">+더보기</button> -->
		
							<div class="dropup float-r">
								<!-- <span class="caret"></span> -->
								<a id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> + 더보기</a>
								<ul class="dropdown-menu radius-15" role="menu" aria-labelledby="dropdownMenu2">
									<li role="presentation">
										<a role="menuitem" tabindex="-1" href="#">플레이리스트에 추가<span id="plus">+</span></a>
									</li>
									<li role="presentation" class="divider"></li>
									<li role="presentation">
										<a id="shr_review" role="menuitem" tabindex="-1" href="#">
											서평 공유하기
											<span class="glyphicon glyphicon-share" aria-hidden="true"></span>
										</a>
									</li>
									<li role="presentation" class="divider"></li>
									<li role="presentation">
										<a role="menuitem" tabindex="-1" href="#">
											이미지 미리보기
											<span class="glyphicon glyphicon-save" aria-hidden="true"></span>
										</a>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>`;
	
			$("#jumbo-wrap").append(str);
		}		
	} catch(error) {
		console.error('Error while getting bookEntity:', error);
	}
	
	
}

function renderPaging(startPage, endPage){
	
	$(".pagination").empty();
	
	let str = '';
	
	for(let i = startPage; i <= endPage; i++) {
		str += `<li class="page-btn">
					<a>${i}</a>
				</li>`;
	}
	
	$(".pagination").append(str);
}

//===============[ 플레이리스트 모달 ]==========================
//해당 페이지 서평 리스트
function renderModalReview(list){
	
	$('#reviewAll').empty();
	
	if(list.length == 0) {
		emptyResult();
	} else {
		for(var i = 0; i < list.length; i++){
			let item = list[i];
			
			//그리기
			var str = '';
			str +='	<li> ';
			str +='		<div class="review-card"> ';
			str +=' 		<p class="bookname">'+item.bookEntity.title+'</p> ';
			str +=' 		<p class="review-content">'+item.content+'</p> ';
			str +=' 		<span class="tag">#'+item.emotionEntity.emotionName+'</span> ';
			str +=' 		<span class="glyphicon glyphicon-unchecked btn-check" aria-hidden="true" data-reviewno="'+item.reviewId+'"></span> ';
			str +=' 	</div> ';
			str +=' </li> ';
			
			$('#reviewAll').append(str);		
		}
	}
}

//플리 모달 페이징
function renderModalPaging(prev, next, startPageBtnNo, endPageBtnNo, crtPage){
	
	$('#addModal-pagination').empty();
	
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
