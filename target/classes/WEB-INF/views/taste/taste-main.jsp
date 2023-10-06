<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>taste-main</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/bootstrap/css/bootstrap.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/all_css.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/taste-main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/write.css">
<link rel="stylesheet" href="/bookproject/asset/css/modal.css">
<script src="${pageContext.request.contextPath}/asset/js/jquery-1.12.4.js"></script>
<script src="${pageContext.request.contextPath}/asset/bootstrap/js/bootstrap.js"></script>
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
						<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}">내 서평</a></li>
						<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${nickname}/tastemain">취향저격</a></li>
						<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/like_playlist">플레이리스트</a></li>
						<!--세션 아이디와 사이트아이디 같을때
						<li role="presentation"><a href="${pageContext.request.contextPath}/analyze">통계</a></li>
						-->
					</ul>
				</c:when>
				<c:otherwise>
					<!-- 세션아이디랑 다를때는 사이트주소의 아이디와 같은 유저의 데이터들 불러오기-->
					<ul class="nav nav-tabs">
						<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}">남 서평</a></li>
						<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${nickname}/tastemain">취향저격</a></li>
						<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/like_playlist">플레이리스트</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
		<!-- ------nav------ -->
		<!-- ------nav2------ -->
		<ul id="nav2" class="nav nav-pills">
			<!-- 세션아이디와 비교, 다를경우 '이름님의 취향' -->
			<!-- <li role="presentation" class="active"><a href="">'유저이름'님의 취향</a></li> -->
			<li role="presentation" class="active"><a href="${pageContext.request.contextPath}/${nickname}/tastemain">my 취향</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/tastereview">좋아요한 서평</a></li>
			<li role="presentation"><a href="${pageContext.request.contextPath}/${nickname}/main_book">관심가는 책</a></li>
		</ul>
		<div id="content">
			<!--content1-->
			<div id="content1">
				<div class="minicontent">
					<div class="left">
						<c:choose>
							<c:when test="${result eq 'sameUser'}">
								<p>'${nickname}'님이 좋아요한 서평</p>
							</c:when>
							<c:otherwise>
								<p>'${otherUser.nickname}'님이 좋아요한 서평</p>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="right" id="more">
						<p>
							<a href="${pageContext.request.contextPath}/${nickname}/tastereview">더보기</a><span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
						</p>
					</div>
				</div>
				<!-- 서평 리스트 -->
				<c:forEach items="${like1 }" var="vo">
					<div id="reviews">
						<div id="reviews-header">
							<div class="left">
								<p>
									<a href="${pageContext.request.contextPath}/bookdetail?bookNo=${vo.bookNo}&userNo=${vo.userNo}">${vo.bookTitle }</a>
								</p>
							</div>
							<!-- 작성자아이디와 세션아이디가 동일할 시에만 보이게 -->
							<div class="right">
								<c:if test="${result eq 'sameUser'}">
									<a href="${pageContext.request.contextPath}/review/write?reviewNo=${vo.reviewNo}">수정</a>
									<a class="delete" data-reviewno="${vo.reviewNo }">삭제</a>
								</c:if>
							</div>
						</div>
						<!-- 작성자아이디와 세션아이디가 동일할 경우에는 안보이게 -->
						<div id="reviewer">
							<a>${vo.nickname }</a>
						</div>
						<div id="reviews-content">
							<p>${vo.reviewContent }</p>
							<span class="label label-default">${vo.emoName }</span>
						</div>
						<div id="reviews-footer">
							<div class="left">
								<span class="like" data-reviewno="${vo.reviewNo }" id="heart" class="glyphicon glyphicon-heart" aria-hidden="true"></span> <span class="likecnt" data-likecnt="${vo.likecnt}">${vo.likecnt }</span> <span>${vo.reviewDate }</span>
							</div>
							<div class="right">
								<div class="dropup">
									<a id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> 더보기 <span class="caret"></span>
									</a>
									<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu2">
										<li role="presentation"><a class="add_pli" data-userno="${vo.userNo }" data-reviewno="${vo.reviewNo }" id="add_pli" role="menuitem" tabindex="-1">플레이리스트에 추가<span id="plus">+</span></a></li>
										<li role="presentation" class="divider"></li>
										<li role="presentation"><a id="shr_review" role="menuitem" tabindex="-1">서평 공유하기<span class="glyphicon glyphicon-share" aria-hidden="true"></span></a></li>
										<li role="presentation" class="divider"></li>
										<li role="presentation">
										<li role="presentation"><a id="save_img" role="menuitem" target="_blank" tabindex="-1" href="${pageContext.request.contextPath}/imgpreview">이미지 미리보기<span class="glyphicon glyphicon-save" aria-hidden="true"></span>
										</a></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
				<!-- 서평 -->
			</div>
			<!--content1-->
			<!--content2-->
			<div id="content2">
				<div class="minicontent">
					<div class="left">
						<c:choose>
							<c:when test="${result eq 'sameUser'}">
								<p>'${nickname}'님이 좋아요한 유저</p>
							</c:when>
							<c:otherwise>
								<p>'${otherUser.nickname}'님이 좋아요한 유저</p>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div>
					<c:forEach items="${likelist}" var="vo">
						<div class="likewriter">
							<a href="${pageContext.request.contextPath}/${vo.nickname}"> <img src="${vo.userPro }" onerror="this.src='${pageContext.request.contextPath}/asset/img/profile.png'" class="img-circle">
							</a>
							<div id="writerinfo">
								<h1>${vo.nickname}</h1>
								<p>서평 수 : ${vo.likecheck }</p>
							</div>
							<p class="word">${vo.reviewContent }</p>
						</div>
					</c:forEach>
				</div>
			</div>
			<!--content2-->
			<!--content3-->
			<div id="content3">
				<div class="minicontent">
					<div class="left">
						<c:choose>
							<c:when test="${result eq 'sameUser'}">
								<p>'${nickname}'님이 관심있는 책</p>
							</c:when>
							<c:otherwise>
								<p>'${otherUser.nickname}'님이 관심있는 책</p>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="right" id="more">
						<p>
							<a href="${pageContext.request.contextPath}/${nickname }/main_book">더보기</a><span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
						</p>
					</div>
				</div>
				<div id="background">
					<div>
						<c:forEach items="${get5book}" var="vo">
							<a class="a" href="${pageContext.request.contextPath}/bookdetail?bookNo=${vo.bookNo}&userNo=${vo.userNo}"> <img class="cover" src="${vo.cover_url }" alt="image" />
							</a>
						</c:forEach>
					</div>
				</div>
			</div>
			<!--content3-->
			<!--content4-->
			<div id="content4">
				<div class="minicontent">
					<div class="left">
						<c:choose>
							<c:when test="${result eq 'sameUser'}">
								<p>'${nickname}'님이 좋아요한 플레이리스트</p>
							</c:when>
							<c:otherwise>
								<p>'${otherUser.nickname}'님이 좋아요한 플레이리스트</p>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="right" id="more">
						<p>
							<a href="${pageContext.request.contextPath}/${nickname }/like_playlist">더보기</a><span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span>
						</p>
					</div>
				</div>
				<div>
					<p class="index">오늘의 플레이리스트를 확인해보세요!</p>
				</div>
				<div id="playlist">
					<c:forEach items="${likeplay}" var="vo">
						<div class="nail purple">
							<!-- 1~14까지 감정으로색깔 -->
							<div onclick="location.href='${pageContext.request.contextPath}/playlist/folder?playlistNo=${vo.playlistNo }&userNo=${vo.userNo }&nickname=${vo.nickname}'" class="nail-desc">
								<p>${vo.playlistName }</p>
							</div>
							<div>
								<div onclick="location.href='${pageContext.request.contextPath}/main?playlistNo=${vo.playlistNo }'" id="opac">
									<span class="glyphicon glyphicon-play-circle" aria-hidden="true"></span>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
			<!--content4-->
		</div>
		<!-- footer -->
		<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		<!-- modal창 
		<c:import url="/WEB-INF/views/include/modal.jsp"></c:import>-->
	</div>
	<!--wrap-->
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

	let reviewNo

	//로딩되기전에 하트모양 요청
	$(document).ready(function() {

		$('#heart').attr('class', 'like glyphicon glyphicon-heart');

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
					url : "${pageContext.request.contextPath }/like",
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
	
	/* 더보기 플리추가 클릭했을 때 이벤트 */
	$(".add_pli").on("click", function(){
		reviewNo = $(this).data("reviewno")
		let userNo = $(this).data("userno")

		// 내 플리 불러오기 
		fetchMyPli(reviewNo, userNo)
		
		// 모달 보임 
		$(".modal_myply").removeClass("unstaged")
		$(".modal_myply").addClass("opaque")
	})

	
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
	
	function renderMyPli(list) {

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
	
	function fetchMyPli(reviewNo, userNo){
		let url = "${pageContext.request.contextPath}/review/getMyPlaylist?userNo=" + userNo
		
		$.ajax({
			url: url,
			type: "post",
			dataType: "json",
			success: function(data){
				console.log(data)
				
				renderMyPli(data)
				
			},
			error:  function(XHR, status, error){
				console.log(status + " : " + error);
			}
		})	
		
	}	
	
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
			url : "${pageContext.request.contextPath }/delete",
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
<script src="${pageContext.request.contextPath}/asset/js/more.js"></script>
</html>