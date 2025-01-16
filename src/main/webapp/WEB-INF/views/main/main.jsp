<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Boggle - main</title>
    
    <link href="https://hangeul.pstatic.net/hangeul_static/css/maru-buri.css" rel="stylesheet">
    <link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-barun-pen.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-gothic-eco.css" rel="stylesheet">
	<link href="https://hangeul.pstatic.net/hangeul_static/css/nanum-myeongjo.css" rel="stylesheet">
	
	<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/style3.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/style3-slide.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/style3-sidebar.css">
    <!--
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/style3-modal.css">
    -->
    <script src="${pageContext.request.contextPath}/resources/static/js/jquery-1.12.4.js"></script>
    <script src="${pageContext.request.contextPath}/resources/static/js/app3.js" defer></script>
    <script src="${pageContext.request.contextPath}/resources/static/js/main-slide.js" defer></script>
    <script src="${pageContext.request.contextPath}/resources/static/js/main-sidebar.js" defer></script> 
    <script src="${pageContext.request.contextPath}/resources/static/js/main-modal.js" defer></script>
</head>
<body>
	<!-- Header -->
    <header>
        <!-- nav -->
        <nav>
            <div class="nav-header">
                <div class="logo"><img src="${pageContext.request.contextPath}/resources/static/images/logo/logo2.png"></div>
            </div>
            <div class="links-container">
                <ul class="links">				
					<sec:authentication var="auth" property="principal" />
						
					<sec:authorize access="isAuthenticated()">
					    <!-- 로그인한 사용자에게만 보이는 콘텐츠 -->			    
					    <li class="mobile_delete"><a href="${pageContext.request.contextPath}/reviews/new">기록하기</a></li>
					    <li class="login" data-logStatus="login" data-userNo="${auth.userId}">
					        <a href="${pageContext.request.contextPath}/my-reviews">
					            <div class="userImg">
					                <img id="header-img-icon" src="${pageContext.request.contextPath}/resources/static/images/${auth.profileUrl}" class="img-circle" onerror="this.src='${pageContext.request.contextPath}/resources/static/images/profile.png'">
					            </div>
					            <a href="${pageContext.request.contextPath}/my-reviews">
					                <c:out value="${auth.nickname}" />
					            </a>
					        </a>
					    </li>
					</sec:authorize>
					
					<sec:authorize access="!isAuthenticated()">
					    <!-- 로그아웃한 사용자에게만 보이는 콘텐츠 -->
               			<li class="mobile_delete">
               				<a href="${pageContext.request.contextPath}/user/joinForm">회원가입</a>
               			</li>
                  			<li class="login" data-logStatus="logout">
	                        <div class="userImg">
	                            <img src="${pageContext.request.contextPath}/resources/static/images/profile.png" alt="">
	                        </div>
	                        <a href="${pageContext.request.contextPath}/user/loginForm">로그인</a>
	                    </li>
					</sec:authorize>

                </ul>
            </div>
        </nav>
        <!-- nav -->
        <button class="sidebarBtn">
            <i class="fa-solid fa-bars"></i>
        </button>
    </header>   
	<!-- Header -->
    <!-- Container -->
    <div class="container">

        <div class="upDown-container">
            <button class="downBtn">
                <i class="fa-solid fa-chevron-down"></i>
            </button>
            <button class="upBtn">
                <i class="fa-solid fa-chevron-up"></i>
            </button>
            <button class="autoModeBtn" title="슬라이드 자동 전환">
                <i class="fa-solid fa-expand"></i>
            </button>
        </div>

        <div class="messageModal unstaged">
            <p>슬라이드 전환 방식을 변경하고 싶으면 '스페이스바'를 눌러주세요</p>
        </div>

        <div class="slide-container">
        	<div class="slide"></div>
        </div>
        
    </div>

    <!-- side-bar dim -->
    <div class="dim unstaged"></div>

    <!-- Side bar -->
    <aside class="sidebar">
        <div class="sidebar-header">
            <div class="nav-header">
                <div class="sidebar-logo logo">
                	<img src="${pageContext.request.contextPath}/resources/static/images/logo/logo2.png">
                </div>
            </div>
            <button type="button" class="close-btn">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>
        <div class="tags">
            <h2 class="subheading">감정 태그</h2>
            <div class="tag-box">
            
            
                <!-- <button class="emoTag">행복</button> -->
                <c:forEach var="emoVo" items="${emotionList }" varStatus="status">
                	<button id="${emoVo.emotionId }" class="emoTag">${emoVo.emotionName }</button>
                </c:forEach>
                
                
            </div>
        </div>
        <div class="randomBtn">
            <button class="random-btn">
            	<i class="fa-solid fa-shuffle"></i>
            </button>
            <p class="random-text">랜덤 서평 재생하기</p>
        </div>
        <div class="bgm">
            <h2 class="subheading">BGM</h2>
            <div class="bgm-container">
                <div class="bgm-info-box">
                    <h3 class="song-title">노래 제목</h3>
                    <p class="singer">가수 이름</p>
                </div>
                <div class="btnBoxes">
                    <button class="bgmBtn arrow prevArrow">
                        <i class="fa-solid fa-chevron-left"></i>
                    </button>
                    <button class="bgmBtn playBtn">
                        <i class="fa-solid fa-play"></i>
                    </button>
                    <button class="bgmBtn arrow nextArrow">
                        <i class="fa-solid fa-chevron-right"></i>
                    </button>
                </div>
                <div class="bgm-pagination"></div>
                <audio preload="auto" src="" class="audioEle"></audio>
            </div>
        </div>
        <div class="playlist">
            <div class="playlist-header">
                <h2 class="subheading">
                    My 플레이리스트
                </h2>
                <a href="${pageContext.request.contextPath}/${authUser.nickname}/like_playlist">
	           	    <button class="arrowMyPlaylist">
	                    <i class="fa-solid fa-arrow-right-long"></i>
	                </button>                
                </a>
            </div>
            <div class="playlist-container">
            	<!-- 
                <div class="static">
                    <p>새 플레이리스트 추가</p>
                    <button class="playlistAddBtn"><i class="fa-solid fa-plus"></i></button>
                </div>
                -->
                <ul class="playlist-list">
                	<c:choose>
                		<c:when test="${sessionScope.authUser eq null}">
                			<p class="logout-msg">로그인 후 이용해주세요</p>
                		</c:when>
                		<c:otherwise>
		                    <!-- 
		                    <li data-playlistno="30" class="playlistBtn">
		                    	<p>나른나른한 요즘 날씨에 틀어놓기 좋은 음악</p>
		                    </li>
		                    -->  
		                    
		                    <c:forEach var="playlistVo" items="${playlists }">
		                   		<li data-playlistno="${playlistVo.playlistId }" class="playlistBtn">
		                    		<p>${playlistVo.playlistName }</p>
		                    	</li>
		                    </c:forEach>  
		                         		
                		</c:otherwise>
                	</c:choose>
                </ul>
            </div>
        </div>
        <div class="help">
          	<a class="help-link" role="button" href="${pageContext.request.contextPath}/help">고객 센터</a>
       	</div>
    </aside>

    <!--  모달 (서평 플레이리스트에 추가) -->
    <div class="review-modal-background modal-background">
        <div class="addReviewModal">
        
            <div class="input-box">
                <p>My 플레이리스트</p>
                <button class="modal-closeBtn">
                    <i class="fa-solid fa-xmark"></i>
                </button>
            </div>
            
            <div class="playlist-box">
                <ul>
                	<!-- 
                    <li class="playlist-li" data-playlist-no="">
                        <p>플레이리스트 1</p>
                        <button class="reviewAddBtn"><i class="fa-solid fa-plus"></i></button>
                    </li>
                    -->
                </ul>
            </div>
            
            <form class="addPlaylist">
            	<input autocomplete='off' class="addPly-input" type="text" placeholder="새 플레이리스트 추가" name="playlistTitle" required>
            </form>
        </div>
        <div class="emotion-select-box">
        	<ul class="emotion-select-ul">
        		<!--
        		<li>
        			<div class="tag" data-emoNo="">감사한</div>
        		</li>
				-->
				
                <c:forEach var="emoVo" items="${emotionList }" varStatus="status">
                	<li>
                		<div class="tag" data-emoNo="${emoVo.emotionId }">${emoVo.emotionName }</div>
                	</li>
                </c:forEach>
                
        	</ul>
        	<button class="plySubmitBtn" type="submit">등 록</button>
        </div>
    </div>
</body>
</html>