const elements = {
    nicknameInput: document.getElementById('input_nickname'),
    newPasswordInput: document.getElementById('input_newPassword'),
    secondNewPasswordInput: document.getElementById('input_secondNewPassword'),
    fileInput: document.getElementById('btn_file'),
    previewImage: document.getElementById('user_icon'),
    modifyButton: document.getElementById('btn_modify'),
    checkDuplicateButton: document.getElementById('checkDuplicate'),
    nicknameCheckMessage: document.getElementById('id_check')
};

const state = {
    nickname: null,
    password: null,
    imageFile: null
};

document.addEventListener('DOMContentLoaded', () => {

    // 사용자 정보 수정 요청
    const editUser = async () => {
        const formData = new FormData();

        if (state.imageFile) {
            formData.append("file", state.imageFile);
        } 
        if (state.nickname) {
            formData.append('nickname', state.nickname);
        }
        if (state.password) {
            formData.append('password', state.password);
        }
		
        try {
            const response = await fetch('/users', {
                method: 'PUT',
                body: formData
            });

            if (response.ok) {
                // 성공 시 페이지 이동
                window.location.href = '/success';
            } else {
                throw new Error('수정 실패');
            }
        } catch (error) {
            console.error('서버 요청에 실패했습니다.', error);
            alert('사용자 정보 수정 중 오류가 발생했습니다.');
        }
    };

    // 새 비밀번호 일치 확인
    const validateNewPassword = () => {
        const newPw = elements.newPasswordInput.value;
        const confirmPw = elements.secondNewPasswordInput.value;
		
        if (!newPw || !confirmPw) {
            alert('비밀번호를 입력해주세요.');
            return false;
        }

        if (newPw !== confirmPw) {
            alert('새 비밀번호가 일치하지 않습니다.');
            return false;
        }

        state.password = confirmPw;
        return true;
    };

    // 파일 미리보기 및 상태 업데이트
    const handleFileUpload = (event) => {
        const file = event.target.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = (e) => {
            elements.previewImage.src = e.target.result;
            elements.previewImage.alt = "New Profile Image";
        };
        reader.readAsDataURL(file);

        state.imageFile = file;
    };

    // 닉네임 메시지 업데이트 헬퍼 함수
    const updateNicknameMessage = (message, color) => {
        elements.nicknameCheckMessage.textContent = message;
        elements.nicknameCheckMessage.style.color = color;
    };

    // 닉네임 중복 검사
    const checkNicknameDuplicate = async () => {
        const nickname = elements.nicknameInput.value.trim();
        
        if (!nickname) {
            updateNicknameMessage('닉네임을 입력해주세요.', 'red');
            return false;
        }

        try {
            const response = await fetch(`/users/nickname/exists?nickname=${nickname}`);
            const isDuplicate = await response.json();

            if (isDuplicate) {
                updateNicknameMessage('이미 사용중인 닉네임입니다.', 'red');
                state.nickname = null;
                return false;
            } else {
                updateNicknameMessage('사용 가능한 닉네임입니다.', 'blue');
                state.nickname = nickname;
                return true;
            }
        } catch (error) {
            updateNicknameMessage('서버 요청에 실패했습니다.', 'red');
            return false;
        }
    };

    // 이벤트 리스너 설정
    elements.checkDuplicateButton.addEventListener('click', checkNicknameDuplicate);
    elements.fileInput.addEventListener('change', handleFileUpload);
    elements.modifyButton.addEventListener('click', async (e) => {
        // 새 비밀번호 유효성 검사
        const isPasswordValid = elements.newPasswordInput.value 
			? validateNewPassword() 
			: true;
       
        if (isPasswordValid) {
            await editUser();
        }
    });
})