package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.ReviewPlaylistEntity;

public interface ReviewPlaylistRepository extends JpaRepository<ReviewPlaylistEntity, Long>{
	
	void deleteByReviewId(Long reviewId);
}
