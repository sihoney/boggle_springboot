package com.boggle.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Entity(name = "emotion")
@Table(name = "emotion")
public class EmotionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emotion_id")
	private Long emotionId;
	
	@Column(name = "emotion_name")
	private String emotionName;
	
	public static EmotionEntity of(String emotionName) {
		EmotionEntity emotionEntity = new EmotionEntity();
		
		emotionEntity.emotionName = emotionName;
		
		return emotionEntity;
	}
	
	public static EmotionEntity of(Long emotionId) {
		EmotionEntity emotionEntity = new EmotionEntity();
		
		emotionEntity.emotionId = emotionId;
		
		return emotionEntity;
	}
}
