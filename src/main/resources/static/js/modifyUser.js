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
	
	// 기존 비밀번호 일치 확인
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
                	$('#pw_check').html('*일치합니다.');
					$('#pw_check').css("color", "blue")                 	

                	CHECK_CRT_PASSWORD = true
                } else {
                	$('#pw_check').html('*사용중인 비밀번호가 일치하지 않습니다.');
					$('#pw_check').css("color", "red")                	

                	CHECK_CRT_PASSWORD = false
                }
            },
            error: function() {
                // 요청에 실패한 경우 처리합니다.
                $('#pw_check').html('서버 요청에 실패했습니다.');
            }
        });			
	})	
		
	// 닉네임 중복 검사
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
                    $('#id_check').html('* 이미 사용중인 닉네임입니다.');
					$('#id_check').css("color", "red")                     

                    NICKNAME = null;
                } else {
                    $('#id_check').html('* 이 값은 사용 가능합니다.');
					$('#id_check').css("color", "blue") 			
                    
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
