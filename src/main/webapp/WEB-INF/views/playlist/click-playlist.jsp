<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>플레이리스트 폴더</title>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/all_css.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/playlist-click.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/maru-buri.css" rel="stylesheet">

	<script src="${pageContext.request.contextPath}/js/jquery-1.12.4.js"></script>
	<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
	<script src="${pageContext.request.contextPath}/js/more.js" defer></script>
	<script src="${pageContext.request.contextPath}/js/playlistFolder.js" defer></script>
</head>

<!--header-->
<body>
	<div id="wrap">

		<!-- ------header------ -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
		<!-- ------header------ -->

		<c:import url="/WEB-INF/views/include/nav.jsp">
			<c:param name="path" value="playlist" />
		</c:import>
		
		<!-- cover -->
		<div id="playlist-cover" class="clearfix">
			<div class="float-l">
				<p>${requestScope.playlistCover.nickname}님의플레이리스트</p>
				<h1 id="playlist-title">${requestScope.playlistCover.playlistName}</h1>
			</div>
			<div id="btn-cover" class="float-r">
				<button type="button" id="playlistLike" class="btn btn-default float-r"
					data-playlistno="${requestScope.playlistCover.playlistId}"
					data-userId="${authUser.userId}" 
					data-nickname="">
					좋아요
					
					<c:choose>
						<c:when test="${requestScope.playlistCover.likeByAuthUser }">
							<span id="likeview" class="glyphicon glyphicon-heart" aria-hidden="true"></span>
						</c:when>
						<c:otherwise>
							<span id="likeview" class="glyphicon glyphicon-heart-empty" aria-hidden="true"></span>
						</c:otherwise>
					</c:choose>					
					
				</button>			
				<button type="button" class="btn btn-default float-r" onclick="location.href = '${pageContext.request.contextPath}/main?playlistId=${requestScope.playlistCover.playlistId}';">전체재생</button>
			</div>
		</div>
		<!-- cover -->
		
		<!-- 플레이리스트 생성한 유저만 수정가능 -->
		<c:if test="${authUser.nickname eq playlistCover.nickname}">
			<div id="middle-content">
				<div id="playlist-add" data-keyboard="false" data-backdrop="static">
					<span class="glyphicon glyphicon-edit" aria-hidden="true"></span> 
					<span>서평 추가</span>
				</div>
				<div id="btnwrap-delete">
					<button type="button" class="btn btn-default">전체선택</button>
					<button type="button" class="btn btn-default">선택삭제</button>
				</div>
			</div>
		</c:if>
		
		<!--review List-->
		<main id="review-wrap">
			<div id="jumbo-wrap">
			<c:forEach items="${requestScope.reviewList}" var="playlistVo">
				<!-- 서평 리스트 vo-->
				<div class="jumbotron">
					<div id="reviewVo-wrap">
						<div id="review_first">
							<h3>
								<a href="${pageContext.request.contextPath}/bookdetail?bookNo=${playlistVo.bookEntity.isbn}&userId=${playlistVo.userId}">${playlistVo.bookEntity.bookName}</a>
							</h3>
							
							<!-- 본인이 만든 플레이리스트에만 출력 -->
							<c:if test="${authUser.nickname eq playlistCover.nickname}">
								<a id="${playlistVo.reviewId }" class="review_modify btn_delete_review" data-reviewId="${playlistVo.reviewId}">삭제</a>
								<%-- <a href="${pageContext.request.contextPath}/review/write?reviewId=${playlistVo.reviewId}" class="review_modify">수정</a> --%>
							</c:if>
							<!-- 자기글에만 수정 삭제 노출 -->

							<a href="${pageContext.request.contextPath}/${playlistVo.nickname}/mybook" class="review_nick">
								${playlistVo.nickname}
								<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
							</a>

							<div class="multiline-ellipsis">${playlistVo.content}</div>
						</div>

						<div id="review_second">
							<!-- 하트 아이콘 -->
							<c:choose>
								<c:when test="${playlistVo.likeByAuthUser eq true}">
									<!-- 좋아요 활성화 -->
									<span id="btn_like_${playlistVo.reviewId }" class="btn_like glyphicon glyphicon-heart icon-success" data-reviewId="${playlistVo.reviewId }" aria-hidden="true"></span>
								</c:when>
								<c:otherwise>
									<!-- 좋아요 비활성화 -->
									<span id="btn_like_${playlistVo.reviewId }" class="btn_like glyphicon glyphicon-heart-empty icon-success" data-reviewId="${playlistVo.reviewId }" aria-hidden="true"></span> 													
								</c:otherwise>
							</c:choose>
							<!-- 하트 아이콘 -->

							<%-- <span class="review_like">${playlistVo.likeCount}</span> --%>
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
			</div>
			<!-- 서평 리스트 vo-->
		
			<!-- 페이징 --> 
			<div id="page">
				<ul class="pagination">
 
  					<c:forEach begin="${requestScope.startPageBtnNo}" end="${requestScope.endPageBtnNo}" step="1" var="i">
								<li class="page-btn">
									<!-- ${pageContext.request.contextPath}/playlist/folder?playlistNo=${foldermainMap.playlistCover.playlistNo}&userId=${foldermainMap.playlistCover.userId}&nickname=${param.nickname}&crtPage=${i} -->
									<a>${i}</a>
								</li>
					</c:forEach>

				</ul>
			</div>
			<!-- 페이징 -->
		</main>
		
		<!-- footer -->
		<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		<!-- footer -->
		<!-- playlist-wrap -->
	</div>
	<!--wrap-->

	<!-- 서평 추가 모달 -->
	<div id="review-add" class="modal fade" role="dialog"
		style="z-index: 1600;">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body modal_body">
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