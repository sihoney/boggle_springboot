package com.boggle.example.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
//@TestPropertySource(value = "classpath:application-test.properties")
public class CustomUserDetailsServiceTest {

	@Autowired
	private CustomUserDetailsService service;
	
	@Test
	public void loadUserByUsernameTest() {
		
		String email = "kang@naver.com";
		String password = "1234";
		
		UserDetails userDetail = service.loadUserByUsername(email);
		
		Assertions.assertEquals(userDetail.getUsername(), "이강인");
		Assertions.assertEquals(userDetail.getPassword(), password);
	}
}
