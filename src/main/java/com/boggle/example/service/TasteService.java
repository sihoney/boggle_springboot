package com.boggle.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.dto.user.UserDto;
import com.boggle.example.entity.BookEntity;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.entity.PlaylistUserEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewUserEntity;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.repository.PlaylistRepository;
import com.boggle.example.repository.PlaylistUserRepository;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.ReviewUserRepository;
import com.boggle.example.repository.UserRepository;

@Service
public class TasteService {

	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	ReviewUserRepository rvUsRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PlaylistUserRepository plUsRepository;
	@Autowired
	PlaylistRepository plRepository;
	
	@Transactional
	public Map<String, Object> getTaste(String nickname) {
		UserEntity userEntity = userRepository.findByNickname(nickname);
		
		Page<ReviewUserEntity> page01 = rvUsRepository.findAllByUserId(userEntity.getUserId(), PageRequest.of(0, 2));

		List<ReviewEntity> reviewlist = page01.getContent().stream().map(entity -> {
			ReviewEntity reviewEntity =  entity.getReviewEntity();
			
//			reviewEntity.setLikeCount(reviewEntity.getReviewUserEntityList().size());
			reviewEntity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(userEntity.getUserId(), reviewEntity));
//			reviewEntity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewId(authUserId, reviewEntity.getReviewId()));
			
			return reviewEntity;
		}).collect(Collectors.toList());
		
		Page<ReviewUserEntity> page02 = rvUsRepository.findAllByUserId(userEntity.getUserId(), PageRequest.of(0, 5));
		List<UserDto> userList = page02.stream().map(entity -> {
			UserDto dto = new UserDto();
			
			UserEntity user = userRepository.findById(entity.getUserId()).orElse(null);
			Page<ReviewEntity> reviewPage = reviewRepository.findAllByUserId(entity.getUserId(), null);
			
			dto.setNickname(user.getNickname());
			dto.setUserId(user.getUserId());
			dto.setUserProfile(user.getUserProfile());
			dto.setRecentReviewContent(entity.getReviewEntity().getContent());
			dto.setTotalReviewCount((int) reviewPage.getTotalElements());
			
			return dto;
		}).collect(Collectors.toList());
		
		System.out.println(userList);
		
		List<BookEntity> bookList = page02.stream().map(entity -> {
			return entity.getReviewEntity().getBookEntity();
		}).collect(Collectors.toList());
		
		Page<PlaylistUserEntity> page = plUsRepository.findAllByUserId(userEntity.getUserId(), PageRequest.of(0, 5));
		List<PlaylistEntity> plList = page.getContent().stream().map(entity -> {
			return plRepository.findByPlaylistId(entity.getPlaylistId());		
		}).collect(Collectors.toList());
		
		Map<String, Object> map = new HashMap();
		map.put("reviewList", reviewlist);
		map.put("userList", userList);
		map.put("bookList", bookList);
		map.put("plList", plList);
	
		return map;
	}
}
