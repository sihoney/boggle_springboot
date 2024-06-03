let USER_ID
let SORT
let PAGE
let EMOTION_NAME
let isSameUser
	
let projectName
let nickname
let reviewNo
let crtPage
let emoName
	
/************************************************ 
EVENT LISTENER 
*************************************************/

//리스트(로딩되기전에 요청)
$(document).ready(function() {
	
	//SORT = $("#rvlist").data("sort")
	//SORT = "createdAt:DESC"
	SORT= "createdAt,desc"
	USER_ID = $("#username").data("userid")
	isSameUser = $('#profile-title').text() === '내 서재'

	//색깔변화
	$('#latest-order').attr('class','txt-b');
	$('#best-order').attr('class','');
});
	
// 페이징
$("#mybook_paging").on("click", "li", pagingHandler)
	
// 최신순
$('#latest-order').on('click', async function() {
	
	let name = $(this).text()
	
	//색깔변화
	$('#best-order').attr('class','');
	$('#latest-order').attr('class','txt-b');
	
	let direction
	if(name === '최신순') {
		$(this).text('오래된순')
		direction = 'asc'
	} else {
		$(this).text('최신순')
		direction = 'desc'
	}
	
	PAGE = 0
	EMOTION_NAME = null
	
	reviewsAndPaging(PAGE, "createdAt," + direction, EMOTION_NAME)
});

// 인기순
$('#best-order').on('click', function() {
	console.log('인기순');
	
	//색깔변화
	$('#best-order').attr('class','txt-b');
	$('#latest-order').attr('class','');

	PAGE = 0
	SORT = "likeCount,desc";
	EMOTION_NAME = null
	
	reviewsAndPaging(PAGE, SORT, EMOTION_NAME)
});
	
	// 감정태그
	$("#dropdown-menu-emotion").on("click", "li", async function() {

		//색깔변화
		$('#latest-order').attr('class', '');
		$('#best-order').attr('class', '');

		//데이터수집
		EMOTION_NAME = $(this).text();
		PAGE = 1
		
		reviewsAndPaging(PAGE, "createdAt,desc", EMOTION_NAME)
	});

//좋아요 버튼을 클릭했을때(이벤트)
$("#rvlist").on("click", ".like", function() {
	
	//데이터수집
	var $this = $(this);
	var reviewId = $this.data("reviewid");
	var likeCnt = $this.next().data("likecount");
	
	postReviewUser(reviewId).then(response => {
		console.log(response)
		
		if($this.hasClass("glyphicon-heart")) {
			//하트모양변경
	        $this.attr('class','like glyphicon glyphicon-heart-empty');
	
	        //카운트 -1
	        $this.next().html(likeCnt - 1);
	        $this.next().data("likecount", likeCnt - 1)
		} else {
			//하트모양변경
	      	$this.attr('class', 'like glyphicon glyphicon-heart');
			
		    //카운트 +1
		    $this.next().html(likeCnt + 1);
		    $this.next().data("likecount", likeCnt + 1)
		}		
	})
});
	
	//삭제 버튼을 눌렀을때
	$("#rvlist").on("click", ".delete", function() {

		//데이터수집
		var $this = $(this);
		var reviewId = $this.data("reviewid");
		
		//출력
		console.log("삭제하려는서평 : "+reviewId);
		
		// 서평 삭제
		deleteReview(reviewId).then(response => {
			console.log(response)
			
			//삭제알림  
			alert('서평이 삭제되었습니다! :-)');
		
			//해당 서평 삭제(화면변화)
			//$("#r"+no).remove();

		}).then(() => {
			//색깔변화
			$('#best-order').attr('class','txt-b');
	    	$('#latest-order').attr('class','');
	    	
			reviewsAndPaging(1, "createdAt,desc", null)
		}).catch(errer => {
			console.log(error)
		})
	
		/*
		.catch(error => {
			console.log(error)
			
			//삭제실패알림  
			alert('잘못된 접근입니다! :-/');
		})
		*/
	});	
	
	/*
	// modal_add_playlist - closeBtn 
	$(".modal_myply_btn").on("click", function(){
		
		$(".modal_myply").removeClass("opaque")
		$(".modal_myply").one("transitionend", function(){
			$(".modal_myply").addClass("unstaged")
		})
	})
	*/
	
