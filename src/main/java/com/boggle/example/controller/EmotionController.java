package com.boggle.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boggle.example.entity.EmotionEntity;
import com.boggle.example.service.EmotionService;

@RestController
public class EmotionController {

	@Autowired
	private EmotionService emotionService;
	
	@GetMapping(path = "/emotions")
	public ResponseEntity<EmotionEntity> getEmotions() {
		
//		return ResponseEntity.ok();
		return new ResponseEntity(emotionService.getEmotions(), HttpStatus.OK);
	}
	
	@PostMapping(path = "/emotions/{emotionName}")
	public ResponseEntity<Long> createEmotion(@PathVariable String emotionName) {
	
		return new ResponseEntity(emotionService.createEmotion(emotionName).getEmotionId(), HttpStatus.OK);
	}
}
