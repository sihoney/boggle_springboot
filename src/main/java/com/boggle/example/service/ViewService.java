package com.boggle.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.UserRepository;

@Service
public class ViewService {

	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	UserRepository userRepository;
	
	public ReviewEntity getReview(Long reviewId) {
		ReviewEntity reviewEntity = reviewRepository.findByReviewId(reviewId);
		
		reviewEntity.setNickname(userRepository.findById(reviewEntity.getUserId()).orElse(null).getNickname());
		
		return reviewEntity;
	}
}
