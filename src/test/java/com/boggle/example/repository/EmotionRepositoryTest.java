package com.boggle.example.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.domain.EmotionEntity;

@SpringBootTest
@Transactional
@TestPropertySource(value = "classpath:application-test.properties")
public class EmotionRepositoryTest {

	@Autowired
	EmotionRepository emotionRepository;
	
	@Test
	public void findByEmotionName() {
		String emotionName = "바람";
		
		EmotionEntity emotionEntity = emotionRepository.findByEmotionName(emotionName);
		
		Assertions.assertNotNull(emotionEntity);
	}
}
