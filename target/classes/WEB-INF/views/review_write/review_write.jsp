<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Write Review</title>

    <script src="${pageContext.request.contextPath}/resources/static/js/jquery-1.12.4.js"></script>
    <script src="${pageContext.request.contextPath}/resources/static/bootstrap/js/bootstrap.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/all_css.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/write.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/modal.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/source.css">
	
	<script src="${pageContext.request.contextPath}/resources/static/js/modal_write_review.js" defer></script>
	<script src="${pageContext.request.contextPath}/resources/static/js/write_review.js" defer></script>
	
    <!-- JavaScript Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    
    <!-- font(수정 전) -->
    <link href="https://hangeul.pstatic.net/hangeul_static/css/maru-buri.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-pen.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-gothic-eco.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-myeongjo.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/NanumGaRamYeonGgoc.css" rel="stylesheet">	
</head>
<body>

    <div id="wrap">
		<div id="container">
			
			<!-- 헤더 -->
            <c:import url="/WEB-INF/views/include/header.jsp"></c:import>
            
            <!-- userNo -->
            <input class="userNo" type="hidden" name="userNo" value="${sessionScope.authUser.userId }">

			<!-- progress bar -->
			<div id="progress_bar">
                <div class="progressbar-wrapper">
                    <ul class="progressbar">
                        <li>책 선택</li>
                        <li>감정 태그</li>
                        <li>스타일</li>
                        <li>서평 쓰기</li>
                    </ul>
                </div>
            </div>
            <!-- /progress bar -->
           

			<!-- 책 선택하기 -->
			
			<div id="contents" class="clearfix selectbook-header">
				<div id="book_select">
					<h1>책 선택하기</h1>
				</div>
			</div>
			
			<div class="jumbotron" data-toggle="modal" data-target="#modal_searchbook">
				<div id="select_box">
					<div class="button">
						<button class="btn_circle">+</button>
					</div>
					<p>읽은 책을 검색해주세요</p>
				</div>
			</div>
			
            <!-- 책 선택 완료(selectBookOk) -->
            <!-- 
            <div id="contents" class="clearfix selectedbook">
				<div id="select_box">
					<div id="bookVo">
						<img id="book_img" src="../img/book/book2.jpeg" alt="..." class="img-thumbnail">
						<div id="book_detail">
							<h1>자유로부터의 도피</h1>
							<h3>저자 에리히 프롬(Erich Fromm)</h3>
							<div id="book_review">
								<span class="review_count">서평 수 </span><span class="review_num">163+</span>
							</div>
						</div>
						<button id="btn_delete" type="button" class="btn btn-light">삭제</button>
						<button id="btn_modify" type="button" class="btn btn-light">수정</button>
					</div>
				</div>
			</div>
			-->		
	
			<!-- 감정 태그 선택하기 -->
			<div id="contents" class="clearfix">
				<div id="mood_tag">
					<h1>감정 태그</h1>
				</div>
			</div>

			<div id="btn_mood">
 				<c:forEach var="emotionObj" items="${writeFormResponse.emotionList}">
					<button type="button" id="${emotionObj.emotionId }" class="btn btn-primary">${emotionObj.emotionName }</button>
				</c:forEach>
			</div>
            <!-- /감정 태그 선택하기 -->


			<!-- 스타일 선택하기 -->
			<div id="contents" class="clearfix">
				<div id="style">
					<h1>스타일</h1>
				</div>
			</div>

			<div id="select-style" class="clearfix">
				<div class="btn-group" role="group" aria-label="...">
 					<c:forEach var="fontObj" items="${writeFormResponse.fontList}">
						<button class="btn_style btn-outline-secondary" data-fontid="${fontObj.fontId }" style="font-family: ${fontObj.fontName}">폰트</button>
					</c:forEach> 
					
 					<c:forEach var="wallpaperObj" items="${writeFormResponse.wallpaperList}">
						<button class="btn_style btn-outline-secondary" data-wallpaperid="${wallpaperObj.wallpaperId }" style="background-image: url('${pageContext.request.contextPath}/resources/static/images/review_card/${wallpaperObj.wallpaperName}')">배경</button>
					</c:forEach> 
				</div>
			</div>
			<!-- /스타일 선택하기 -->
			

			<!-- 서평 쓰기 -->
			<div id="contents" class="clearfix">
				<div id="review_box">
					<!--  
                    <p>
                        “내 삶에서 불가피하게 직면해야 했던 시기가 있습니다.<br>바로 1958년의 여름, 나의 열일곱 살 무렵 말입니다. <br> 나는 그 시기를 사회·역사적으로 그려 내기를 바랐고,<br> 이를테면 오토픽션의 방법으로 『그들의 말 혹은 침묵』을 썼습니다.” <br> -아니 에르노					
                    </p>
                    -->
                    
                    <textarea id="review_textarea" rows="1" placeholder="글자를 입력하세요" spellcheck="false"></textarea>
				</div>
			</div>
            <p id="limit-text">
                0/200
            </p>
            <!-- /서평 쓰기 -->

			<!-- 플레이리스트에 추가 -->
			<!--  
            <div id="contents" class="clearfix">
				<div id="add_playlist">
					<h1>플레이리스트에 추가</h1>
				</div>
			</div>
			<div class="jumbotron" data-toggle="modal" data-target="#modal_playlist">
				<div id="select_box">
					<div class="button">
						<button class="btn_circle">+</button>
					</div>
					<p>플레이리스트</p>
				</div>
			</div>			 
			-->
			<!-- 모달창 -->
			<c:import url="/WEB-INF/views/include/modal.jsp"></c:import>

			<!-- 플리 추가 질문 모달 -->
			<div class="modal_question unstaged">
				<p>저장되었습니다. 플레이리스트에 추가하시겠습니까?</p>
				<button class="mqBtn" name="addToPlaylist" value="yes">예</button>
				<button class="mqBtn" name="addToPlaylist" value="no">아니오</button>
			</div>
			
			<div class="msg_modal unstaged" style="top: 15%">
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

		</div>
        <!-- /container -->
    

        <!-- 기록하기 버튼 -->
        <div class="btn">
            <button id="btn_admit" class="btn btn-primary btn-block" type="button">기록하기</button>
        </div>
        <!-- /기록하기 버튼 -->


        <!-- 취소 버튼 -->
        <div class="btn">
            <button id="btn_cancle" class="btn btn-light btn-block" type="button">취소</button>
        </div>
        <!-- /취소 버튼 -->   


		<!-- footer -->
        <%-- <c:import url="/WEB-INF/views/include/footer.jsp"></c:import> --%>
            
        <!-- /footer -->
        
        <!-- modal창 -->
        <c:import url="/WEB-INF/views/include/modal.jsp"></c:import>


	</div>
    <!-- /wrap -->

    
</body>
</html>