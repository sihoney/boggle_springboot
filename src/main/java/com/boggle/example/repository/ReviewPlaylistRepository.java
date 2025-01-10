package com.boggle.example.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.domain.ReviewPlaylistEntity;

public interface ReviewPlaylistRepository extends JpaRepository<ReviewPlaylistEntity, Long>{
	
//	void deleteByReviewId(Long reviewId);
	
	void deleteByReviewEntity(ReviewEntity reviewEntity);
	
//	void deleteByReviewIdAndPlaylistId(Long reviewId, Long playlistId);
	
//	void deleteByReviewEntityAndPlaylistId(ReviewEntity reviewEntity, Long playlistId);
	
	void deleteByReviewEntityAndPlaylistEntity(ReviewEntity reviewEntity, PlaylistEntity playlistEntity);
	
//	Page<ReviewPlaylistEntity> findAllByPlaylistId(Long playlistId, Pageable pageable);
	
	Page<ReviewPlaylistEntity> findAllByPlaylistEntity(PlaylistEntity playlistEntity, Pageable pageable);

	boolean existsByReviewEntityAndPlaylistEntity(ReviewEntity reviewEntity, PlaylistEntity playlistEntity);

	Optional<ReviewPlaylistEntity> findByReviewEntityAndPlaylistEntity(ReviewEntity reviewEntity, PlaylistEntity playlistEntity);
}
