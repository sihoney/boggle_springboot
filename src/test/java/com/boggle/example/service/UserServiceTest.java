package com.boggle.example.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.controller.LoginResponse;
import com.boggle.example.controller.UserIdResponse;
import com.boggle.example.controller.UserRequest;
import com.boggle.example.domain.UserEntity;
import com.boggle.example.repository.UserRepository;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {

	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void testJoin() { 
		
		//Given
		UserRequest request = new UserRequest();
		request.setUserName("test_name");
		request.setNickname("test_nickname");
		request.setEmail("test_email");
		request.setPassword("1234");
		request.setUserProfile("test_location");
		
		//When
		UserIdResponse userIdResponse = userService.join(request);
		UserEntity userEntity = userRepository.findById(userIdResponse.getUserId()).orElse(null);
		
		//Then
		Assertions.assertNotNull(userEntity);
		Assertions.assertEquals(request.getUserName(), userEntity.getUserName());
		Assertions.assertEquals(request.getNickname(), userEntity.getNickname());
		Assertions.assertEquals(request.getEmail(), userEntity.getEmail());
		Assertions.assertEquals(request.getPassword(), userEntity.getPassword());
		Assertions.assertEquals(request.getUserProfile(), userEntity.getUserProfile());
	}
	
	@Test 
	public void testCheckNickname() {
		
		//Given
		UserRequest request = new UserRequest();
		request.setUserName("test_name");
		request.setNickname("test_nickname");
		request.setEmail("test_email");
		request.setPassword("1234");
		request.setUserProfile("test_location");
		UserIdResponse userIdResponse = userService.join(request);
		
		//When
		boolean result1 = userService.checkNickname(request.getNickname());
		boolean result2 = userService.checkNickname("test");
		
		//Then
		Assertions.assertNotNull(result1);
		Assertions.assertTrue(result1);
		Assertions.assertFalse(result2);
	}
	
	@Test 
	public void testLogin() {
		
		//Given
		UserRequest request = new UserRequest();
		request.setUserName("test_name");
		request.setNickname("test_nickname");
		request.setEmail("test_email");
		request.setPassword("1234");
		request.setUserProfile("test_location");
		UserIdResponse userIdResponse = userService.join(request);
		
		//When
		LoginResponse userEntity = userService.login("test_email", "1234");
		
		//Then
		Assertions.assertNotNull(userEntity);
		Assertions.assertEquals(request.getUserName(), userEntity.getUserName());
		Assertions.assertEquals(request.getNickname(), userEntity.getNickname());
		Assertions.assertEquals(request.getUserProfile(), userEntity.getUserProfile());
	}
}
