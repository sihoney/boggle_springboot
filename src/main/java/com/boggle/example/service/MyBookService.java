package com.boggle.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.controller.LoginResponse;
import com.boggle.example.domain.EmotionEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.domain.ReviewUserEntity;
import com.boggle.example.domain.UserEntity;
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
	ReviewUserRepository reviewUserRepository;
	@Autowired
	ReviewPlaylistRepository reviewPlaylistRepository;
	
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
	
	@Transactional(readOnly = true)
	public Page<ReviewEntity> getReviewsByCreatedAt(String nickname, Long userId, Pageable pageable, Long authUserId) {
		
		if( !Objects.isNull(nickname) ) {
			userId = userRepository.findByNickname(nickname).getUserId();
		}
		
		Page<ReviewEntity> reviewList = reviewRepository.findAllByUserId(userId, pageable);
		reviewList.getContent().stream().forEach(reviewEntity -> {
			reviewEntity.setLikeCount(reviewEntity.getReviewUserEntityList().size()); //reviewUserRepository.getLikeCount(reviewEntity.getReviewId())
			reviewEntity.setLikeByAuthUser(reviewUserRepository.existsByUserIdAndReviewId(authUserId, reviewEntity.getReviewId()));
		});

		return reviewList;
	}
	
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
			reviewEntity.setLikeCount(reviewEntity.getReviewUserEntityList().size()); //reviewUserRepository.getLikeCount(reviewEntity.getReviewId())
			reviewEntity.setLikeByAuthUser(reviewUserRepository.existsByUserIdAndReviewId(authUserId, reviewEntity.getReviewId()));
		});
		
		return reviewList;
	}
	
	@Transactional
	public ReviewUserEntity likeOrDislikeReview(Long userId, Long reviewId) {
		
		// 상태 파악
		boolean likeReview = reviewUserRepository.existsByUserIdAndReviewId(userId, reviewId);

		if(likeReview) {
			System.out.println("삭제");
			return ReviewUserEntity.of(
					userId, 
					reviewUserRepository.deleteByUserIdAndReviewId(userId, reviewId),
					LocalDateTime.now()
				);
		} else {
			System.out.println("저장");
			return reviewUserRepository.save(ReviewUserEntity.of(userId, reviewId, LocalDateTime.now()));
		}
	}
	
	@Transactional
	public Page<ReviewEntity> getReviewsOrderByLikeCount(Long userId, Long authUserId, Pageable pageable) {
		System.out.println("getReviewsOrderByLikeCount()");
		
		Page<Object[]> page = reviewRepository.getAllReviewSortByLikeCount(userId, pageable);
		Iterator<Object[]> itr = page.getContent().iterator();

		List<ReviewEntity> entityList = new ArrayList<>();
		while(itr.hasNext()) {
			Object[] obj = itr.next();

			ReviewEntity reviewEntity = (ReviewEntity) obj[0];
			Long likeCount = (Long) obj[1];
			
			reviewEntity.setLikeCount(Integer.valueOf(likeCount.toString()));
			reviewEntity.setLikeByAuthUser(reviewUserRepository.existsByUserIdAndReviewId(authUserId, reviewEntity.getReviewId()));
			
			entityList.add(reviewEntity);
		}
		
		Page<ReviewEntity> sortedPage = new PageImpl<>(entityList, page.getPageable(), page.getTotalElements());
		
		return sortedPage;
	}
	
	@Transactional
	public void deleteReview(Long userId, Long reviewId) {
		
		// 좋아요 기록 삭제
		reviewUserRepository.deleteByReviewId(reviewId);
		
		// 플리 목록에서 삭제
		reviewPlaylistRepository.deleteByReviewId(reviewId);
		
		// 서평 삭제
		reviewRepository.deleteByUserIdAndReviewId(userId, reviewId);
	}
}
 