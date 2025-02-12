package com.boggle.example.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.boggle.example.dto.user.LoginResponse;
import com.boggle.example.dto.user.UserForm;
import com.boggle.example.dto.user.UserIdResponse;
import com.boggle.example.dto.user.UserRequest;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.entity.UserPrincipal;
import com.boggle.example.exception.UserNotFoundException;
import com.boggle.example.repository.UserRepository;
import com.boggle.example.util.FileUploadUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	
	/* 수정 
	 * 
	 * 추가로 고려할 수 있는 개선사항:
		1. 비밀번호 유효성 검증 추가
		2. 파일 업로드 크기 제한 추가
		3. 지원하는 이미지 형식 검증
		4. 트랜잭션 격리 수준 설정
	 * */
	
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
   
    /*
     * 사용자 조회
     * findUserByNickname		특정 유저 조회
     * findUserByEmail			특정 유저 조회
     * 
     * 사용자 생성/수정
     * createUser				가입
     * updateUser				수정
     * 
     * 개별 정보 수정
     * updateNickname
     * updatePassword
     * updateProfileImage
     * updateUserPrincipal		authentication principal 갱신
     * 
     * 유효성 검사
     * validateNickname			닉네임 중복 검사
     * validatePassword			비밀번호 검사
     */
    
    /*
     * 사용자 조회
     * findUserByNickname		특정 유저 조회
     * findUserByEmail			특정 유저 조회
     */
    
	@Transactional(readOnly = true)
	public LoginResponse findUserByNickname(String nickname) {
		UserEntity userEntity = userRepository.findByNickname(nickname)
				.orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다. nickname: " + nickname));
		
		return LoginResponse.of(
				true, 
				userEntity.getUserId(), 
				userEntity.getUserName(), 
				userEntity.getNickname(), 
				userEntity.getUserProfile());
	}
    
    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
   /*
    * 사용자 생성/수정
    * createUser				가입
    * updateUser				수정
    */
    
	/* 가입 */
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE) 
	public UserIdResponse createUser(UserRequest userRequest) {
		log.info("UserService.join() - userName: {}", userRequest.getUserName());
		
        // 중복 이메일 검사
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
		
        // 비밀번호 검증      
        if (userRequest.getPassword() == null) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }
        
		UserEntity userEntity = UserEntity.of(
				userRequest.getUserName(), 
				userRequest.getNickname(), 
				userRequest.getEmail(), 
				passwordEncoder.encode(userRequest.getPassword()), 
				"profile.png"
				);
		
		userRepository.save(userEntity);
		return UserIdResponse.from(userEntity.getUserId());
	}
    
	@Transactional
	public UserEntity updateUser(Long authUserId, UserForm userForm) throws FileUploadException {
		System.out.println("UserService.modifyUser()");
		
		// 1. 엔티티 조회
        UserEntity user = userRepository.findById(authUserId)
        		.orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));
		
        // 2. 닉네임 업데이트
        updateNickname(user, userForm.getNickname());
        
        // 3. 비밀번호 업데이트
        updatePassword(user, userForm.getPassword());
        
        // 4. 프로필 이미지 업데이트
        updateProfileImage(user, userForm.getFile());
        
        // 5. 변경된 엔티티 저장
        return userRepository.save(user);
	}
	
	/*
	 * 개별 정보 수정
     * updateNickname
     * updatePassword
     * updateProfileImage
     * updateUserPrincipal		authentication principal 갱신
	 */
	   
    private void updateNickname(UserEntity user, String nickname) {
        if (StringUtils.hasText(nickname) && !"undefined".equals(nickname)) {
            user.setNickname(nickname);
        }
    }
    
    private void updatePassword(UserEntity user, String password) {
        if (StringUtils.hasText(password) && !"undefined".equals(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
    }
    
    private void updateProfileImage(UserEntity user, MultipartFile file) throws FileUploadException {
        if (file != null && !file.isEmpty()) {
            try {
//                userEntity.setUserProfile(file.getOriginalFilename());
//                FileUploadUtil.uploadUserProfile(file);
            	
                String fileName = file.getOriginalFilename();
                user.setUserProfile(fileName);
                FileUploadUtil.uploadUserProfile(file);
            } catch (Exception e) {
                throw new FileUploadException("프로필 이미지 업로드 실패: " + e.getMessage());
            }
        }
    }
    
    /* authentication principal 갱신 */
    public void updateUserPrincipal(UserPrincipal updatedUser, HttpServletRequest request) {
        // 1. SecurityContextHolder 갱신
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
            updatedUser,
            null,
            updatedUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        
        // 2. Session 갱신
        HttpSession session = request.getSession();
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        );
    }
    
    /*
     * 유효성 검사
     * validateNickname			닉네임 중복 검사
     * validatePassword			비밀번호 검사
     */
	
	/* 닉네임 중복 검사 */
	@Transactional(readOnly = true)
	public boolean validateNickname(String nickname, String nicknamePrincipal) {
		System.out.println("UserService.nickcheck()");
		
		return userRepository.existsByNicknameAndNicknameNot(nickname, nicknamePrincipal);
	}
	
	/* 비밀번호 확인 */
	public boolean validatePassword(String nickname, String passwordToCheck) {
		UserEntity authUserEntity = userRepository.findByNickname(nickname)
				.orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다. nickname: " + nickname));
		
		String encodedPasswordToCheck = passwordEncoder.encode(passwordToCheck);
		
		return passwordEncoder.matches(passwordToCheck, authUserEntity.getPassword());
	}	
}
