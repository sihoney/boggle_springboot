<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>taste-main</title>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/all_css.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/playlist-like.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css">
	
	<script src="${pageContext.request.contextPath}/js/jquery-1.12.4.js"></script>
	<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
	<script src="${pageContext.request.contextPath}/js/more.js"></script>
</head>
<!--header-->
<body>
	<div id="wrap">
		<!-- 헤더 -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
		
		<!-- ------nav------ -->
		<c:import url="/WEB-INF/views/include/nav.jsp">
			<c:param name="path" value="playlist" />
		</c:import>
		<!-- ------nav------ -->
		
		<div id="content">
			<!--content4-->
			<div id="content4">
				<div class="minicontent">
					<div class="left">
<%-- 						<c:choose>
							<c:when test="${result eq 'sameUser'}">
								<p>'${nickname}'님이 좋아하는 플레이리스트</p>
							</c:when>
							<c:otherwise>
								<p>'${otherUser.nickname}'님이 좋아하는 플레이리스트</p>
							</c:otherwise>
						</c:choose> --%>
<%-- 						<c:if test="${isOwner}">
						    <!-- 소유자용 UI -->
						    <p>'${nickname}'님이 좋아하는 플레이리스트</p>
						</c:if>
						<c:if test="${!isOwner}">
						    <!-- 방문자용 UI -->
						    <p>'${otherUser.nickname}'님이 좋아하는 플레이리스트</p>
						</c:if> --%>	
						<p>'${nickname}'님이 좋아하는 플레이리스트</p>					
					</div>
					<div class="right" id="more">
						<p>
							<a href="${pageContext.request.contextPath}/${nickname}/playlist">더보기</a><span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
						</p>
					</div>
				</div>
				<div>
					<p class="index">오늘의 플레이리스트를 확인해보세요!</p>
				</div>
				<div id="playlist">
					<c:choose>
						<c:when test="${empty likeList}">
							<div class = "empty-box">플레이리스트가 없습니다😥</div>						
						</c:when>
						<c:otherwise>
							<c:forEach items="${likeList}" var="vo">
								<div class="nail purple"> <!-- 1~14까지 감정으로색깔 -->
									<div onclick="location.href='${pageContext.request.contextPath}/playlist/folder?playlistNo=${vo.playlistId }&makerUserNo=${vo.userId }'" class="nail-desc">
										<p>${vo.playlistName }</p>
									</div>
									<div>
										<div id="opac" onclick="location.href='${pageContext.request.contextPath}/main?playlistNo=${vo.playlistId }'">  
											<span class="glyphicon glyphicon-play-circle" aria-hidden="true"></span>
										</div>
									</div>
								</div>	
							</c:forEach>							
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<!--content4-->
			<!--content4-->
			<div id="content4">
				<div class="minicontent">
					<div class="left">
						<p>인기순 플레이리스트</p>
					</div>
				</div>
				<div class="right" id="more">
					<p>
						<a href="${pageContext.request.contextPath}/${nickname}/hotplaylist">더보기<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span></a>
					</p>
				</div>
				<div>
					<p class="index">보글이 끓인 오늘의 플레이리스트를 확인해보세요!</p>
				</div>
				<div id="playlist">
					<c:choose>
						<c:when test="${empty ppList}">
							<div class = "empty-box">플레이리스트가 없습니다😥</div>
						</c:when>
						<c:otherwise>
							<c:forEach items="${ppList}" var="vo">
								<div class="nail green">
									<!-- 1~14까지 감정으로색깔 -->
									<div class="nail-desc" onclick="location.href='${pageContext.request.contextPath}/playlists/${vo.playlistId }?makerUserNo=${vo.userId }'">
										<p>${vo.playlistName }</p>
									</div>
									<div>
										<div id="opac" onclick="location.href='${pageContext.request.contextPath}/main?playlistNo=${vo.playlistId }'">
											<span class="glyphicon glyphicon-play-circle" aria-hidden="true"></span>
										</div>
									</div>
								</div>
							</c:forEach>							
						</c:otherwise>
					</c:choose>
				

				</div>
			</div>
			<!--content4-->
			<!--content4-->
			<div id="content4">
				<div class="minicontent">
					<div class="left">
						<p>내가 만든 서평 플레이리스트</p>
					</div>
					<div class="right" id="more">
						<p>
							<a href="${pageContext.request.contextPath}/${nickname}/writeplaylist">더보기</a><span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
						</p>
					</div>
				</div>
				<div>
					<p class="index">내가 만든 플레이리스트를 확인해보세요!</p>
				</div>
				<div id="playlist">
					<c:choose>
						<c:when test="${empty myList}">					
							<div class = "empty-box">플레이리스트가 없습니다😥</div>
						</c:when>
						<c:otherwise>
							<c:forEach items="${myList}" var="vo">						
								<div class="nail deeppurple"> <!-- 1~14까지 감정으로색깔 -->
									<div class="nail-desc"  onclick="location.href='${pageContext.request.contextPath}/playlist_folder/${vo.playlistId }'">
										<p>${vo.playlistName }</p>
									</div>
									<div>
										<div id="opac" onclick="location.href='${pageContext.request.contextPath}/main?playlistNo=${vo.playlistId }'">
											<span class="glyphicon glyphicon-play-circle" aria-hidden="true"></span>
										</div>
									</div>
								</div>							
							</c:forEach>
						</c:otherwise>						
					</c:choose>
				</div>
			</div>
			<!--content4-->
		</div>
		<!-- footer -->
		<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		<!-- modal창 -->
		<c:import url="/WEB-INF/views/include/modal.jsp"></c:import>
	</div>
	<!--wrap-->
</body>
</html>