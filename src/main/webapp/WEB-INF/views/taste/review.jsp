<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>taste-main</title>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/all_css.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-review.css">
	<link rel="stylesheet" href="/bookproject/css/modal.css">
	
	<script src="${pageContext.request.contextPath}/js/jquery-1.12.4.js"></script>
	<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
</head>
<!--header-->
<body>
	<div id="wrap">
		<!-- 헤더 -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
		<!-- ------nav------ -->
		<div id="nav" class="clearfix">
			<c:choose>
				<c:when test="${result eq 'sameUser'}">
					<ul class="nav nav-tabs">
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/${nickname}">내 서평</a></li>
						<li role="presentation" class="active"><a
							href="${pageContext.request.contextPath}/${nickname}/tastemain">취향저격</a></li>
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/${nickname}/like_playlist">플레이리스트</a></li>
						<!--세션 아이디와 사이트아이디 같을때
						<li role="presentation"><a href="${pageContext.request.contextPath}/analyze">통계</a></li>
						-->
					</ul>
				</c:when>
				<c:otherwise>
					<!-- 세션아이디랑 다를때는 사이트주소의 아이디와 같은 유저의 데이터들 불러오기-->
					<ul class="nav nav-tabs">
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/${nickname}">남 서평</a></li>
						<li role="presentation" class="active"><a
							href="${pageContext.request.contextPath}/${nickname}/tastemain">취향저격</a></li>
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/${nickname}/like_playlist">플레이리스트</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
		<!-- ------nav------ -->
		<!-- ------nav2------ -->
		<ul id="nav2" class="nav nav-pills">
			<!-- 세션아이디와 비교, 다를경우 '이름님의 취향' -->
			<!-- <li role="presentation" class="active"><a href="">'유저이름'님의 취향</a></li> -->
			<li role="presentation"><a
				href="${pageContext.request.contextPath}/${nickname}/tastemain">my
					취향</a></li>
			<li role="presentation" class="active"><a
				href="${pageContext.request.contextPath}/${nickname}/tastereview">좋아요한
					서평</a></li>
			<li role="presentation"><a
				href="${pageContext.request.contextPath}/${nickname}/main_book">관심가는
					책</a></li>
		</ul>

		<!-- 서평 리스트 -->
		<div id="content">
			<div id="content1">
				<c:forEach items="${like1 }" var="vo">
					<div id="reviews">
						<div id="reviews-header">
							<div class="left">
								<p>
									<a
										href="${pageContext.request.contextPath}/bookdetail?bookNo=${vo.bookNo}&userNo=${vo.userNo}">${vo.bookTitle }</a>
								</p>
							</div>
							<!-- 작성자아이디와 세션아이디가 동일할 시에만 보이게 -->
							 	<div class="right">
						<c:if test="${result eq 'sameUser'}">
							<a
								href="${pageContext.request.contextPath}/review/write?reviewNo=${vo.reviewNo}">수정</a>
							<a class="delete" data-reviewno="${vo.reviewNo }">삭제</a>
						</c:if>
					</div> 
						</div>
						<!-- 작성자아이디와 세션아이디가 동일할 경우에는 안보이게 -->
						<div id="reviewer">
							<a href="${pageContext.request.contextPath}/${vo.nickname }">${vo.nickname }</a>
						</div>
						<div id="reviews-content">
							<p>${vo.reviewContent }</p>
							<span class="label label-default">${vo.emoName }</span>
						</div>
						<div id="reviews-footer">
							<div class="left">
								<span class="heart" data-reviewno="${vo.reviewNo }"
									class="glyphicon glyphicon-heart" aria-hidden="true"></span> <span
									class="likecnt" data-likecnt="${vo.likecnt}">${vo.likecnt }</span>
								<span>${vo.reviewDate }</span>
							</div>
							<div class="right">
								<div class="dropup">
									<a id="dLabel" type="button" data-toggle="dropdown"
										aria-haspopup="true" aria-expanded="false"> 더보기 <span
										class="caret"></span>
									</a>
									<ul class="dropdown-menu" role="menu"
										aria-labelledby="dropdownMenu2">
										<li role="presentation"><a class="add_pli"
											role="menuitem" tabindex="-1">플레이리스트에 추가<span id="plus">+</span></a></li>
										<li role="presentation" class="divider"></li>
										<li role="presentation"><a class="shr_review"
											role="menuitem" tabindex="-1">서평 공유하기<span
												class="glyphicon glyphicon-share" aria-hidden="true"></span></a></li>
										<li role="presentation" class="divider"></li>
										<li role="presentation">
										<li role="presentation"><a role="menuitem" tabindex="-1"
											target="_blank" href="/bookproject/view/${vo.reviewNo}">이미지
												미리보기<span class="glyphicon glyphicon-save"
												aria-hidden="true"></span>
										</a></li>
									</ul>
								</div>


							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>

		<!-- footer -->
		<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		<!-- modal창 -->
		<c:import url="/WEB-INF/views/include/modal.jsp"></c:import>
		<!-- wrap -->
	</div>

