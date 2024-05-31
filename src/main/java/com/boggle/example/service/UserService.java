package com.boggle.example.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.boggle.example.controller.LoginResponse;
import com.boggle.example.controller.UserIdResponse;
import com.boggle.example.controller.UserRequest;
import com.boggle.example.domain.UserEntity;
import com.boggle.example.repository.UserRepository;
import com.boggle.example.util.FileUploadUtil;

@Service
public class UserService {
		
//	@Autowired
	private final UserRepository userRepository;
//    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }    
    
//    public void saveUser(UserEntity userEntity) {
//        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
//        userRepository.save(userEntity);
//    }

    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
	/* 가입 */
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE) 
	public UserIdResponse join(UserRequest userRequest) {
		System.out.println("UserService.insert()");
		
		UserEntity userEntity = UserEntity.of(
				userRequest.getUserName(), 
				userRequest.getNickname(), 
				userRequest.getEmail(), 
				passwordEncoder.encode(userRequest.getPassword()), 
				userRequest.getUserProfile());
		
		userRepository.save(userEntity);
//		saveUser(userEntity);
		
		return UserIdResponse.from(userEntity.getUserId());
	}
	
	/* 닉네임 중복 체크 */
	@Transactional(readOnly = true)
	public boolean checkNickname(String nickname) {
		System.out.println("UserService.nickcheck()");
		
		return userRepository.existsByNickname(nickname);
	}
	
	/* 비밀번호 체크 */
	public boolean checkPassword(String nickname, String passwordToCheck) {
		UserEntity authUserEntity = userRepository.findByNickname(nickname);
//		System.out.println(authUserEntity.getPassword());
		
		String encodedPasswordToCheck = passwordEncoder.encode(passwordToCheck);
//		System.out.println(encodedPasswordToCheck);
		
		return passwordEncoder.matches(passwordToCheck, authUserEntity.getPassword());
	}
	
	/* 로그인 */
	@Transactional(readOnly = true)
	public LoginResponse login(String email, String password) {
		UserEntity userEntity = userRepository.findByEmailAndPassword(email, password);

		if(Objects.isNull(userEntity)) {
			return LoginResponse.of(false);
		} else {
			LoginResponse authUser = LoginResponse.of(true, 
													userEntity.getUserId(), 
													userEntity.getUserName(), 
													userEntity.getNickname(), 
													userEntity.getUserProfile());
			
			return authUser;
		}
	}
	
	@Transactional
	public UserEntity modifyUser(Long authUserId, MultipartFile file, String nickname, String password) {

        // 엔티티 조회
        Optional<UserEntity> optionalUser = userRepository.findById(authUserId);
        UserEntity updatedUserEntity = null;
        
        if (optionalUser.isPresent()) {
        	UserEntity userEntity = optionalUser.get();
        	
            // 엔티티 수정
        	if(!Objects.isNull(nickname) && !"".equals(nickname) && !"undefined".equals(nickname)) {
        		userEntity.setNickname(nickname);
        	}
        	
        	if(!Objects.isNull(password) && !"".equals(password) && !"undefined".equals(nickname)) {
//        		password = passwordEncoder.encode(password);
        		userEntity.setPassword(passwordEncoder.encode(password));
        	}
        	
        	if (!Objects.isNull(file)) {
        		userEntity.setUserProfile(file.getOriginalFilename());
            	
        		FileUploadUtil.uploadUserProfile(file);
            } 

            // 엔티티 저장 (업데이트)
            updatedUserEntity = userRepository.save(userEntity);
            return updatedUserEntity;
        } else {
        	return updatedUserEntity;
        }
	}
}
