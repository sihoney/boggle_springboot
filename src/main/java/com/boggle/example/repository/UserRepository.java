package com.boggle.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

// 로그인 시 이메일과 비밀번호와 일치하는 데이터 조회
	UserEntity findByEmailAndPassword(String email, String password);
	
	UserEntity findByNickname(String nickname);
	
	Optional<UserEntity> findByEmail(String email);
	
//	닉네임 중복 체크
	boolean existsByNickname(String nickname);
	
	boolean existsByNicknameAndNicknameNot(String email, String status);
	
	boolean existsByEmail(String email);
}