</body>
<script type="text/javascript">
	//로딩되기전에 하트모양 요청
	$(document).ready(function() {
		$('.heart').attr('class', 'like glyphicon glyphicon-heart');

	});

	//좋아요 버튼을 클릭했을때(이벤트)
	$("#content1").on(
			"click",
			".like",
			function() {

				//데이터수집
				var $this = $(this);

				var no = $this.data("reviewno");
				//var likecnt = parseInt($(this).next().data("likecnt"));
				var likecnt = $this.next().data("likecnt");

				//출력(리뷰넘버찍어보기), json 으로 보내주기
				console.log("서평넘버 : " + no + ", 좋아요 수 : " + likecnt);

				var clickReviewVo = {
					reviewNo : no
				};

				//요청 : json 방식
				$.ajax({
					//url로 요청할게!    
					url : "${pageContext.request.contextPath }/like1",
					type : "post",
					contentType : "application/json", //보낼때 json으로 보낼게
					data : JSON.stringify(clickReviewVo),
					//주소뒤에 갈 데이터 전송방식, //자바 스크립트 객체를 json형식으로 변경
					dataType : "json", //json> javascript

					success : function(likeok) {

						//좋아요인경우
						if (likeok.likecheck == 0) {

							console.log("좋아요");
							console.log(likeok.likecnt);

							//하트모양변경
							$this.attr('class',
									'like glyphicon glyphicon-heart');

							//카운트 +1
							$this.next().html(likeok.likecnt + 1);

						} else {

							console.log("좋아요취소");

							$this.attr('class',
									'like glyphicon glyphicon-heart-empty');

							$this.next().html(likeok.likecnt - 1);
						}

					},
					//로그인하지 않은경우(모달창띄워주기)
					error : function(XHR, status, error) {
						console.error(status + " : " + error);
					}
				});

			});

	//삭제 버튼을 눌렀을때
	$("#content1").on("click", ".delete", function() {

		//데이터수집
		var $this = $(this);
		var no = $this.data("reviewno");

		//출력
		console.log("삭제하려는서평 : " + no);

		var clickReviewVo = {
			reviewNo : no
		};

		//요청 : json 방식
		$.ajax({
			//url로 요청할게!    
			url : "${pageContext.request.contextPath }/delete1",
			type : "post",
			contentType : "application/json", //보낼때 json으로 보낼게
			data : JSON.stringify(clickReviewVo),
			//주소뒤에 갈 데이터 전송방식, //자바 스크립트 객체를 json형식으로 변경
			dataType : "json", //json> javascript

			success : function(checkuser) {
				if (checkuser == 1) {
					//해당 서평 삭제(화면변화)
					$("#r" + no).remove();
					//삭제알림  
					console.log("리뷰삭제")
					alert('서평이 삭제되었습니다! :-)');
					window.location.reload()

				} else {
					//삭제실패알림  
					console.log("리뷰삭제실패")
					alert('잘못된 접근입니다! :-/');
					window.location.reload()
				}
			},
			//로그인하지 않은경우(모달창띄워주기)
			error : function(XHR, status, error) {
				console.error(status + " : " + error);
			}
		});
	});
</script>
<script src="${pageContext.request.contextPath}/js/more2.js"></script>
</html>