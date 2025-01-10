<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html>
<html lang="ko">    
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>책 상세페이지</title>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/source.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/all_css.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/book_detail.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/modal.css">
	
	<link href="https://hangeul.pstatic.net/hangeul_static/css/maru-buri.css" rel="stylesheet">
	
	<script src="${pageContext.request.contextPath}/resources/static/js/jquery-1.12.4.js"></script>
	<script src="${pageContext.request.contextPath}/resources/static/bootstrap/js/bootstrap.js"></script>
	<script src="${pageContext.request.contextPath}/resources/static/js/bookdetail.js"></script>
    <script src="${pageContext.request.contextPath}/resources/static/js/more.js"></script>
	
     <!-- kakao sdk 호출 -->
	<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
</head>
<body>
    <div id="wrap">
		<!-- 헤더 -->
        <c:import url="/WEB-INF/views/include/header.jsp"></c:import>

		<!-- ------nav------ -->
		<c:import url="/WEB-INF/views/include/nav.jsp"></c:import>

        <div id="contents" class="clearfix">
            <div id="bookVo">
                <img id="book_img" src="${requestScope.bookEntity.coverUrl}" alt="..." class="img-thumbnail">
                <div id="book_detail">
                    <h1>${requestScope.bookEntity.bookName}</h1>
                    <h3>저자 ${requestScope.bookEntity.author}</h3>
                    <div id="book_review">
                        <span class="review_count">서평 수</span><span class="review_num">+</span>
                    </div>
                    <div id="btn_review">
                        <button type="button" class="btn blue-2 font-color-w " onclick="window.open('${requestScope.bookEntity.bookUrl}')">알라딘 북스</button>
						
						<!-- 북마크 버튼 -->
						<div id="bookmark" data-userno="${sessionScope.authUser.userId}" data-bookno="${bookEntity.isbn}">
						<c:choose>
							<c:when test="${requiestScope.likeBook }">
								<!-- <button id="addMark" type="button" class="btn Markbtn" data-markresult="true">관심가는 책 + </button> -->
								<button id="deleteMark" type="button" class="btn Markbtn" data-markresult="false">관심가는 책 - </button>									
							</c:when>
							<c:otherwise>
								<button id="addMark" type="button" class="btn Markbtn" data-markresult="true">관심가는 책 + </button>
								<!-- <button id="deleteMark" type="button" class="btn Markbtn" data-markresult="false">관심가는 책 - </button> -->							
							</c:otherwise>
						</c:choose>
						</div>
						<!-- 북마크 버튼 -->
                    </div>
                </div>
                <button id="btn_write" type="button" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/write?isbn=${requestScope.bookEntity.isbn}';">이 책 서평 쓰기</button>
            </div>
        </div>

        <div class="reviewVo" class="clearfix">
            <div id="list" class="clearfix">
                <ul id="listing" data-bookno="" data-userno="">
                    <li><a id="latest-order" class=""><span>최신순</span></a></li>
                    <li><a id="best-order" class=""><span>인기순</span></a></li>
                </ul>
            </div>

			<!-- 서평 리스트 vo-->
			<div id="reviewlistVo">
				<c:forEach items="${requestScope.reviewList }" var="reviewVo">
					 <div class="jumbotron">
					 	<div id="review_first">
					 		<h3>${reviewVo.bookEntity.bookName } </h3>
					 		
					 		<c:if test="${reviewVo.userId eq sessionScope.authUser.userId }">
					 			<a id="reviewDelete" class="review_modify" data-reviewno="reviewVo.reviewNo">삭제</a>
					 			<a href="review/write?reviewNo=reviewVo.reviewNo" class="review_modify">수정</a>					 		
					 		</c:if>
					 		
					 		<a href="reviewVo.nickname" class="review_nick">${reviewVo.nickname } <span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span></a>
					 		<div class="multiline-ellipsis">${reviewVo.content }</div>
					 	</div>
					 	<div id="review_second">
					 		<!-- 좋아요 -->
					 		<c:choose>
					 			<c:when test="${reviewVo.likeByAuthUser }">
									<span id="btn_like" class="glyphicon glyphicon-heart icon-success" aria-hidden="true"></span>					 							 				
					 			</c:when>
					 			<c:otherwise>
									<span id="btn_like" class="glyphicon glyphicon-heart-empty icon-success" aria-hidden="true"></span>					 			
					 			</c:otherwise>
					 		</c:choose>
					 		<!-- /좋아요 -->
					 		<span class="review_like">${reviewVo.likeCount }</span><span class="review_like">${reviewVo.createdAt }</span>
					 		<span id="tag_btn">${reviewVo.emotionEntity.emotionName }</span>
					 		<div class="dropup float-r">
					 			<a id="dLabel" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">+ 더보기</a>
					 			<ul class="dropdown-menu radius-15" role="menu" aria-labelledby="dropdownMenu2">
					 				<li role="presentation"><a id="add_pli" role="menuitem" tabindex="-1">플레이리스트에 추가<span id="plus"></span></a></li>
					 				<li role="presentation" class="divider"></li>
					 				<li role="presentation"><a id="shr_review" role="menuitem" tabindex="-1">서평 공유하기<span class="glyphicon glyphicon-share" aria-hidden="true"></span></a></li>
					 				<li role="presentation" class="divider"></li>
					 				<li role="presentation"><li role="presentation"><a id="save_img" role="menuitem" target="_blank" tabindex="-1" href="${pageContext.request.contextPath}/imgpreview">이미지 저장하기<span class="glyphicon glyphicon-save" aria-hidden="true"></span></a></li>
					 			</ul>
					 		</div>	
						</div>	
				</c:forEach>
			</div>
            <!-- 서평 리스트 vo-->
           
	        <!-- 페이지네이션 -->
	        <nav class="pageWrap">
	            <ul class="pagination">
	              <li>
	                <a href="" aria-label="Previous">
	                  <span aria-hidden="true">&laquo;</span>
	                </a>
	              </li>
	              
					<c:forEach begin="${requestScope.startPage}" end="${requestScope.endPage}" step="1" var="i">
								<li class="active"><a>${i}<span class="sr-only"></a></li>
					</c:forEach>	    
					
	              <li>
	                <a href="" aria-label="Next">
	                  <span aria-hidden="true">&raquo;</span>
	                </a>
	              </li>
	            </ul>
	        </nav>         
           <!-- 페이지네이션 -->
					          
        </div>

        <!-- 모달창 -->
        <div id="myModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">공유하기</h4>
                    </div>
                    <div class="modal-body">
                        <p>BOGGLE의 서평을 SNS에 공유해 보세요 :)</p>
                        <a id="kakao-link-btn" href="javascript:kakaoShare()">
                            <img src="${pageContext.request.contextPath}/resources/static/images/share/kakao.jpg" />
                        </a>
                        <a id="facebook-link-btn" href="">
                            <img src="${pageContext.request.contextPath}/resources/static/images/share/facebook.jpg" />
                        </a>
                        <a id="twitter-link-btn" href="">
                            <img src="${pageContext.request.contextPath}/resources/static/images/share/twitter.png" />
                        </a>
                        <a id="kakaostory-link-btn" href="">
                            <img src="${pageContext.request.contextPath}/resources/static/images/share/kakaostory.png" />
                        </a>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        <!-- Modal -->

        <!-- 페이지네이션 -->
        <!-- <nav class="pageWrap">
            <ul class="pagination">
              <li>
                <a href="" aria-label="Previous">
                  <span aria-hidden="true">&laquo;</span>
                </a>
              </li>
              <li class="active"><a href="">1 <span class="sr-only">(current)</span></a></li>
              <li><a href="">2</a></li>
              <li><a href="">3</a></li>
              <li><a href="">4</a></li>
              <li><a href="">5</a></li>
              <li>
                <a href="" aria-label="Next">
                  <span aria-hidden="true">&raquo;</span>
                </a>
              </li>
            </ul>
        </nav> -->

        <!-- footer -->
        <c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
        
        <!-- modal창 -->
        <c:import url="/WEB-INF/views/include/modal.jsp"></c:import>
    </div>
</body>	
</html>