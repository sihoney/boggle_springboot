package com.boggle.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boggle.example.domain.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{

	ReviewEntity findByReviewId(Long reviewId);
	
	Page<ReviewEntity> findAllByUserId(Long userId, Pageable pageable);
	
	Page<ReviewEntity> findAllByUserIdAndContentContaining(Long userId, String query, Pageable pageable);

	Page<ReviewEntity> findAllByUserIdAndEmotionEntity_EmotionId(Long userId, Long emotionId, Pageable pageable);

	Page<ReviewEntity> findAllByBookEntity_isbn(Long isbn, Pageable pageable);

	@Query("SELECT r, count(ru) AS likeCount FROM review r " + 
				"LEFT JOIN review_user ru ON r.reviewId = ru.reviewId " + 
				"WHERE r.userId = ?1 " + 
				"GROUP BY r.reviewId")
	Page<Object[]> getAllReviewSortByLikeCount(Long userId, Pageable pageable);
	
	void deleteByUserIdAndReviewId(Long userId, Long reviewId);
	
//		       "LEFT JOIN review_user ru ON ru.reviewId = r.reviewId AND ru.userId = ?2 " +
//		       "GROUP BY r.reviewId"
//	@Query("SELECT r, u.nickname " +
//				"FROM review r " +
//		       "INNER JOIN r.review_playlist rp " +
//		       "INNER JOIN r.users u " + 
//		       "WHERE rp.playlistId = :playlistId")
//	Page<Object[]> getAllReviewByPlaylistId(Long playlistId, Pageable pageable);
}