/***************************************************************
EVENT HANDLER
****************************************************************/	
async function pagingHandler(){
	
	PAGE = $(this).text() 
	
/*	let sort_arr = SORT.split(':');
	let property = sort_arr[0]
	let direction = sort_arr[1]
	direction = direction.substring(1, direction.length)*/
	
	reviewsAndPaging(PAGE, SORT, EMOTION_NAME)
}
	/*
	async function showMoreEventHandler(){
		// 0. 초기화
		$(".modal_ply_ul").empty()		
		
		reviewNo = $(this).data("reviewno")
		
		// 내 플리 불러오기
		fetchMyPli(reviewNo).then(data => {
			
			// session 정보가 만료됐을 때
			if(data === null) {
				location.href = projectname + "/main"
			}
			
			// 1-1. 플리 렌더
			renderMyPli(data)
			
			// 1-2. 이벤트 리스터 (서평 플리에 추가)
			$(".modal_ply_ul").on("click", ".list", addReviewToPliHandler)			
		})
		
		// 모달 보임 
		$(".modal_myply").removeClass("unstaged")
		$(".modal_myply").addClass("opaque")		
	}
	
	function addReviewToPliHandler(){
		
		// 1> 플리에 서평 저장
		let playlistno = $(this).data("playlistno")
		
		// 플리에서 서평 추가
		if($(this).hasClass("selected") === false) { 
			addReviewToPly(playlistno, reviewNo).then(result => {
				console.log("결과: " + result + ", 플리에 저장했습니다.")					
			})
		}
		else {	// 플리에서 서평 삭제
			removeReviewAtPly(playlistno, reviewNo).then(result => {
				console.log("결과: " + result + ", 플리에서 삭제했습니다.")					
			})
		}
		
		// 2> css 변화
		$(this).toggleClass("selected")			
	}
	*/
/************************************************ 
UTIL 
*************************************************/
async function reviewsAndPaging(page, sort, emotionName) {
	
	const data = await getReviewsAndPaging(page, sort, emotionName)
	console.log(data)
	
	let startPage = data.startPage
	let endPage = data.endPage
	let reviewList = data.reviewList
	SORT = data.sort

	let sort_arr = SORT.split(':');
	let property = sort_arr[0]
	let direction = sort_arr[1]
	direction = direction.substring(1, direction.length)
	SORT = property + "," + direction

	//초기화 
	$("#rvlist").empty();
	$("#mybook_paging").remove();
	
	//렌더
	renderList(reviewList, "down");
	renderPaging(null, null, startPage, endPage)
	
	//이벤트 리스너
	$("#mybook_paging").on("click", "li", pagingHandler)
}

/**************** 
FETCH 
*******************/
async function getReviewsAndPaging(page, sort, emotionName){
	try {
	
		let path = `/api/reviews?userId=${USER_ID}&emotionName=${emotionName}&page=${page}&sort=${sort}`
		//let queryString = "page=" + page + "&sort=" + sort + "&emotionName=" + emotionName
	
		const response = await fetch(path)
		
		return response.json()			
	} catch(error) {
		console.log("Failed to fetch: " + error)
		
		location.href = '/user/loginForm'
		
		return "error"
	}
}
	
