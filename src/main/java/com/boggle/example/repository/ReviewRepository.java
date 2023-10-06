package com.boggle.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boggle.example.domain.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{

	Page<ReviewEntity> findAllByUserId(Long userId, Pageable pageable);
	
	Page<ReviewEntity> findAllByUserIdAndEmotionEntity_EmotionId(Long userId, Long emotionId, Pageable pageable);

	Page<ReviewEntity> findAllByBookEntity_isbn(Long isbn, Pageable pageable);

	@Query("SELECT r, count(ru) AS likeCount FROM review r LEFT JOIN review_user ru ON r.reviewId = ru.reviewId WHERE r.userId = ?1 GROUP BY r.reviewId")
	Page<Object[]> getAllReviewSortByLikeCount(Long userId, Pageable pageable);
	
	void deleteByUserIdAndReviewId(Long userId, Long reviewId);
	
	@Query("SELECT r FROM review r LEFT JOIN review_playlist rp ON r.reviewId = rp.reviewId WHERE rp.playlistId = ?1")
	Page<ReviewEntity> getAllReviewByPlaylistId(Long userId, Pageable pageable);
}