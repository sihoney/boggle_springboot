const elements = {
    boxes: document.getElementsByClassName("box"),
    btnSignup: document.getElementById("btn_signup"),
    inputName: document.getElementById("userName"),
    inputNickname: document.getElementById("nickname"),
    inputEmail: document.getElementById("email"),
    inputPassword: document.getElementById("input_pwd"),
    inputPasswordConfirm: document.getElementById("input_pwd_double"),
    warningEmpty: document.getElementById("warning_empty"),
    warningNicknameAlready: document.getElementById("warning_nickname_already")
};

document.addEventListener('DOMContentLoaded', () => {
	
    const validateInput = () => {
        const inputs = [
            elements.inputName,
            elements.inputNickname, 
            elements.inputEmail,
            elements.inputPassword,
            elements.inputPasswordConfirm
        ];

        return inputs.every(input => 
            input.value !== null && input.value.trim() !== ""
        );
    };

    const checkNickname = async (nickname) => {
        try {
            const response = await fetch(`/users/nickname/exists?nickname=${nickname}`);
            const isDuplicate = await response.json();

            elements.warningNicknameAlready.style.display = 
                isDuplicate ? "block" : "none";

            return !isDuplicate;
        } catch (error) {
            console.error("닉네임 중복 확인 중 오류:", error);
            return false;
        }
    };

    const handleBoxClick = (event) => {
        const clickedBox = event.target;
        
        // Array.from으로 HTMLCollection을 배열로 변환
        Array.from(elements.boxes).forEach(box => 
            box.classList.remove("clicked")
        );

        clickedBox.classList.toggle("clicked");
    };

    const signup = async () => {
        const userData = {
            userName: elements.inputName.value,
            nickname: elements.inputNickname.value,
            email: elements.inputEmail.value,
            password: elements.inputPassword.value
        };

        try {
            const response = await fetch('/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            return response;
        } catch (error) {
            console.error("회원가입 요청 실패:", error);
            throw error;
        }
    };

    const handleSignupSubmit = async (e) => {
        e.preventDefault();

        // 비밀번호 일치 확인 추가
        if (elements.inputPassword.value !== elements.inputPasswordConfirm.value) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        if (!validateInput()) {
            elements.warningEmpty.style.display = "block";
            return;
        }

        try {
            const isNicknameAvailable = await checkNickname(elements.inputNickname.value);
            
            if (!isNicknameAvailable) {
                alert("이미 사용 중인 닉네임입니다.");
                return;
            }

            await signup();
            window.location.replace('/success');
        } catch (error) {
            window.location.replace('/failure');
        }
    };

    const init = () => {
        // Array.from으로 HTMLCollection을 배열로 변환하여 이벤트 추가
        Array.from(elements.boxes).forEach(box => 
            box.addEventListener("click", handleBoxClick)
        );

        elements.btnSignup.addEventListener("click", handleSignupSubmit);
    };

    init();
})
/*
let box = document.getElementsByClassName("box");
let btn_signup = document.getElementById("btn_signup");
let input_name = document.getElementById("userName");
let input_nickname = document.getElementById("nickname");
let input_email = document.getElementById("email");
let input_pwd = document.getElementById("input_pwd");
let input_pwd_double = document.getElementById("input_pwd_double");


function checkNickname(nickname){

    $.ajax({
        url:'/users/nickname/exists?nickname=' + nickname, //Controller에서 인식할 주소
        type:'get', //POST 방식으로 전달
        success:function(response){ 
			 console.log(response);
        	
        	//1이면 이미 존재
            // if(cnt == 1){//사용 불가능한 닉네임
            //    $('.nickname_already').css("display", "none");
            //}       
            
            if(response == true) {
            	$('#warning_nickname_already').css("display", "block");
            } else {
            	$('#warning_nickname_already').css("display", "none");
            }
        },
        error:function(){
        	console.log("error")
        }
    });
};

function handleClick(event) {
  console.log(event.target);
  // console.log(this);
  // 콘솔창을 보면 둘다 동일한 값이 나온다

  console.log(event.target.classList);

  if (event.target.classList[1] === "clicked") {
    event.target.classList.remove("clicked");
  } else {
    for (var i = 0; i < box.length; i++) {
      box[i].classList.remove("clicked");
    }

    event.target.classList.add("clicked");
  }
}

async function signup() {
	
	let obj = {
		userName: input_name.value,
		nickname: input_nickname.value,
		email: input_email.value,
		password: input_pwd.value
	};
	
	try {
		let response = await fetch(`/users`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(obj)
		})
		
		return response;
	} catch(error) {
		console.log("Failed to fetch: " + error)
	}
}

function clickSignupBtn(e) {
	e.preventDefault();
	
	console.log(e);
	
	if(input_nickname.value == null || input_nickname.value == "" ||
	input_name.value == null || input_name.value =="" ||
	input_email.value == null || input_email == "" ||
	input_pwd.value == null || input_pwd.value == "" ||
	input_pwd_double.value == null || input_pwd_double.value == "") {
		
		document.getElementById("warning_empty").style.display = "block";
	} else {
		signup().then(response => {
			window.location.replace('/success');
		}).catch(error => {
			window.location.replace('/failure');
		})
	}
}

function init() {
  for (var i = 0; i < box.length; i++) {
    box[i].addEventListener("click", handleClick);
  }

	btn_signup.addEventListener("click", clickSignupBtn);
}

init();
*/