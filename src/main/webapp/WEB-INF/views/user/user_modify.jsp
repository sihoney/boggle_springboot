<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
	<title>회원정보수정</title>
	
    <script src="${pageContext.request.contextPath}/resources/static/js/jquery-1.12.4.js"></script>
    <script src="${pageContext.request.contextPath}/resources/static/bootstrap/js/bootstrap.js"></script>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/source.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/all_css.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/static/css/user_modify.css">
</head>

<body>
    <div id="wrap">
        <!-- 헤더 -->
		<c:import url="/WEB-INF/views/include/header.jsp"></c:import>
		
		<%-- <form action="${pageContext.request.contextPath }/modifyUser" method="post"> --%>
	        <!-- 상단 프로필 영역 -->
	        <div id="user_pro" >
	            <div id="user_modify_head">
	                <div id="user_mdfy_h1" class="line">
	                    <h1 class="title-font-color txt-mm margin-b-15">'${authUser.userName }'님의 프로필</h1>
	                </div>
	            </div>    
	
	            <div id="user_modify_head1" class="clearfix padding-20 gray-ss">
	                <div class="col-md-12 txt-b txt_ms title-font-color">
	                    <!-- 기본 프로필 사진 -->
	                    <div class="float-l">
	                        <label className="input-file-button" for="btn_file">
	                            <img id="user_icon" src="${pageContext.request.contextPath}/resources/static/images/user_profile/${authUser.userProfile}">
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
	                        <h2>${authUser.userName }</h2>
	                        <h2 class="txt-b">${authUser.nickname }</h2>
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
	                    <div>
	                        <span id="modify_nickname">닉네임</span>
	                        <!-- 사용자 닉네임이 존재할 때 -->
	                        <span id="id_check"></span>
	                        <!-- 사용자 닉네임이 존재할 때 -->
	                        <input id="input_nickname" type="text" class="input-box" name="nickname" value="${authUser.nickname }" placeholder="닉네임을 입력하세요">
	                    	<button id="checkDuplicate">중복 확인</button>
	                    </div>
	                    <div>
	                        <span id="modify_pw">비밀번호 변경</span>
	                        <!-- 사용중인 비밀번호 일치하지 않을 때 -->
	                        <span id="pw_check"></span>
	                        <!-- 변경한 비밀번호 일치하지 않을 때
	                        <span id="pw_check">*변경된 비밀번호가 일치하지 않습니다.</span> -->
	                        <input id="input_crtPassword" type="text" class="input-box" name="" value="" placeholder="사용중인 비밀번호를 입력하세요">
	                        <button id="checkPassword">확인</button>
	                    </div>
	
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
		 <!-- </form> -->

        <!-- footer -->
		<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>

    </div>  
</body>

<script>

	let NICKNAME = null;
	let PASSWORD
	let IMAGE_FILE
	
	let	CHECK_CRT_PASSWORD = false
	let CHECK_NICKNAME = false

	$(document).ready(function() {
		
		$('#btn_modify').click(function(e){
			//e.preventDefault();
			
			var newPw = $('#input_newPassword').val()
			var scNewPw = $('#input_secondNewPassword').val()

			if(CHECK_CRT_PASSWORD && newPw !== null && scNewPw !== null && newPw === scNewPw) {
				PASSWORD = scNewPw
				
			} else if(CHECK_CRT_PASSWORD && newPw !== scNewPw) {
				alert("새 비밀번호가 동일하지 않습니다.");
			} else if(CHECK_CRT_PASSWORD === false && newPw === scNewPw) {
				alert("현재 비밀번호를 확인해주세요")
			}
			
			var formData = new FormData();

		    formData.append('image-file', IMAGE_FILE); // FormData에 파일 추가
		    formData.append('nickname', NICKNAME);
		    formData.append('password', PASSWORD);
		    
		    $.ajax({
		        url: '/modifyUser', // 서버의 엔드포인트 URL로 변경
		        method: 'POST', // HTTP 메서드를 필요에 따라 변경
		        processData: false, // FormData를 처리하지 않도록 설정
		        contentType: false, // 컨텐츠 유형을 설정하지 않도록 설정
		        data: formData,
		        success: function(response) {
		          console.log('서버 응답:', response);
		          
		          window.location.href = '/' + response;
		        },
		        error: function() {
		          console.log('서버 요청에 실패했습니다.');
		        }
	        });
		})
		
		
		$('#checkPassword').click(function(e) {
			e.preventDefault();
			
			var inputValue = $('#input_crtPassword').val();
			var obj = { password: inputValue };
			
	        $.ajax({
	            url: '/check-password', // 서버의 처리 URL로 변경해야 합니다.
	            method: 'POST', // HTTP 메서드를 사용자의 요구에 따라 변경해야 합니다.
				headers : {
					'Content-Type': 'application/json'
				},
	            data: JSON.stringify(obj), // 서버에 전송할 데이터를 설정합니다.
	            success: function(response) {
	            	
	                // 서버로부터 받은 응답을 처리합니다.
	                if (response) {
	                	$('#pw_check').html('일치합니다.');
	                	
	                	CHECK_CRT_PASSWORD = true
	                } else {
	                	$('#pw_check').html('*사용중인 비밀번호가 일치하지 않습니다.');
	                	
	                	CHECK_CRT_PASSWORD = false
	                }
	            },
	            error: function() {
	                // 요청에 실패한 경우 처리합니다.
	                $('#pw_check').html('서버 요청에 실패했습니다.');
	            }
	        });			
		})	
			
		$('#checkDuplicate').click(function(e) {
			e.preventDefault();
			
			var inputValue = $('#input_nickname').val();
			var obj = { nickname: inputValue }
			
	        $.ajax({
	            url: '/check-duplicate', // 서버의 처리 URL로 변경해야 합니다.
	            method: 'POST', // HTTP 메서드를 사용자의 요구에 따라 변경해야 합니다.
				headers : {
					'Content-Type': 'application/json'
				},
	            data: JSON.stringify(obj), // 서버에 전송할 데이터를 설정합니다.
	            success: function(response) {
	            	
	                // 서버로부터 받은 응답을 처리합니다.
	                if (response) {
	                    $('#id_check').html('*이미 사용중인 닉네임입니다.');
	                    
	                    NICKNAME = null;
	                } else {
	                    $('#id_check').html('이 값은 사용 가능합니다.');
	                    
	                    NICKNAME = inputValue;
	                }
	            },
	            error: function() {
	                // 요청에 실패한 경우 처리합니다.
	                $('#id_check').html('서버 요청에 실패했습니다.');
	            }
	        });
		})
		
		
	  // 파일 선택(input type="file") 요소에 change 이벤트 핸들러 추가
	  $('#btn_file').on('change', function(e) {
	    // 선택된 파일 객체에 접근
	    var fileInput = this;
	    var previewImage = $('#user_icon');
	    
	    if (fileInput.files && fileInput.files[0]) {
	    	var reader = new FileReader();
	    	
	    	reader.onload = function(e) {
	    		// 파일을 Data URL로 읽어와서 이미지 태그의 src 속성에 할당
		        previewImage.attr("src", e.target.result);
		        previewImage.attr("alt", "New Image"); // 이미지 대체 텍스트 변경 (선택 사항)
	    	}
	        
            // 파일을 Data URL로 읽어옴
            reader.readAsDataURL(fileInput.files[0]);
            
            IMAGE_FILE = fileInput.files[0];
	    }
	  });
	});

</script>

</html>