package com.boggle.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boggle.example.domain.EmotionEntity;
import com.boggle.example.repository.EmotionRepository;

@Service
public class EmotionService {

	@Autowired
	EmotionRepository emotionRepository;
	
	public List<EmotionEntity> getEmotions() {
		List<EmotionEntity> emotionList = emotionRepository.findAll();
		
		return emotionList;
	}
	
	public EmotionEntity createEmotion(String emotionName) {
		return emotionRepository.save(EmotionEntity.of(emotionName));
	}
	

}
