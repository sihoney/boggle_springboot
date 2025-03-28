package com.boggle.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.dto.PlaylistDTO;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.entity.PlaylistUserEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewPlaylistEntity;
import com.boggle.example.repository.EmotionRepository;
import com.boggle.example.repository.MusicRepository;
import com.boggle.example.repository.PlaylistRepository;
import com.boggle.example.repository.PlaylistUserRepository;
import com.boggle.example.repository.ReviewPlaylistRepository;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.ReviewUserRepository;
import com.boggle.example.repository.UserRepository;
import com.boggle.example.util.PagingUtil;

@Service
public class PlaylistService {

	@Autowired
	PlaylistRepository plRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	ReviewUserRepository reviewUserRepository;
	@Autowired
	PlaylistUserRepository plUserRepository;
	@Autowired
	ReviewPlaylistRepository rvPlRepository;
	@Autowired
	EmotionRepository emotionRepository;
	@Autowired
	PlaylistRepository playlistRepository;
	@Autowired
	ReviewUserRepository rvUsRepository;
	@Autowired
	MusicRepository musicRepository;
	@Autowired
	ReviewService reviewService;
	
/*
	getPlaylistFolder				-> reviewService
	getReviewByPlaylist				-> reviewService
	
 	플리 조회/생성/삭제
 		getPlCover					특정 플리 정보 조회
 		makePlaylist
 		
 	플리 목록 조회
	  	getMyPlaylist
		getPlaylists				특정 유저의 플리 목록 조회	
		getMyPlaylists
			
 	플리 좋아요/취소
		toggleLikePlaylist			플리 좋아요 & 좋아요 취소
	
 */
	
	@Transactional(readOnly = true)
	public Map<String, Object> getPlaylistFolder(Long authUserId, Long playlistId, Pageable pageable) {
		
		PlaylistEntity plEntity = getPlCover(authUserId, playlistId);

		Page<ReviewEntity> page = getReviewByPlaylist(authUserId, playlistId, pageable);
	
		Map<String, Integer> pagination = PagingUtil.pagination(
				(int) page.getTotalElements(),
				pageable.getPageSize(), 
				pageable.getPageNumber() + 1);
		
		return Map.of(
				"reviewList", page.getContent(), 
				"playlistCover", plEntity, 
				"startPage", pagination.get("startPage"), 
				"endPage", pagination.get("endPage"),
				"authUserId", authUserId);
	}
	
	private Page<ReviewEntity> getReviewByPlaylist(Long authUserId, Long playlistId, Pageable pageable) {
		
		Page<ReviewPlaylistEntity> page = rvPlRepository.findAllByPlaylistEntity(plRepository.findByPlaylistId(playlistId), pageable);

		List<ReviewEntity> entityList = page.getContent().stream().map(entity -> {
			ReviewEntity reviewEntity = entity.getReviewEntity();
			
			boolean likeByAuthUser = reviewUserRepository.existsByUserIdAndReviewEntity(authUserId, reviewEntity);
			reviewEntity.setLikeByAuthUser(likeByAuthUser);
			
			return reviewEntity;
		}).collect(Collectors.toList());
				
		return new PageImpl<>(entityList, page.getPageable(), page.getTotalElements());
	}
	
/*
   	플리 조회/생성/삭제
 		getPlCover					특정 플리 정보 조회
 		makePlaylist	
 */
	
	/* ★플레이리스트 커버 */
	private PlaylistEntity getPlCover(Long userId, Long playlistId) {
		
		Tuple tuple = plRepository.getByPlaylistId(playlistId);
		
		PlaylistEntity plEntity = (PlaylistEntity) tuple.get(0);
		String nickname = (String) tuple.get(1);
		
		plEntity.setNickname(nickname);
		
		Optional<PlaylistUserEntity> plUserEntity = plUserRepository.findByUserIdAndPlaylistId(userId, playlistId);
		if(plUserEntity.isPresent()) {
			plEntity.setLikeByAuthUser(true);
		}
		
		return plEntity;
	}
	
	public int makePlaylist(String playlistName, Long userId) {
		PlaylistEntity plEntity = PlaylistEntity.of(playlistName, userId);
		plEntity.setCreatedAt(LocalDateTime.now());

		playlistRepository.save(plEntity);
		
		return 201;
	}
	
/*
 	플리 목록 조회
	  	getMyPlaylist
		getPlaylists				특정 유저의 플리 목록 조회	
		getMyPlaylists				
 */
	
	@Transactional(readOnly = true)
	public List<PlaylistEntity> getMyPlaylist(Long userId) {
		return playlistRepository.findByUserId(userId);
	}
	
	@Transactional(readOnly = true)
	public Map<String, List<PlaylistEntity>> getPlaylists(String nickname) {
		// 유저 정보 불러오기
		Long userId = userRepository.findByNickname(nickname).getUserId();
		
		// 유저가 좋아요한 플레이리스트들
		List<PlaylistEntity> likeList = plRepository.findByPlaylistUser_UserId(userId);

		// 인기있는 플레이리스트들
		List<Object[]> ppList = plRepository.findOrderByLikeCount(PageRequest.of(1, 4, Sort.by(Sort.Order.desc("likeCount"))));
		Iterator<Object[]> itr = ppList.iterator();
		List<PlaylistEntity> entityList = new ArrayList<>();
		while(itr.hasNext()) {
			Object[] obj = itr.next();
			
			PlaylistEntity plEntity = (PlaylistEntity) obj[0];
			
			entityList.add(plEntity);
		}

		// 내가 만든 플레이리스트들
		List<PlaylistEntity> myList = plRepository.findByUserId(userId);

		if(likeList.size() == 0)
			likeList = null;
		
		if(entityList.size() == 0)
			entityList = null;
		
		if(myList.size() == 0) {
			myList = null;
		}
		
		Map<String, List<PlaylistEntity>> map = new HashMap<>();
		map.put("likeList", likeList);
		map.put("ppList", entityList);
		map.put("myList", myList);
		
		return map;
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
	
/*
 	플리 좋아요/취소
		toggleLikePlaylist			플리 좋아요 & 좋아요 취소
 */	
	
	/* 플리 좋아요 & 취소 */
	@Transactional
	public Long toggleLikePlaylist(Long userId, Long playlistId) {
		
		Optional<PlaylistUserEntity> existingLike = plUserRepository.findByUserIdAndPlaylistId(userId, playlistId);
		
		// 좋아요가 이미 존재하면 삭제
	    if (existingLike.isPresent()) {
	        plUserRepository.delete(existingLike.get());
	        return 0L;
	    } 
	    // 좋아요가 존재하지 않으면 생성
	    else {
	        PlaylistUserEntity newLike = new PlaylistUserEntity();
	        newLike.setPlaylistId(playlistId);
	        newLike.setUserId(userId);
	        return plUserRepository.save(newLike).getPlaylistUserId();
	    }
	}
}
