package com.boggle.example.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewUserEntity;

public interface ReviewUserRepository extends JpaRepository<ReviewUserEntity, Long>{

//	boolean existByUserIdAndReviewId(Long userId, Long reviewId);
//	boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
	boolean existsByUserIdAndReviewEntity(Long userId, ReviewEntity reviewEntity);
	
//	ReviewUserEntity findByUserIdAndReviewId(Long userId, Long reviewId);
	Optional<ReviewUserEntity> findByUserIdAndReviewEntity(Long userId, ReviewEntity reviewEntity);
	
	Page<ReviewUserEntity> findAllByUserId(Long userId, Pageable pageable);
	
//	Long deleteByUserIdAndReviewId(Long userId, Long reviewId);
	Long deleteByUserIdAndReviewEntity(Long userId, ReviewEntity reviewEntity);
	
//	Long deleteByUserIdAndReviewId(Long userId, Long reviewId);
	
//	@Query("SELECT COUNT(ru) FROM review_user ru WHERE ru.reviewId = ?1")
//	Integer getLikeCount(Long reviewId);
	
//	void deleteByReviewId(Long reviewId);
	void deleteByReviewEntity(ReviewEntity reviewEntity);

	Integer countByUserIdAndReviewEntity(Long authUserId, ReviewEntity rvEntity);
	
}
