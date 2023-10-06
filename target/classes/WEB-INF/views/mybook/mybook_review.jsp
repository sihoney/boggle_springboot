<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title></title>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/all_css.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/mybook_review.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/modal.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/source.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/write.css">
	
	<script src="${pageContext.request.contextPath}/resources/static/js/jquery-1.12.4.js"></script>
	<script src="${pageContext.request.contextPath}/resources/static/bootstrap/js/bootstrap.js"></script>
	
</head>
<body>
	<div id="wrap">
		<!-- 헤더 -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
		<!-- ------nav------ -->
		<div id="nav" class="clearfix">
			<c:choose>
				<c:when test="${result eq 'sameUser'}">
					<ul class="nav nav-tabs">
						<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${nickname}/mybook">내 서평</a></li>
						<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/tastemain">취향저격</a></li>
						<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/playlist">플레이리스트</a></li>
						<!--세션 아이디와 사이트아이디 같을때
						<li role="presentation"><a href="${pageContext.request.contextPath}/analyze">통계</a></li>
						-->
					</ul>
				</c:when>
				<c:otherwise>
					<!-- 세션아이디랑 다를때는 사이트주소의 아이디와 같은 유저의 데이터들 불러오기-->
					<ul class="nav nav-tabs">
						<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${nickname}/mybook">남 서평</a></li>
						<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/tastemain">취향저격</a></li>
						<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/playlist">플레이리스트</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
		<!-- ------nav------ -->
		
		<!--content-->
		<div class="container">
			<div class="row">
				<!-- col-xs-8 -->
				<div id="content" class="col-xs-8">
					<!--기록하기 박스-->
					<!-- 작성자아이디와 세션아이디가 동일할 시에만 보이게 -->
					<%-- <c:if test="${result eq 'sameUser'}"> --%>
						<div id="writebox" class="jumbotron">
							<h1>서평 기록하기</h1>
							<p>'${authUser.nickname}'님, 오늘은 어떤 책을 읽으셨나요?</p>
							<p>
								<a class="btn btn-primary btn-md" href="${pageContext.request.contextPath}/write" role="button">기록하기</a>
							</p>
						</div>
					<%-- </c:if> --%>
					<!-- list -->
					<div id="list">
						<ul>
							<li><a><span id="latest-order">최신순</span></a></li>
							<li><a><span id="best-order">인기순</span></a></li>
						</ul>
						<span class="glyphicon glyphicon-filter" aria-hidden="true"></span>
						<div id="category" class="dropdown">
							<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
								감정태그 <span class="caret"></span>
							</button>
							<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
							
								<c:forEach items="${emotionList }" var="emotionEnum">
									<li role="presentation"><a role="menuitem" tabindex="-1">${emotionEnum}</a></li>
								</c:forEach>
								<!--  
								<li role="presentation"><a role="menuitem" tabindex="-1">외로운</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">심심한</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">슬픈</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">충만한</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">편안한</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">즐거운</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">희망찬</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">황홀한</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">용감한</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">불안한</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">무력한</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">화난</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">상처입은</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1">회의적인</a></li>
								-->
							</ul>
						</div>
					</div>
					
					<!-- 서평 리스트 -->
					<div id="rvlist" data-sort="${sort }">
					 	<c:forEach items="${reviewList}" var="reviewObj">
					 	
					 		<div class="reviews" id="r${reviewObj.reviewId }">
					 			<div class="reviews-header">
					 				<div class="left">
					 					<p><a href="${pageContext.request.contextPath}/bookdetail?bookNo=${reviewObj.bookEntity.isbn } &userNo=${reviewObj.userId}">${reviewObj.bookEntity.bookName }</a></p>
					 				</div>
					 				<c:if test="${result eq 'sameUser'}">
						 				<div class="right">
						 					<a href="${pageContext.request.contextPath}/review/write?reviewNo=${reviewObj.reviewId}">수정</a> 
						 					<a class="delete" data-reviewid="${reviewObj.reviewId }">삭제</a>
						 				</div>
						 			</c:if>
					 			</div>
					 			<div class="reviews-content">
					 				<p>${reviewObj.content }</p>
					 				<span class="label label-default">${reviewObj.emotionEntity.emotionName }</span>
					 			</div>
					 			<div class="reviews-footer">
					 				<div class="left likecontrol">
					 					<c:choose>
					 						<c:when test="${reviewObj.likeByAuthUser}">
					 							<span id="heart" data-reviewid="${reviewObj.reviewId }" class="like glyphicon glyphicon-heart" aria-hidden="true"></span> 
					 						</c:when>
					 						<c:otherwise>
					 							<span id="heart" data-reviewid="${reviewObj.reviewId }" class="like glyphicon glyphicon-heart-empty" aria-hidden="true"></span>
					 						</c:otherwise>
					 					</c:choose>
					 					<span class="likecnt" data-likecount="${reviewObj.likeCount }">${reviewObj.likeCount }</span>
					 					<span>${reviewObj.createdAt }</span>
					 				</div>
					 				<div class="right">
					 					<div class="dropup">
					 						<a id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> 더보기 <span class="caret"></span></a>
			 								<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu2"> 
			 									<c:if test="${result eq 'sameUser'}">
				  									<li role="presentation"><a class="add_pli" data-userno="${reviewObj.userId }" data-reviewno="${reviewObj.reviewId }" id="add_pli" role="menuitem" tabindex="-1">플레이리스트에 추가<span id="plus"></span></a></li>
				  									<li role="presentation" class="divider"></li>
				  								</c:if>
			  									<li role="presentation"><a id="shr_review" role="menuitem" tabindex="-1">서평 공유하기<span class="glyphicon glyphicon-share" aria-hidden="true"></span></a></li>
			  									<li role="presentation" class="divider"></li>
			  									<li role="presentation"><a role="menuitem" tabindex="-1" target="_blank" href="${pageContext.request.contextPath}/view/${reviewObj.reviewId}">이미지 미리보기<span class="glyphicon glyphicon-save" aria-hidden="true"></span></a></li> 
  											</ul>		
					 					</div>
					 				</div>
					 			</div>
					 		</div>
					 		
					 	</c:forEach>
					</div>
					<!-- /서평 리스트/ -->
					
					
					<!-- paging -->
					<div id="mybook_paging">
						<ul>
							<li>◀</li>
							<c:forEach items="${pagination }" var="page">
								<li>${page }</li>
							</c:forEach>
							<li>▶</li>
						</ul>
					</div>
				</div>
				<!-- //col-xs-8 -->
				
				<!-- col-xs-4:프로필 -->
				<div id="aside" class="col-xs-4">
					<div id="profile-box" class="panel panel-default">
						<div class="panel-heading">
						
							<c:choose>
								<c:when test="${result eq 'sameUser'}">
									<h3 id="profile-title" class="panel-title">내 서재</h3>
								</c:when>
								<c:otherwise>
									<h3 id="profile-title" class="panel-title">${userProfile.nickname}님의서재</h3>
								</c:otherwise>
							</c:choose>
							
						</div>
						<div class="panel-body">
							<div id="profile">
								<img class="img-circle" id="profile-image" src="${pageContext.request.contextPath}/resources/static/images/user_profile/${userProfile.userProfile}" onerror="this.src='${pageContext.request.contextPath}/resources/static/images/profile.png'">
							</div>
							<p id="username" data-userId = "${userProfile.userId }">${userProfile.nickname}</p>
							<p id="level">Lv.0</p>
							<div id="info">
								<a href="${pageContext.request.contextPath}/user/modify">회원정보수정</a> 
								<a href="${pageContext.request.contextPath}/user/logout">로그아웃</a>
							</div>
						</div>
					</div>
				</div>
				<!-- //col-xs-4 -->
			</div>
		</div>
		<!--content-->
		
		
		
		<!-- footer -->
		<%-- <c:import url="/WEB-INF/views/include/footer.jsp"></c:import> --%>
		<!-- modal창 -->
		<c:import url="/WEB-INF/views/include/modal.jsp"></c:import>
				
	</div>
	
	<div class="msg_modal unstaged">
		<p>저장되었습니다</p>
	</div>
	
	<div class="modal_myply unstaged">
		<div class="modal_ply_header">
			<p>My 플레이리스트</p>
			<button class="modal_myply_btn">내 서평 보러가기</button>
		</div>
		<div class="modal_ply_content">
			<ul class="modal_ply_ul">
			</ul>
		</div>
	</div>

