package com.boggle.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.entity.EmotionEntity;

public interface EmotionRepository extends JpaRepository<EmotionEntity, Long> {

	Optional<EmotionEntity> findById(Long emotionId);
	
	EmotionEntity findByEmotionName(String emotionName);
}
