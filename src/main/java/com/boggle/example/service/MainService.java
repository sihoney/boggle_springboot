package com.boggle.example.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.domain.EmotionEntity;
import com.boggle.example.domain.MusicEntity;
import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.domain.ReviewPlaylistEntity;
import com.boggle.example.domain.UserEntity;
import com.boggle.example.dto.PlaylistDTO;
import com.boggle.example.repository.EmotionRepository;
import com.boggle.example.repository.MusicRepository;
import com.boggle.example.repository.PlaylistRepository;
import com.boggle.example.repository.ReviewPlaylistRepository;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.ReviewUserRepository;
import com.boggle.example.repository.UserRepository;
import com.boggle.example.util.PagingUtil;

@Service
public class MainService {

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
		Integer totalEmotionCount = emotionRepository.findAll().size();
		Map<String, Object> reviewMap = reviewsByEmotionId((long) Math.floor(totalEmotionCount * Math.random()), userId, pageable);
		
		map.put("emotionList", emotionList);
//		map.put("reviewList", reviewMap.get("reviewList"));
//		map.put("pagination", reviewMap.get("pagination"));
		
		return map;
	}
	
	@Transactional
	public Map<String, Object> reviewsByEmotionId(Long emotionId, Long authUserId, Pageable pageable) {
		Map<String, Object> map = new HashMap<>();

		EmotionEntity emotionEntity = emotionRepository.findById(emotionId).orElse(null);
		
		if(emotionEntity != null) {
			Page<ReviewEntity> reviewList = reviewRepository.findAllByEmotionEntity(emotionEntity, pageable);
			
			List<ReviewEntity> modifiedList = reviewList.stream().map(entity -> {
					UserEntity userEntity = userRepository.findById(entity.getUserId()).orElse(null);
					
					if(userEntity != null) {
						entity.setNickname(userEntity.getNickname());
					}
					if(authUserId != null) {
						entity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, entity));
						entity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, entity));
					}
					
					return entity;
				}).collect(Collectors.toList());
			
			List<MusicEntity> musicList = musicRepository.findAllByEmotionId(emotionId);
			
			map.put("reviewList", modifiedList);
			map.put("musicList", musicList);
//			 else {
//				map.put("reviewList", reviewList.getContent());
//			}
			
			Map<String, Integer> pagination = PagingUtil.pagination((int) reviewList.getTotalElements(), pageable.getPageSize(), pageable.getPageNumber() + 1);
			map.put("pagination", pagination);
			
			return map;			
		} else {
			System.out.println("잘못된 emotion id");
			return null;			
		}
	}
	
	@Transactional
	public Map<String, Object> reviewsByPlaylistId(Long playlistId, Long authUserId, Pageable pageable) {
		Map<String, Object> map = new HashMap<>();
		
		PlaylistEntity plEntity = playlistRepository.findByPlaylistId(playlistId);
		if(plEntity != null) {
			Page<ReviewPlaylistEntity> reviewList = rvPlRepository.findAllByPlaylistEntity(plEntity, pageable);
		
			
			List<ReviewEntity> modifiedList = reviewList.stream().map(entity -> {
				ReviewEntity rvEntity = entity.getReviewEntity();
				
				if(authUserId != null) {
					rvEntity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, rvEntity));
					rvEntity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, rvEntity));
				}
				
				return rvEntity;
			}).collect(Collectors.toList());
			
			map.put("reviewList", modifiedList);
			
			List<MusicEntity> musicList = musicRepository.findAllByEmotionId(plEntity.getEmotionId());
			map.put("musicList", musicList);
			//map.put("reviewList", reviewList.getContent());
			
			Map<String, Integer> pagination = PagingUtil.pagination((int) reviewList.getTotalElements(), pageable.getPageSize(), pageable.getPageNumber() + 1);
			map.put("pagination", pagination);
			
			return map;
		
		} else {
			System.out.println("잘못된 플레이리스트 id");
			return null;
		}		
	}
	
	public List<PlaylistDTO> getMyPlaylists(Long reviewId, Long userId) {
		List<PlaylistEntity> playlists = playlistRepository.findAllByUserId(userId);
		
		List<PlaylistDTO> modifiedPlaylists = playlists.stream().map(entity -> {
			Long playlistId = entity.getPlaylistId();
			
			PlaylistDTO dto = new PlaylistDTO();
			dto.setPlaylistId(entity.getPlaylistId());
			dto.setPlaylistName(entity.getPlaylistName());
			dto.setHasReview(
					rvPlRepository.existsByReviewEntityAndPlaylistEntity(
						reviewRepository.findByReviewId(reviewId), 
						playlistRepository.findByPlaylistId(playlistId)
			));
			
			return dto;
		}).collect(Collectors.toList());
		
		return modifiedPlaylists;
	}
	
	// 서평을 플리에 추가 & 삭제
	public int toggleReviewPlaylist(Long reviewId, Long playlistId) {
		
		ReviewEntity reviewEntity = reviewRepository.findByReviewId(reviewId);
		PlaylistEntity playlistEntity = playlistRepository.findByPlaylistId(playlistId);
		
		Optional<ReviewPlaylistEntity> existingEntity = rvPlRepository.findByReviewEntityAndPlaylistEntity(
				reviewEntity,
				playlistEntity
				);
		
		if(existingEntity.isPresent()) {
			// 삭제
			rvPlRepository.delete(existingEntity.get());
			return 200;
		} else {
			// 저장
			rvPlRepository.save(ReviewPlaylistEntity.of(reviewEntity, playlistEntity));
			return 201;
		}
	}
	
	public int makePlaylist(String playlistName, Long userId) {
		PlaylistEntity plEntity = PlaylistEntity.of(playlistName, userId);
		plEntity.setCreatedAt(LocalDateTime.now());

		playlistRepository.save(plEntity);
		
		return 201;
	}
}