// 서평 좋아요
async function postReviewUser(reviewid){
	try {
		console.log(reviewid)
		
		let obj = {
				reviewId : reviewid
			}
		
		const response = await fetch("/reviewUser", {
				method: "POST",
				headers : {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(obj)
			})
		return response;
		//return response.json()			
	} catch(error) {
		console.log("Failed to fetch: " + error)
		
		return "error"
	}
}
	
	// 서평 삭제
	async function deleteReview(reviewId) {
		try {

			const response = await fetch("/review/" + reviewId, {
				method: 'DELETE'
			})
			
			return response	
		} catch(error) {
			console.log("Failded to fetch: " + error)
			
			return "error"
		}
	}	
	
	// 플리 목록
	async function fetchMyPli(reviewNo) {
		try {
			const response = await fetch(projectName + "/getplaylist?reviewno=" + reviewNo)
			
			return response.json()
		} catch(error) {
			console.log("Failed to fetch: "+ error)
			
			return "error"
		}
	}
	
	// 플리 목록 불러오기
	/*
	async function getMyPly(userNo) {
		try {
			const response = await fetch(projectName + "/review/getMyPlaylist?userNo=" + userNo)
			
			return response.json()			
		} catch(error) {
			console.log("Failed to fetch: " + error)
			
			return "error"
		}
	}
	*/
	
	// 플리에 서평 추가
	async function addReviewToPly(playlistNo, reviewNo) {
		const response = await fetch(projectName + "/review/addReviewToPly?playlistNo=" + playlistNo + "&reviewNo=" + reviewNo)
		
		return response.json()
	}
	
	// 플리에 서평 삭제
	async function removeReviewAtPly(playlistNo, reviewNo) {
		const response = await fetch(projectName + "/review/removeReviewAtPly?playlistNo=" + playlistNo + "&reviewNo=" + reviewNo)
		
		return response.json()
	}	
	
	/*
	function addReviewToPly(playlistNo, reviewNo) {
		$.ajax({
			url: "${pageContext.request.contextPath}/review/addReviewToPly?playlistNo=" + playlistNo + "&reviewNo=" + reviewNo,
			method: "post",
			dataType: "json",
			success: function(){
				console.log("플리에 저장했습니다.")

				$(".msg_modal").removeClass("unstaged")
				$(".msg_modal").addClass("opaque")
				
				setTimeout(function(){
					$(".msg_modal").removeClass("opaque")
					
					$(".msg_modal").one("transitionend", function(){
						$(".msg_modal").addClass("unstaged")
					})
				}, 2000)
				

			},
			error:  function(XHR, status, error){
				console.log(status + " : " + error);
			}
		})
	}	
	*/
	/**************** 
	RENDER 
	*******************/	
	
	//페이징 렌더
	function renderPaging(prev, next, startPageBtnNo, endPageBtnNo) {
		
		let str = '<div id="mybook_paging">'
			str += '<ul>'
			
/* 			if(prev == true) {
				str += '<li>◀</li>'
			} */
			str += '<li>◀</li>'
			for(var i = startPageBtnNo; i <= endPageBtnNo; i++) {
				str += '<li>'+ i +'</li>'
			}
			str += '<li>▶</li>'			
/* 			if(next == true) {
				str += '<li>▶</li>'
			} */

			str += '</ul>'
			str += '</div>'		 	
		
		$("#content").append(str)
	}					
	 
	function renderList(list, updown) {
		
		list.forEach(mybookVo => {
			var str = '';
			str += '	<div class="reviews" id=r'+mybookVo.reviewId+'> ';
			str += '		<div class="reviews-header"> ';
			str += ' 		<div class="left"> ';
			str += ' 			<p><a href="${pageContext.request.contextPath}/bookdetail?bookNo='+ mybookVo.bookId + '&userNo='+mybookVo.userId+'">' + mybookVo.bookEntity.title + '</a></p> ';
			str += ' 		</div> ';
			
			if(isSameUser === true) {
				str += ' 		<div class="right"> ';
				str += ' 			<a href="${pageContext.request.contextPath}/review/write?reviewNo='+mybookVo.reviewId+'">수정</a> <a class="delete" data-reviewid="'+mybookVo.reviewId+'">삭제</a> ';
				str += ' 		</div> ';
			}
			str += ' 	</div> ';
			str += ' 	<div class="reviews-content"> ';
			str += ' 		<p>' + mybookVo.content + '</p> ';
			str += ' 		<span class="label label-default">'+mybookVo.emotionEntity.emotionName+'</span> ';
			str += ' 	</div> ';
			str += ' 	<div class="reviews-footer"> ';
			str += ' 		<div class="left likecontrol"> ';
			
  			if(mybookVo.likeByAuthUser){
  				str += ' <span id="heart" data-reviewid="'+mybookVo.reviewId+'" class="like glyphicon glyphicon-heart" aria-hidden="true"></span> <span class="likecnt" data-likecount="'+mybookVo.likeCount+'">'+ mybookVo.likeCount+ '</span> <span>'+ mybookVo.createdAt+ '</span> ';
	        } else {
        		str += ' <span id="heart" data-reviewid="' +mybookVo.reviewId+'" class="like glyphicon glyphicon-heart-empty" aria-hidden="true"></span> <span class="likecnt" data-likecount="'+mybookVo.likeCount+'">'+ mybookVo.likeCount+ '</span> <span>'+ mybookVo.createdAt+ '</span> ';
 		    }	
			str += ' 		</div> ';
			str += ' 		<div class="right"> ';
			str += ' 			<div class="dropup"> ';
			str += ' 				<a id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> 더보기 <span class="caret"></span></a> ';
			str += ' 				<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu2"> ';
			
			if(mybookVo.likeByAuthUser){
				str += ' 					<li role="presentation"><a class="add_pli" data-userno="'+ mybookVo.userId +'" data-reviewno="'+ mybookVo.reviewId +'" id="add_pli" role="menuitem" tabindex="-1">플레이리스트에 추가<span id="plus">+</span></a></li> ';
				str += ' 					<li role="presentation" class="divider"></li> ';
			}
			str += ' 					<li role="presentation"><a id="shr_review" role="menuitem" tabindex="-1">서평 공유하기<span class="glyphicon glyphicon-share" aria-hidden="true"></span></a></li> ';
			str += ' 					<li role="presentation" class="divider"></li> ';
			str += ' 					<li role="presentation"><a role="menuitem" tabindex="-1" target="_blank" href="${pageContext.request.contextPath}/reviews/'+mybookVo.reviewId+'">이미지 미리보기<span class="glyphicon glyphicon-save" aria-hidden="true"></span></a></li> ';
			str += ' 				</ul> ';
			str += ' 			</div> ';
			str += ' 		</div> ';
			str += ' 	</div> ';
			str += ' </div> ';
			
			$("#rvlist").append(str);
			/*
 			if (updown == 'down') {
				$("#rvlist").append(str);
			} else if (updown == 'up') {
				$("#rvlist").append(str);
			} else {
				console.log("방향오류");
			} 
			*/			
		})

	}
	
	function renderMyPli(list) {

		for(let item of list) {
			str = ""

			if(item.likeuser == 0) {
				str += '<li class="list" data-playlistNo="'+ item.playlistNo +'">'
			}
			else {
				str += '<li class="list selected" data-playlistNo="'+ item.playlistNo +'">'
			}

			str += '	<div class="info-container">'
			str += '		<button class="tagBtn">'+ item.emoName +'</button>'
			str += '		<div class="playlist-title">' + item.playlistName + '</div>'
			str += '		<div class="username">' + item.nickname + '</div>'
			str += '	</div>'
			str += '</li>'
			
			$(".modal_ply_ul").append(str)
		}
	}
	/*
	function renderMyPly(list) {

		for(let item of list) {
			str = ""
			str += '<li class="list" data-playlistNo="'+ item.playlistNo +'">'
			str += '	<div class="info-container">'
			str += '		<button class="tagBtn">'+ item.emoName +'</button>'
			str += '		<div class="playlist-title">' + item.playlistName + '</div>'
			str += '		<div class="username">' + item.nickname + '</div>'
			str += '	</div>'
			str += '</li>'
			
			$(".modal_ply_ul").append(str)
		}
		
		$(".modal_ply_ul").on("click", ".list", function(){
			let playlistno = $(this).data("playlistno")

			addReviewToPly(playlistno, reviewNo)
		})		
	}	
	*/