package com.boggle.example.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.domain.UserEntity;

@SpringBootTest
@Transactional
//@TestPropertySource(value = "classpath:application-test.properties")
public class UserRepositoryTest {

	private static UserEntity testUserEntity;
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void testFindByNickname() {
		UserEntity userEntity = userRepository.findByNickname("강잉뉴");
		
		Assertions.assertNotNull(userEntity);
	}
	
//	@BeforeEach
//	public void init() {
//		//String userName, String nickname, String email, String password, String userProfile
//		testUserEntity = UserEntity.of("lee mark", "mork", "mork99@gmail.com", "1234", "");
//	}
//	
//	@Test
//	public void testFindByEmailAndPassword() {
//		
//		//Given
//		userRepository.save(testUserEntity);
//		
//		//When
//		UserEntity userEntity = userRepository.findByEmailAndPassword("mork99@gmail.com", "1234");
//		System.out.println(userEntity);
////		UserEntity(userId=2, userName=lee mark, nickname=mork, email=mork99@gmail.com, password=1234, userProfile=, joinDate=null)
//		
//		//Then
//		Assertions.assertNotNull(userEntity);
//		Assertions.assertEquals(testUserEntity.getUserName(), userEntity.getUserName());
//		Assertions.assertEquals(testUserEntity.getNickname(), userEntity.getNickname());
//		Assertions.assertEquals(testUserEntity.getEmail(), userEntity.getEmail());
//		Assertions.assertEquals(testUserEntity.getPassword(), userEntity.getPassword());
//		Assertions.assertEquals(testUserEntity.getUserProfile(), userEntity.getUserProfile());
//	}
//	
//	@Test
//	public void testExistByNickname() {
//		
//		//Given
//		userRepository.save(testUserEntity);
//		
//		//When
//		boolean isNicknameExist = userRepository.existsByNickname("mork");
//		System.out.println(isNicknameExist);
////		true
//		
//		//Then
//		Assertions.assertEquals(true, isNicknameExist);
//	}
	
}
