package com.boggle.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.entity.EmotionEntity;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.repository.EmotionRepository;
import com.boggle.example.repository.MusicRepository;
import com.boggle.example.repository.PlaylistRepository;
import com.boggle.example.repository.ReviewPlaylistRepository;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.ReviewUserRepository;
import com.boggle.example.repository.UserRepository;

@Service
public class EmotionService {

	@Autowired
	EmotionRepository emotionRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	ReviewPlaylistRepository rvPlRepository;
	@Autowired
	PlaylistRepository playlistRepository;
	@Autowired
	ReviewUserRepository rvUsRepository;
	@Autowired
	MusicRepository musicRepository;
	
	/*
	 * getEmotions
	 * createEmotion
	 * main
	 */
	
	public List<EmotionEntity> getEmotions() {
		List<EmotionEntity> emotionList = emotionRepository.findAll();
		
		return emotionList;
	}
	
	public EmotionEntity createEmotion(String emotionName) {
		return emotionRepository.save(EmotionEntity.of(emotionName));
	}
	
	@Transactional
	public Map<String, Object> main(Long userId, Pageable pageable) {
		
		Map<String, Object> map = new HashMap<>();
		
		// 감정 리스트
		List<EmotionEntity> emotionList = emotionRepository.findAll();
		
		// 로그인 상태 > 플리 목록
		if(!Objects.isNull(userId)) {
			List<PlaylistEntity> playlists = playlistRepository.findAllByUserId(userId);
			
			System.out.println(playlists);
			
			map.put("playlists", playlists);
		}
		
		// 서평 리스트
//		Integer totalEmotionCount = emotionRepository.findAll().size();
//		Map<String, Object> reviewMap = reviewsByEmotionId((long) Math.floor(totalEmotionCount * Math.random()), userId, pageable);
		
		map.put("emotionList", emotionList);
//		map.put("reviewList", reviewMap.get("reviewList"));
//		map.put("pagination", reviewMap.get("pagination"));
		
		return map;
	}
}
