package com.boggle.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.repository.PlaylistRepository;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.ReviewUserRepository;
import com.boggle.example.repository.UserRepository;

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
		System.out.println(likeList);
		//[]
		
		// 인기있는 플레이리스트들
		List<Object[]> ppList = plRepository.findOrderByLikeCount(PageRequest.of(1, 4, Sort.by(Sort.Order.desc("likeCount"))));
		Iterator<Object[]> itr = ppList.iterator();
		List<PlaylistEntity> entityList = new ArrayList<>();
		while(itr.hasNext()) {
			Object[] obj = itr.next();
			
			PlaylistEntity plEntity = (PlaylistEntity) obj[0];
			
			entityList.add(plEntity);
		}
		
		System.out.println(entityList);
		//[]
		
		// 내가 만든 플레이리스트들
		List<PlaylistEntity> myList = plRepository.findByUserId(userId);
		System.out.println(myList);
		/*
		 * [PlaylistEntity(
		 * 		playlistId=2, 
		 * 		playlistName=우갈02, 
		 *		userId=1, 
		 *		createdAt=2023-09-12T21:13, 
		 *		likeCount=null)]
		 */
		
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
	
	public void getPlaylistInfo(Long playlistId) {
		//★플레이리스트 커버
//		plRepository
	}
	
	public Page<ReviewEntity> getReviewByPlaylist(Long authUserId, Long plUserId, Long playlistId, Pageable pageable) {
		
		// 서평 리스트
		Page<ReviewEntity> page = reviewRepository.getAllReviewByPlaylistId(playlistId, pageable);
		
		// alreadyLiked 여부 체크하기
		List<ReviewEntity> newEntityList = new ArrayList<>();
		Iterator<ReviewEntity> itr = page.getContent().iterator();	
		while(itr.hasNext()) {
			ReviewEntity entity = itr.next();
			
			entity.setLikeByAuthUser(reviewUserRepository.existsByUserIdAndReviewId(authUserId,entity.getReviewId()));
			newEntityList.add(entity);
		}
		
		Page<ReviewEntity> newPage = new PageImpl<>(newEntityList, page.getPageable(), page.getTotalElements());
		
		return newPage;
	}
}
