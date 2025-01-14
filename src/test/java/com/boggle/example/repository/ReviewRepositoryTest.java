package com.boggle.example.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.entity.ReviewEntity;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {

	@Autowired
	ReviewRepository reviewRepository;
	
	@Test
	public void test() {
		/*
		Long userId = Long.parseLong("2");
		Pageable pageable = PageRequest.of(
				1, 
				8, 
				Sort.by(Sort.Order.desc("likeCount")));
		
		Page<ReviewEntity> page = reviewRepository.getAllReviewSortByLikeCount(userId, pageable);
		
//		Assertions.assertNotNull(page);
		Assertions.assertEquals(2, page.getTotalPages());
		Assertions.assertEquals(9, page.getTotalElements());
		Assertions.assertEquals(ReviewEntity.class, page.getContent().get(0).getClass());
		*/
	}
	
//	@Test
//	public void findAllByEmotionIdTest() {
//		
//		Long userId = Long.parseLong("1");
//		Long emotionId = Long.parseLong("1");
//		Sort sort = Sort.by(Sort.Order.desc("createdAt"));
//		Pageable pageable = PageRequest.of(1, 8, sort);
//
//		Page<ReviewEntity> reviewPage = reviewRepository.findAllByUserIdAndEmotionEntity_EmotionId(userId, emotionId, pageable);
//		
//		Assertions.assertNotNull(reviewPage);
//		Assertions.assertEquals(ReviewEntity.class, reviewPage.getContent().get(0).getClass());
//	}
	
//	@Test
//	public void findAllByUserIdAndPageableTest() {
//		
//		//Given
//		Long userId = Long.parseLong("1");
//		int pageNumber = 1;
//		int pageSize = 8;
//		Pageable newPageable = PageRequest.of(pageNumber, pageSize);
//		
//		//When
//		Page<ReviewEntity> result = reviewRepository.findAllByUserId(userId, newPageable);
//		System.out.println(result);
//		
//		//Then
//		Assertions.assertNotNull(reviewRepository);
//	}
}
