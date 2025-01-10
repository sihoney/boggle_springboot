package com.boggle.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.domain.ReviewUserEntity;

public interface ReviewUserRepository extends JpaRepository<ReviewUserEntity, Long>{

//	boolean existByUserIdAndReviewId(Long userId, Long reviewId);
//	boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
	boolean existsByUserIdAndReviewEntity(Long userId, ReviewEntity reviewEntity);
	
//	ReviewUserEntity findByUserIdAndReviewId(Long userId, Long reviewId);
	ReviewUserEntity findByUserIdAndReviewEntity(Long userId, ReviewEntity reviewEntity);
	
//	Long deleteByUserIdAndReviewId(Long userId, Long reviewId);
	Long deleteByUserIdAndReviewEntity(Long userId, ReviewEntity reviewEntity);
	
//	@Query("SELECT COUNT(ru) FROM review_user ru WHERE ru.reviewId = ?1")
//	Integer getLikeCount(Long reviewId);
	
//	void deleteByReviewId(Long reviewId);
	void deleteByReviewEntity(ReviewEntity reviewEntity);
	
	Page<ReviewUserEntity> findAllByUserId(Long userId, Pageable pageable);

	Integer countByUserIdAndReviewEntity(Long authUserId, ReviewEntity rvEntity);
	
}
