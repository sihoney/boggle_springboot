<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 

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
	<script src="${pageContext.request.contextPath}/resources/static/js/more.js" defer></script>
	<script src="${pageContext.request.contextPath}/resources/static/js/mybook.js" defer></script>
</head>
<body>
	<div id="wrap">
		<!-- 헤더 -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
		
		<c:import url="/WEB-INF/views/include/nav.jsp">
			<c:param name="path" value="mybook" />
		</c:import>
		
		<sec:authentication var="auth" property="principal" />
		
		<!--content-->
		<div class="container">
			<div class="row">
				<!-- col-xs-8 -->
				<div id="content" class="col-xs-8">			
					<c:if test="${auth.nickname eq nickname}">
						<!--기록하기 박스--> <!-- 작성자아이디와 세션아이디가 동일할 시에만 보이게 -->
						<div id="writebox" class="jumbotron">
							<h1>서평 기록하기</h1>
							<p>'${auth.nickname}'님, 오늘은 어떤 책을 읽으셨나요?</p>
							<p>
								<a class="btn btn-primary btn-md" href="${pageContext.request.contextPath}/reviews/new" role="button">기록하기</a>
							</p>
						</div>
						<!--기록하기 박스-->
					</c:if>
					
					<!-- list -->
					<div id="list">
						<ul>
							<li><a><span id="latest-order">최신순</span></a></li>
							<li><a><span id="best-order">인기순</span></a></li>
						</ul>
						<span class="glyphicon glyphicon-filter" aria-hidden="true"></span>
						<div id="category" class="dropdown">
							<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
								감정태그 
								<span class="caret"></span>
							</button>
							<ul id="dropdown-menu-emotion" class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
								<c:forEach items="${emotionList }" var="emotionEnum">
									<li role="presentation"><a role="menuitem" tabindex="-1">${emotionEnum}</a></li>
								</c:forEach>
								<!-- <li role="presentation"><a role="menuitem" tabindex="-1">외로운</a></li>-->
							</ul>
						</div>
					</div>
					
					<!-- 서평 리스트 -->
					<div id="rvlist"> 
					 	<c:forEach items="${reviewList}" var="reviewObj">
					 	
					 		<div class="reviews" id="r${reviewObj.reviewId }">
					 			<div class="reviews-header">
					 				<div class="left">
					 					<p><a href="${pageContext.request.contextPath}/bookdetail?bookNo=${reviewObj.bookEntity.isbn } &userNo=${reviewObj.userId}">${reviewObj.bookEntity.bookName }</a></p>
					 				</div>
					 				<c:if test="${auth.nickname eq nickname}">
						 				<div class="right">
						 					<a href="${pageContext.request.contextPath}/reviews/edit?reviewId=${reviewObj.reviewId}">수정</a> 
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
			  									<li role="presentation"><a role="menuitem" tabindex="-1" target="_blank" href="${pageContext.request.contextPath}/reviews/${reviewObj.reviewId}">이미지 미리보기<span class="glyphicon glyphicon-save" aria-hidden="true"></span></a></li> 
  											</ul>		
					 					</div>
					 				</div>
					 			</div>
					 		</div>
					 		
					 	</c:forEach>
					</div>
					<!-- /서평 리스트/ -->
					
					<div id="mybook_paging">
						<ul>
	  					<c:forEach begin="${requestScope.startPage}" end="${requestScope.endPage}" step="1" var="i">
									<li class="page-btn">
										<!-- ${pageContext.request.contextPath}/playlist/folder?playlistNo=${foldermainMap.playlistCover.playlistNo}&userId=${foldermainMap.playlistCover.userId}&nickname=${param.nickname}&crtPage=${i} -->
										<a>${i}</a>
									</li>
						</c:forEach>	
						</ul>				
					</div>
					
					<!-- paging -->
					<!--<div id="mybook_paging">
						<ul>
							<li>◀</li>
							<c:forEach items="${pagination }" var="page">
								<li>${page }</li>
							</c:forEach>
							<li>▶</li>
						</ul>
					</div>  -->					
				</div>
				<!-- //col-xs-8 -->
				
				<!-- col-xs-4:프로필 -->
				<div id="aside" class="col-xs-4">
					<div id="profile-box" class="panel panel-default">
						<div class="panel-heading">
						
							<c:choose>
								<c:when test="${auth.nickname eq nickname}">
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
</html>