<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
	<title>회원정보수정</title>
	
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/source.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/all_css.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user_modify.css">	
	
    <script src="${pageContext.request.contextPath}/js/jquery-1.12.4.js"></script>
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/js/modifyUser.js" defer></script>
</head>
<body>
    <div id="wrap">
    	<sec:authentication var="auth" property="principal" />
    
        <!-- 헤더 -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>	
	        <!-- 상단 프로필 영역 -->
	        <div id="user_pro" >
	            <div id="user_modify_head">
	                <div id="user_mdfy_h1" class="line">
	                    <h1 class="title-font-color txt-mm margin-b-15">'${auth.nickname }'님의 프로필</h1>
	                </div>
	            </div>    
	
	            <div id="user_modify_head1" class="clearfix padding-20 gray-ss">
	                <div class="col-md-12 txt-b txt_ms title-font-color">
	                    <!-- 기본 프로필 사진 -->
	                    <div class="float-l">
	                        <label className="input-file-button" for="btn_file">
	                            <img id="user_icon" src="${auth.userProfile}" onerror="this.src='${pageContext.request.contextPath}/images/user_profile/profile.png'" alt="유저 이미지">
	                        </label>
	                        <input type="file" id="btn_file" name="userProfile" value="" style="display: none">
	                    </div>
	
	                    <!-- 사용자가 변경한 프로필 사진 -->
	                    <!-- <div class="float-l">
	                        <label className="input-file-button" for="btn_file">
	                            <img id="user_icon" src="../img/login/user.jpg" alt="..." class="img-circle">
	                        </label>
	                        <input type="file" id="btn_file" name="" value="" style="display: none">
	                    </div> -->
	                    <!-- 사용자가 변경한 프로필 사진 -->
	
	                    <div id="user_md_txt" class="float-l margin-30">
	                        <%-- <h2>${authUser.userName }</h2> --%>
	                        <h2 class="txt-b">${auth.username }</h2>
	                    </div>
	                </div>
	            </div>
	        </div>
	        <!-- 상단 프로필 영역 -->
	
	        <!-- 회원가입 수정 컨텐츠 -->
	        <div id="user_modify_content" >
	                <div id="modify_content_head">
	                    <h1 class="title-font-color txt-mm margin-b-15">회원정보수정</h1>
	                </div>    
	
	                <div id="modify_content_body" class="">
	                	<!-- 새 비밀번호 확인 -->
	                    <div>
	                        <h2 id="modify_nickname">닉네임 변경</h2>
	                        <span id="id_check"></span>
	                        <input type="text" pattern="[A-Za-z0-9]" id="input_nickname" class="input-box" name="nickname" value="${auth.nickname }" placeholder="닉네임을 입력하세요">
	                    	<button id="checkDuplicate">닉네임 중복 확인</button>
	                    </div>
	                    <!-- 새 비밀번호 확인 -->
	                    <!--
	                    <div>
	                        <h2 id="modify_pw">현재 비밀번호 입력</h2>
	                        <span id="pw_check"></span>
	                        <input id="input_crtPassword" type="text" class="input-box" name="" value="" placeholder="사용중인 비밀번호를 입력하세요">
	                        <button id="checkPassword">현재 비밀전호 확인</button>
	                    </div>
	                    -->
	                    <!-- 새 비밀번호 확인 -->
	                    <div id="pw_check_box">        
	                        <div id="user_pw_check">
	                            <input id="input_newPassword" type="text" class="input-box" name="" value="" placeholder="변경할 비밀번호를 입력하세요">
	                            <input id="input_secondNewPassword" type="text" class="input-box" name="" value="" placeholder="비밀번호 확인">
	                        </div>
	                    </div>
	                </div>
	         </div>
             <button id="btn_modify" type="button" class="btn btn-primary btn-lg btn-block blue-2">회원 정보 수정</button>
        	<!-- 회원가입 수정 컨텐츠 -->

        <!-- footer -->
		<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>

    </div>  
</body>
</html>