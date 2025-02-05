package com.boggle.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.dto.user.LoginResponse;
import com.boggle.example.entity.EmotionEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewUserEntity;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.repository.EmotionRepository;
import com.boggle.example.repository.ReviewPlaylistRepository;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.ReviewUserRepository;
import com.boggle.example.repository.UserRepository;

@Service
public class MyBookService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	EmotionRepository emotionRepository;
	@Autowired
	ReviewUserRepository rvUsRepository;
	@Autowired
	ReviewPlaylistRepository reviewPlaylistRepository;
	
/*
	getUserProfile				유저 정보(개인 페이지용)
	getReviewsByCreatedAt		최신순, 오래된순
	getReviewsOrderByLikeCount	인기순
	getReviewByEmotion			감정순
	likeOrDislikeReview			서평 좋아요 & 좋아요 취소
	deleteReview				서평 삭제
	
 */
	
	@Transactional(readOnly = true)
	public LoginResponse getUserProfile(String nickname) {
		UserEntity userEntity = userRepository.findByNickname(nickname);
		
		return LoginResponse.of(
				true, 
				userEntity.getUserId(), 
				userEntity.getUserName(), 
				userEntity.getNickname(), 
				userEntity.getUserProfile());
	}
	
//	최신순, 오래된순
	@Transactional(readOnly = true)
	public Page<ReviewEntity> getReviewsByCreatedAt(String nickname, Long userId, Pageable pageable, Long authUserId) {
		
		if( !Objects.isNull(nickname) ) {
			userId = userRepository.findByNickname(nickname).getUserId();
		}
		
		Page<ReviewEntity> reviewList = reviewRepository.findAllByUserId(userId, pageable);
		reviewList.getContent().stream().forEach(reviewEntity -> {
//			reviewEntity.setLikeCount(reviewEntity.getReviewUserEntityList().size()); //rvUsRepository.getLikeCount(reviewEntity.getReviewId())
			reviewEntity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, reviewEntity));
			reviewEntity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, reviewEntity));
		});

		return reviewList;
	}
	
//	인기순
	@Transactional
	public Page<ReviewEntity> getReviewsOrderByLikeCount(Long userId, Long authUserId, Pageable pageable) {
		
        Page<ReviewEntity> page = reviewRepository.findAllByUserId(userId, PageRequest.of(pageable.getPageNumber(),
        																					pageable.getPageSize()));
		List<ReviewEntity> list = page.getContent().stream().map(entity -> {
//			ReviewEntity reviewEntity = entity.getReviewEntity();
			
//			entity.setLikeCount(entity.getReviewUserEntityList().size());
			entity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, entity));
			entity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, entity));
						
			return entity;
		}).collect(Collectors.toList());
		
		List<ReviewEntity> sortedEntities = new ArrayList<>(list);
		sortedEntities.sort((entity1, entity2) -> {
			return entity2.getLikeCount() - entity1.getLikeCount();
		});
		
		System.out.println(page);
		System.out.println(sortedEntities);
		
		return new PageImpl<>(sortedEntities, page.getPageable(), page.getTotalElements());
	}

//	감정순
	@Transactional(readOnly = true)
	public Page<ReviewEntity> getReviewByEmotion(Long userId, String emotionName, Pageable pageable, Long authUserId) {
		System.out.println("MyBookService.getReviewByEmotion()");
		
		EmotionEntity emotionEntity = emotionRepository.findByEmotionName(emotionName);
		
		Page<ReviewEntity> reviewList = reviewRepository.findAllByUserIdAndEmotionEntity_EmotionId(
			userId,
			emotionEntity.getEmotionId(),
			pageable
		);
		reviewList.getContent().stream().forEach(reviewEntity -> {
//			reviewEntity.setLikeCount(reviewEntity.getReviewUserEntityList().size()); //rvUsRepository.getLikeCount(reviewEntity.getReviewId())
			reviewEntity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, reviewEntity));
			reviewEntity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, reviewEntity));
		});
		
		return reviewList;
	}
	
//	서평 좋아요 & 취소
	@Transactional
	public Integer likeOrDislikeReview(Long userId, Long reviewId) {

	    ReviewEntity reviewEntity = reviewRepository.findByReviewId(reviewId);

	    if (reviewEntity != null) {
	        ReviewUserEntity existingEntity = rvUsRepository.findByUserIdAndReviewEntity(userId, reviewEntity);
	        
	        if (existingEntity != null) {
	            System.out.println("삭제");
	            rvUsRepository.delete(existingEntity);
	            return 200;
	        } else {
	            System.out.println("저장");
	            rvUsRepository.save(ReviewUserEntity.of(userId, reviewEntity, LocalDateTime.now()));
	            return 201;
	        }
	    } else {
	        // 리뷰를 찾을 수 없음, 예외 처리 또는 로깅을 고려할 수 있음
	    	System.out.println("리뷰를 찾을 수 없음");
	        return null;
	    }
	}	
	
//	서평 삭제
	@Transactional
	public void deleteReview(Long userId, Long reviewId) {
		
		// 좋아요 기록 삭제
		rvUsRepository.deleteByReviewEntity(reviewRepository.findByReviewId(reviewId));
		
		// 플리 목록에서 삭제
		ReviewEntity reviewEntity = new ReviewEntity();
		reviewEntity.setReviewId(reviewId);
		reviewPlaylistRepository.deleteByReviewEntity(reviewEntity);
		
		// 서평 삭제
		reviewRepository.deleteByUserIdAndReviewId(userId, reviewId);
	}
}
 