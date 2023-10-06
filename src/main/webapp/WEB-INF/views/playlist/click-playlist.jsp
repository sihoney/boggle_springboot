<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>플레이리스트 폴더</title>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/all_css.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/playlist-click.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/style.css">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/maru-buri.css" rel="stylesheet">

	<script src="${pageContext.request.contextPath}/resources/static/js/jquery-1.12.4.js"></script>
	<script src="${pageContext.request.contextPath}/resources/static/bootstrap/js/bootstrap.js"></script>
	<script src="${pageContext.request.contextPath}/resources/static/js/more.js" defer></script>
	<script src="${pageContext.request.contextPath}/resources/static/js/playlistFolder.js" defer></script>
</head>

<!--header-->
<body>
	<div id="wrap">

		<!-- ------header------ -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
		<!-- ------header------ -->

		<!-- ------nav------ -->
		<div id="nav" class="clearfix">
			<c:choose>
				<c:when test="${param.userId == authUser.userId}">
					<ul class="nav nav-tabs">
						<li role="presentation"><a href="${pageContext.request.contextPath}/${authUser.nickname}/mybook">내 서평</a></li>
						<li role="presentation"><a href="${pageContext.request.contextPath}/${authUser.nickname}/tastemain">취향저격</a></li>
						<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${authUser.nickname}/playlist">플레이리스트</a></li>
						<!-- <li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${param.nickname}/like_playlist">플레이리스트</a></li> -->
						
						<!--세션 아이디와 사이트아이디 같을때-->
						<!-- <li role="presentation"><a href="${pageContext.request.contextPath}/analyze">통계</a></li> -->
					</ul>
				</c:when>
				<c:otherwise>
					<!-- 세션아이디랑 다를때는 사이트주소의 아이디와 같은 유저의 데이터들 불러오기-->
					<ul class="nav nav-tabs">
						<li role="presentation"><a href="${pageContext.request.contextPath}/${param.nickname}/mybook">남 서평</a></li>
						<li role="presentation"><a href="${pageContext.request.contextPath}/${param.nickname}/tastemain">취향저격</a></li>
						<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${param.nickname}/playlist">플레이리스트</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
		<!-- ------nav------ -->

		<!--cover-->
		<!-- 
		<div id="playlist-cover" class="clearfix">
			<div class="float-l">
				<p>${requestScope.foldermainMap.playlistCover.nickname}님의플레이리스트</p>
				<h1 id="playlist-title">${requestScope.foldermainMap.playlistCover.playlistName}</h1>
			</div>

			<div id="btn-cover" class="float-r">
				<button type="button" id="playlistLike" class="btn btn-default float-r"
					data-playlistno="${param.playlistNo}"
					data-userId="${authUser.userId}" 
					data-nickname="${param.nickname}">
					좋아요
					<span id="likeview" class="" aria-hidden="true"></span>
				</button>
				<button type="button" class="btn btn-default float-r" onclick="location.href = '${pageContext.request.contextPath}/main?playlistNo=${requestScope.foldermainMap.playlistCover.playlistNo}';">전체재생</button>
			</div>
		</div>
		-->
		<!--cover-->
		<!-- 플레이리스트 생성한 유저만 수정가능 -->
		<!--  
		<div id="middle-content">
			
			<c:if test="${authUser.userId == param.userId}">
				<div id="playlist-add" data-keyboard="false" data-backdrop="static">
					<span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 
					<span>플레이리스트 서평 추가</span>
				</div>

				<div id="btnwrap-delete">
					<button type="button" class="btn btn-default">전체선택</button>
					<button type="button" class="btn btn-default">선택삭제</button>
				</div>
			</c:if>
		</div>
		-->
		<!--review List-->
		<div id="review-wrap">

			<c:forEach items="${requestScope.reviewList}" var="playlistVo">
				<!-- 서평 리스트 vo-->
				<div class="jumbotron">
					<div id="reviewVo-wrap">
						<div id="review_first">
							<h3>
								<a href="${pageContext.request.contextPath}/bookdetail?bookNo=${playlistVo.bookEntity.isbn}&userId=${playlistVo.userId}">${playlistVo.bookEntity.bookName}</a>
							</h3>
							<!-- 자기글에만 수정 삭제 노출 -->
<!--	
							<c:if test="${authUser.userId == playlistVo.userId}">
								<a id="${playlistVo.reviewId }" class="review_modify btn_delete_review" data-reviewId="${playlistVo.reviewId}">삭제</a>
								<a href="${pageContext.request.contextPath}/review/write?reviewId=${playlistVo.reviewId}" class="review_modify">수정</a>
							</c:if>

 							<a href="${pageContext.request.contextPath}/${playlistVo.nickname}" class="review_nick">
								${playlistVo.nickname}
								<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
							</a> 
