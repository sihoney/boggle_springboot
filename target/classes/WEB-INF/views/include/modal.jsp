<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
	
<!-- 서평: 더보기 모달창 -->
<!-- 서평 공유 모달창 -->
<div id="myModal" class="modal fade">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">공유하기</h4>
			</div>
			<div class="modal-body">
				<p>playbook의 서평을 SNS에 공유해 보세요 :)</p>
				<a id="kakao-link-btn" href="javascript:kakaoShare()"> <img src="${pageContext.request.contextPath}/resources/static/images/share/kakao.jpg" />
				</a> <a id="facebook-link-btn" href=""> <img src="${pageContext.request.contextPath}/resources/static/images/share/facebook.jpg" />
				</a> <a id="twitter-link-btn" href=""> <img src="${pageContext.request.contextPath}/resources/static/images/share/twitter.png" />
				</a> <a id="kakaostory-link-btn" href=""> <img src="${pageContext.request.contextPath}/resources/static/images/share/kakaostory.png" />
				</a>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<!-- Modal -->

<!-- 플레이리스트 모달창 -->
<div class="modal fade" id="modal_playlist" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-container">
				<div class="modalHeader">
					<div class="title">내 플레이리스트</div>
					<button type="button" class="closeBtn" data-dismiss="modal" aria-label="Close">닫기</button>
				</div>
				<div class="modal-playlist">
					<ul>
						<!--  
						<li class="list">
							<div class="img-container">
								<img src="" alt="">
							</div>
							<div class="info-container">
								<button class="tagBtn">힘이 되는</button>
								<div class="playlist-title">별밤에 볼륨을 높이고 싶은가요</div>
								<div class="username">김지연</div>
							</div>
						</li>
						-->
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- /플레이리스트에 추가 -->

<!-- 플레이리스트 모달창 (bootstrap X) -->
<!-- 
<div class="modal_add_playlist">
	<div class="modal_header">
		<div class="title">내 플레이리스트</div>
		<button type="button" class="closeBtn" aria-label="Close">닫기</button>
	</div>
	<div class="modal-playlist">
		<ul>
			 
			<li class="list">
				<div class="img-container">
					<img src="" alt="">
				</div>
				<div class="info-container">
					<button class="tagBtn">힘이 되는</button>
					<div class="playlist-title">별밤에 볼륨을 높이고 싶은가요</div>
					<div class="username">김지연</div>
				</div>
			</li>
			
		</ul>
	</div>
</div>
 -->
<!-- /플레이리스트 모달창 (bootstrap X) -->

<!-- /이미지 저장 모달창 -->
<div class="modal" id="modal_download_img" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">이미지 저장하기</h5>
			</div>
			<div class="modal-body">
				<p>저장되었습니다.</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="Close">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- /공유하기 모달창 -->
<div class="modal" id="modal_share_img" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">공유하기</h5>
			</div>
			<div class="modal-body">
				<p>공유되었습니다.</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="Close">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- /공유하기 모달창 -->
<!-- 책 선택하기 모달창 -->
<div class="modal" id="modal_searchbook" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-container">
				<div class="modalHeader">
					<div class="title">책 선택하기</div>
					<button type="button" class="closeBtn" data-dismiss="modal" aria-label="Close">닫기</button>
				</div>
				<div class="modal-search">
					<form id="searchbook-form"> 				<!-- input 값 받아오려고 추가(희원) -->
						<input type="text" class="search_box" id="searchbook-query" placeholder=" ex) 책 제목, 저자명, 출판사를 검색해보세요" name="query"> 
					</form>
				</div>
				<div class="modal-playlist">
					<ul id="modal-playlist"> <!-- ul에 id 추가(희원) -->
						<!--  
						<li class="list">
							<div class="book-img-container">
								<img src="${pageContext.request.contextPath}/resources/static/img/book/book2.jpeg" alt="" class="img-thumbnail">
							</div>
							<div class="info-container">
								<button class="book-title">자유로부터의 도피</button>
								<div class="book-author">에리히 프롬(Erich Fromm)</div>
								<div class="review-count">서평수 163+</div>
							</div>
						</li>
						-->
					</ul>
				</div>
				<nav class="paging" aria-label="Page navigation example">
					<ul class="pagination">
						<!-- 
						<li class="page-item"><a class="page-link" href="" aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
						</a></li>
						
						<li class="page-item"><a class="page-link" href="">1</a></li>
						<li class="page-item"><a class="page-link" href="">2</a></li>
						<li class="page-item"><a class="page-link" href="">3</a></li>
						<li class="page-item"><a class="page-link" href="">4</a></li>
						<li class="page-item"><a class="page-link" href="">5</a></li>
						
						<li class="page-item"><a class="page-link" href="" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
						</a></li>
						-->
					</ul>
				</nav>
			</div>
		</div>
	</div>
</div>
<!-- /책 선택하기 -->