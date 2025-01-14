package com.boggle.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.entity.MusicEntity;

public interface MusicRepository extends JpaRepository<MusicEntity, Long>{

	List<MusicEntity> findAllByEmotionId(Long emotionId);
}
