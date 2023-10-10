package com.boggle.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.boggle.example.controller.LoginResponse;
import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.repository.PlaylistRepository;
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
	
	private PlaylistEntity getPlCover(Long playlistId) {
		//★플레이리스트 커버
		Tuple tuple = plRepository.findByPlaylistId(playlistId);
		
		PlaylistEntity plEntity = (PlaylistEntity) tuple.get(0);
		String nickname = (String) tuple.get(1);
		
		plEntity.setNickname(nickname);
		
		return plEntity;
	}
	
	private Page<ReviewEntity> getReviewByPlaylist(Long authUserId, Long plUserId, Long playlistId, Pageable pageable) {
		
		// 서평 리스트
		Page<Object[]> page = reviewRepository.getAllReviewByPlaylistId(playlistId, authUserId, pageable);
		
		// alreadyLiked 여부 체크하기
		List<ReviewEntity> newEntityList = new ArrayList<>();
		Iterator<Object[]> itr = page.getContent().iterator();	
		while(itr.hasNext()) {
			Object[] obj = itr.next();
			
			ReviewEntity entity = (ReviewEntity) obj[0];
			String nickname = (String) obj[1];
			Long likeByAuthUser = (Long) obj[2];

			if(likeByAuthUser != null && likeByAuthUser.intValue() == 1) {
				entity.setLikeByAuthUser(true);
			} else {
				entity.setLikeByAuthUser(false);
			}
			entity.setNickname(nickname);
			
			newEntityList.add(entity);
		}
		
		Page<ReviewEntity> newPage = new PageImpl<>(newEntityList, page.getPageable(), page.getTotalElements());
		
		return newPage;
	}
	
	public Map<String, Object> getPlaylistFolder(Long authUserId, Long playlistId, Pageable pageable) {
		
		PlaylistEntity plEntity = getPlCover(playlistId);
		
		String result;
		if(authUserId == plEntity.getUserId()) {
			result = "sameUser";
		} else {
			result = "otherUser";
		}

		Page<ReviewEntity> page = getReviewByPlaylist(
				authUserId, 
				plEntity.getUserId(), 
				playlistId, 
				pageable
				);
	
		Map<String, Integer> pagination = PagingUtil.pagination(
				(int) page.getTotalElements(), 
				page.getSize(), 
				page.getNumber());

		return Map.of(
				"reviewList", page.getContent(), 
				"playlistCover", plEntity, 
				"startPage", pagination.get("startPage"), 
				"endPage", pagination.get("endPage"));
	}
}
