<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>playlist-like</title>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/all_css.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-book.css">
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.12.4.js"></script>
	<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
</head>
<body>
	<div id="wrap">
		<!-- 헤더 -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
		
		<!-- ------nav------ -->
		<c:import url="/WEB-INF/views/include/nav.jsp">
			<c:param name="path" value="taste" />
		</c:import>
		
		<!-- ------nav2------ -->
		<ul id="nav2" class="nav nav-pills">
			<!-- 세션아이디와 비교, 다를경우 '이름님의 취향' -->
			<!-- <li role="presentation" class="active"><a href="">'유저이름'님의 취향</a></li> -->
			<li role="presentation"><a
				href="${pageContext.request.contextPath}/${nickname}/tastemain">my
					취향</a></li>
			<li role="presentation"><a
				href="${pageContext.request.contextPath}/${nickname}/tastereview">좋아요한
					서평</a></li>
			<li role="presentation" class="active"><a
				href="${pageContext.request.contextPath}/${nickname}/main_book">관심가는
					책</a></li>
		</ul>
	</div>
	<!-- ------nav------ -->
	<!-- ------nav2------ -->
	<!--tag nav-->

	<!--/tag nav-->
	<%-- 		<c:forEach items="${bList}" var="bList">
 --%>
	<div class="container">
<%-- 		<div class="minicontent">
			<c:choose>
				<c:when test="${result eq 'sameUser'}">
					<p>'${nickname}'님이 북마크한 책</p>
				</c:when>
				<c:otherwise>
					<p>'${otherUser.nickname}'님이 북마크한 책</p>
				</c:otherwise>
			</c:choose>
		</div> --%>
			<div class="gradient">
				<div class="gallery">
						<c:forEach items="${bmList}" var="vo">
					<article>
						<div class="img">
							<a class="a" href="${pageContext.request.contextPath}/bookdetail?bookNo=${vo.bookNo}&userNo=${vo.userNo}"> <img class="cover" src="${vo.cover_url}" alt="image" />
							</a>
						</div>
						<div class="book-detail">
							<p>${vo.book_title}</p>
							<p>${vo.author}</p>
						</div>
					</article>
					</c:forEach>


				</div>
				<!-- gallery -->
			</div>
			<!-- gradient -->
			<!--gallery-->
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		
	</div>
	<%-- 		</c:forEach>
 --%>
	<!-- footer -->
</body>
</html>