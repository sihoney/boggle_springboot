<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/all_css.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/joinForm.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/bootstrap/css/bootstrap.css">
	<script src="${pageContext.request.contextPath}/resources/static/bootstrap/js/bootstrap.js"></script>
	<script src="${pageContext.request.contextPath}/resources/static/js/jquery-1.12.4.js"></script>
	<!-- <script src="${pageContext.request.contextPath}/asset/js/joinForm.js" defer></script> -->

</head>
<body>
	<div id="wrap">
		<!-- 헤더 -->
		<%-- <c:import url="/WEB-INF/views/include/header.jsp"></c:import> --%>
		<!-- content : join -->
		<div class="container">
			<div id="join-header" class="row">
				<div id="join-hello" class="col-xs-5">
					<div>나만의 서재를</div>
					<div>만들어 볼까요?</div>
				</div>
				<div class="col-xs-1">
					<div>또는</div>
				</div>
				<!-- 연동 -->
				<div id="join-api" class="col-xs-5">
					<div>
						<div>
							<%-- <img src="${pageContext.request.contextPath}/asset/img/facebooklogo.png"> --%> 페이스북으로 회원가입
						</div>
					</div>
					<div>
						<div>
							<%-- <img src="${pageContext.request.contextPath}/asset/img/naverlogo.png"> --%> 네이버로 회원가입
						</div>
					</div>
				</div>
				<div id="or" class="col-xs-1"></div>
			</div>
		</div>
		<!-- content : form -->
		<div class="container">
			<div id="join-content" class="row">
				<form action="${pageContext.request.contextPath}/join" method="post">
					<div>이름*</div>
					<div>
						<input type="text" id="userName" name="userName" placeholder="이름을 입력해주세요">
					</div>
					<div>닉네임(1~6자)*</div>
					<div class="except">
						<input type="text" maxlength='6' id="nickname" name="nickname" placeholder="닉네임을 입력해주세요" oninput="checkNickname(this.value)">
						<!--닉네임이 사용중일경우-->
                        <p class="nickname_already">이미 사용중인 닉네임입니다.</p>
					</div>
					<div>이메일주소*</div>
					<div class="except">
						<input type="text" id="email" name="email" placeholder="ID@example.com" oninput="">
						<!--메일주소가 사용중일경우-->
						<p class="email_already">이미 사용중이거나 탈퇴한 이메일 주소입니다.</p>
						<!--메일주소양식이 틀렸을경우-->
						<p class="email_check">이메일 주소를 다시 확인해주세요.</p>
					</div>
					<div>비밀번호*</div>
					<div>
						<input type="password" id="" name="password" placeholder="비밀번호를 입력해주세요">
					</div>
					<div>비밀번호 확인*</div>
					<div class="except">
						<input type="password" id="" name="re-pw" placeholder="비밀번호를 확인해주세요">
						<!--비밀번호가 일치하지 않을경우-->
						<p>비밀번호를 확인해주세요.</p>
					</div>
					<!-- 
                        <div id="genrebox">
                            <span>평소 어떤 책을 즐기는지(택1)</span>
                            <div id="love" class="box">두근두근하는</div>
                            <div id="spectacle" class="box">스펙타클한</div>
                            <div id="impressive" class="box">감동적인</div>
                            <div id="horor" class="box">소름끼치는</div>
                            <div id="calm" class="box">잔잔한</div>
                            <div id="encouraging" class="box">용기를 북돋는</div>
                            <div id="touching" class="box">눈물나는</div>
                            <div id="fantastic" class="box">환상적인</div>
                        </div>
                         -->
					<br>
					<button type="submit">회원가입</button>
				</form>
			</div>
		</div>
		<!-- footer -->
		<%-- <c:import url="/WEB-INF/views/include/footer.jsp"></c:import> --%>
	</div>
</body>
 
<script type="text/javascript">

function checkNickname(nickname){
	
    /* var nickname = $('#nickname').val(); //id값이 "nickname"인 값을 저장 */
    
    $.ajax({
        url:'/users/join/checkNickname?nickname=' + nickname, //Controller에서 인식할 주소
        type:'get', //POST 방식으로 전달
        success:function(response){  
        	/*
        	//1이면 이미 존재
             if(cnt == 1){//사용 불가능한 닉네임
                $('.nickname_already').css("display", "none");
            }       
            */
            if(response == true) {
            	$('.nickname_already').css("display", "block");
            } else {
            	$('.nickname_already').css("display", "none");
            }
        },
        error:function(){
        	console.log("error")
        }
    });
};
/*
function checkNickname(){
	
    var nickname = $('#nickname').val(); //id값이 "nickname"인 값을 저장
    
    $.ajax({
        url:'/user/nicknameCheck', //Controller에서 인식할 주소
        type:'post', //POST 방식으로 전달
        data:{nickname : nickname},
        success:function(cnt){        	
        	//1이면 이미 존재
            if(cnt == 1){//사용 불가능한 닉네임
                $('.nickname_already').css("display", "none");
            }        	
        },
        error:function(){
        	console.log("error")
        }
    });
};
*/
</script>
</html>