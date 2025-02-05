<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 

<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/all_css.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/source.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loginForm.css" type="text/css">
    <link rel="stylesheet" href="https://hangeul.pstatic.net/hangeul_static/css/maru-buri.css">     
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.12.4.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>

	<%-- <link href="<c:url value='/custom-login' />" rel="stylesheet" /> --%>
</head>
<body>
    <div id="wrap">

        <!-- 헤더 -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import> 

        <div id="login_wrap">
            <div id="login_header">
               
                <h1 class="login_h1">책, 더이상 억지로 읽지 마세요 </h1>
                <h2 class="login_h2">'보글'로 가볍게 떠먹어요</h2>
            </div>

			<form action="${pageContext.request.contextPath}/login" method="post">
				<!-- ${pageContext.request.contextPath}/login -->
				<!-- ${pageContext.request.contextPath}/login -->
	            <div>	      

		            <c:if test="${result.result eq false}">
	                	<!-- 로그인 버튼 눌렀을 때 일치하지 않을 경우 -->
	                	<p class="font_blue_2 txt_ms">*이메일 주소와 비밀번호를 확인해주세요</p>
		            </c:if>  
		              
		            
                	<input type="text" id="email"  class="login_input"  name="username" value="" placeholder="이메일 주소를 입력해 주세요">
               		<input type="password" id="password" class="login_input" name="password" value="" placeholder="비밀번호를 입력해 주세요">
		                      
	                <button type="submit" class="btn btn-primary btn-lg btn-block blue-2 line-none btn-login">로그인</button>
	            </div>
	            <div>
	                <div id="login_find">
	                    <span>아이디찾기</span>
	                    <span class="line-l-r">비밀번호 찾기</span>
	                    <span><a href="${pageContext.request.contextPath}/users/register">회원가입</a></span>
	                </div>
	                <a href="/oauth2/authorization/google">
		                <button type="button" class="btn btn-primary btn-lg btn-block btn-naverlogin">
		                	구글로 로그인
		                </button>
	                </a>
	            </div>
	       	</form>
	       	
			<c:if test="${param.error != null}">
			    <p class="error-message">로그인에 실패했습니다. 다시 시도해주세요.</p>
			</c:if>	       	
        </div>
        
        <!-- footer -->
		<c:import url="/WEB-INF/views/include/footer.jsp"></c:import> 
    </div>
</body>
</html>