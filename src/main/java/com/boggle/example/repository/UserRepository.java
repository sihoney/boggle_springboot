package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

// 로그인 시 이메일과 비밀번호와 일치하는 데이터 조회
	UserEntity findByEmailAndPassword(String email, String password);
	
//	닉네임 중복 체크
	boolean existsByNickname(String nickname);

	UserEntity findByNickname(String nickname);
}