</body>
<script type="text/javascript">
	
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
	
	/**************** 
	EVENT LISTENER 
	*******************/

	//리스트(로딩되기전에 요청)
	$(document).ready(function() {
		
		SORT = $("#rvlist").data("sort")
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
		
		PAGE = 1
		EMOTION_NAME = null
		
		reviewsAndPaging(PAGE, "createdAt," + direction, EMOTION_NAME)
	});
	
	// 인기순
	$('#best-order').on('click', function() {
		console.log('인기순');
		
		//색깔변화
		$('#best-order').attr('class','txt-b');
    	$('#latest-order').attr('class','');

    	PAGE = 1
    	EMOTION_NAME = null
    	
    	reviewsAndPaging(PAGE, "likeCount,desc", EMOTION_NAME)
	});
	
	// 감정태그
	$(".dropdown-menu").on("click", "li", async function() {

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

		if($this.hasClass("glyphicon-heart")) {
			//하트모양변경
	        $this.attr('class','like glyphicon glyphicon-heart-empty');

	        //카운트 -1
	        $this.next().html(likeCnt - 1);
	        $this.next().data("likecount", likeCnt - 1)
	        
	        postReviewUser(reviewId).then(response => {})
 		} else {
			//하트모양변경
	      	$this.attr('class', 'like glyphicon glyphicon-heart');
			
		    //카운트 +1
		    $this.next().html(likeCnt + 1);
		    $this.next().data("likecount", likeCnt + 1)
		    
		    postReviewUser(reviewId).then(response => {})
 		}
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
	
	/*********************
	EVENT HANDLER
	**********************/	
	async function pagingHandler(){
		
		PAGE = $(this).text() 
		
		let sort_arr = SORT.split(':');
		let property = sort_arr[0]
		let direction = sort_arr[1]
		direction = direction.substring(1, direction.length)
		
		reviewsAndPaging(PAGE, property + "," + direction, EMOTION_NAME)
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
	/**************** 
	UTIL 
	*******************/
	
	async function reviewsAndPaging(page, sort, emotionName) {
		
		const data = await getReviewsAndPaging(page, sort, emotionName)
		console.log(data)
		
		let startPage = data.startPage
		let endPage = data.endPage
		let reviewList = data.reviewList
		SORT = data.sort

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
		
			let path = "/" + USER_ID + "/reviews"
			let queryString = "page=" + page + "&sort=" + sort + "&emotionName=" + emotionName
		
			const response = await fetch(path + "?" + queryString)
			
			return response.json()			
		} catch(error) {
			console.log("Failed to fetch: " + error)
			
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
			
			return response.json()			
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
			str += ' 					<li role="presentation"><a role="menuitem" tabindex="-1" target="_blank" href="${pageContext.request.contextPath}/view/'+mybookVo.reviewId+'">이미지 미리보기<span class="glyphicon glyphicon-save" aria-hidden="true"></span></a></li> ';
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
	
</script>
<script src="${pageContext.request.contextPath}/resources/static/js/more.js"></script>
</html>