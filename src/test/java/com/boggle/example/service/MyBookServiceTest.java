package com.boggle.example.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.entity.ReviewEntity;

@SpringBootTest
@Transactional
public class MyBookServiceTest {

	@Autowired
	MyBookService mybookService;
//	
//	@Test
//	public void testGetReviewsByUser() {
//		//Given
//		String nickname = "강잉뉴";
//		int page = 1;
//		int size = 4;
//		
//		//When
//		Page<ReviewEntity> pageObj = mybookService.getReviewsByUser(nickname, PageRequest.of(page, size));
//		
//		//Then
//		Assertions.assertNotNull(pageObj);
//	}
}