-->

							<div class="multiline-ellipsis">${playlistVo.content}</div>
						</div>

						<div id="review_second">
							
							<!-- 하트 아이콘 -->
							<c:choose>
								<c:when test="${playlistVo.likeByAuthUser eq true}">
									<!-- 좋아요 활성화 -->
									<span id="btn_like_${playlistVo.reviewId }" class="btn_like glyphicon glyphicon-heart icon-success" aria-hidden="true"></span>
								</c:when>
								<c:otherwise>
									<!-- 좋아요 비활성화 -->
									<span id="btn_like_${playlistVo.reviewId }" class="btn_like glyphicon glyphicon-heart-empty icon-success" aria-hidden="true"></span> 													
								</c:otherwise>
							</c:choose>


							<span class="review_like">${playlistVo.likeCount}</span>
							<span class="review_like">${playlistVo.createdAt}</span> 
							<span id="tag_btn">#${playlistVo.emotionEntity.emotionName}</span>
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
				</div>
			</c:forEach>
			<!-- 서평 리스트 vo-->

			<!-- 페이징 
			<nav id="page">
				<ul class="pagination">

					<c:if test="${foldermainMap.prev == true}">
						<li>
							<a href="${pageContext.request.contextPath}/playlist/folder?playlistNo=${foldermainMap.playlistCover.playlistNo}&userId=${foldermainMap.playlistCover.userId}&nickname=${param.nickname}&crtPage=${foldermainMap.startPageBtnNo-1}"
								aria-label="Previous"> 
								<span aria-hidden="true">&laquo;</span>
							</a>
						</li>
					</c:if>

					<c:forEach begin="${requestScope.foldermainMap.startPageBtnNo}" end="${requestScope.foldermainMap.endPageBtnNo}" step="1" var="i">
						<c:choose>
							<c:when test="${param.crtPage == i}">
								<li class="">
									<a class="btn-active" href="${pageContext.request.contextPath}/playlist/folder?playlistNo=${foldermainMap.playlistCover.playlistNo}&userId=${foldermainMap.playlistCover.userId}&nickname=${param.nickname}&crtPage=${i}">${i}</a>
								</li>
							</c:when>
							<c:otherwise>
								<li>
									<a href="${pageContext.request.contextPath}/playlist/folder?playlistNo=${foldermainMap.playlistCover.playlistNo}&userId=${foldermainMap.playlistCover.userId}&nickname=${param.nickname}&crtPage=${i}">${i}</a>
								</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:if test="${foldermainMap.next == true}">
						<li>
							<a href="${pageContext.request.contextPath}/playlist/folder?playlistNo=${foldermainMap.playlistCover.playlistNo}&userId=${foldermainMap.playlistCover.userId}&nickname=${param.nickname}&crtPage=${foldermainMap.endPageBtnNo+1}"
								aria-label="Next"> 
								<span aria-hidden="true">&raquo;</span>
							</a>
						</li>
					</c:if>

				</ul>
			</nav>
			-->
			<!-- 페이징 -->

			<!-- footer -->
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
			<!-- footer -->
		</div>
		<!-- playlist-wrap -->
	</div>
	<!--wrap-->

	<!-- 서평 추가 모달 -->
	<div id="review-add" class="modal fade" role="dialog"
		style="z-index: 1600;">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body">
					<div class="modal-container">
						<div class="modal-header">
							<a class="modal-close">뒤로가기</a>
							<div class="input-box">
								<!-- <form action="reviewSearch" method="get"> -->
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								<input id="reviewSearch" type="text"
									placeholder="키워드를 검색해 주세요(Enter)" name="SearchTxt" value="">
								<i class="fa-solid fa-magnifying-glass"></i>
								<!-- </form> -->
							</div>
						</div>
						<div class="modal-options">
							<button class="optionBtn">서재안의 모든 서평</button>
						</div>
						<div class="modal-list">
							<ul id="reviewAll"
								data-playlistno=""
								data-userId="">
								<!-- 서평 vo 반복 -->
								<!-- 리스트 출력될 곳 -->
							</ul>
						</div>

						<!-- paging -->
						<nav class="paging" aria-label="Page navigation example">
							<ul id="addModal-pagination" class="pagination">
								<!-- 페이징 번호 -->
							</ul>
						</nav>
						<!-- paging -->

						<button class="addReviewBtn">선택한 서평 담기</button>
					</div>

				</div>
			</div>
		</div>
	</div>
	<!-- 서평 추가 모달 -->
</body>
</html>