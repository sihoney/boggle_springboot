package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boggle.example.domain.ReviewUserEntity;

public interface ReviewUserRepository extends JpaRepository<ReviewUserEntity, Long>{

//	boolean existByUserIdAndReviewId(Long userId, Long reviewId);
	boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
	
	ReviewUserEntity findByUserIdAndReviewId(Long userId, Long reviewId);
	
	Long deleteByUserIdAndReviewId(Long userId, Long reviewId);
	
	@Query("SELECT COUNT(ru) FROM review_user ru WHERE ru.reviewId = ?1")
	Integer getLikeCount(Long reviewId);
	
	void deleteByReviewId(Long reviewId);
	
	
}
