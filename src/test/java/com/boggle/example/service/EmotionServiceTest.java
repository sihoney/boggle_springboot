package com.boggle.example.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.domain.EmotionEntity;
import com.boggle.example.repository.EmotionRepository;

@SpringBootTest
@Transactional
@Rollback
@TestPropertySource(value = "classpath:application-test.properties")
public class EmotionServiceTest {

	@Autowired
	private EmotionService emotionService;
	@Autowired
	private EmotionRepository emotionRepository;
	
	@Test
	public void createEmotionTest() {
//		//Given
//		String emotionName = "희망찬";
//		
//		//When
//		EmotionEntity emotionEntity = emotionService.createEmotion(emotionName);
//		
//		//Then
////		EmotionEntity emotionEntity = emotionRepository.findByEmotionName(emotionName);
//		
//		Assertions.assertNotNull(emotionEntity);
//		Assertions.assertEquals(emotionName, emotionEntity.getEmotionName());
	}
	
	@Test
	public void getEmotionsTest() {
//		//Given
//		EmotionEntity emotionEntity01 = emotionService.createEmotion("희망찬");
//		EmotionEntity emotionEntity02 = emotionService.createEmotion("슬픈");
//		EmotionEntity emotionEntity03 = emotionService.createEmotion("우울한");
//		
//		//When
//		List<EmotionEntity> emotionList = emotionService.getEmotions();
//		
//		//Then
//		Assertions.assertNotNull(emotionList);
	}
}
