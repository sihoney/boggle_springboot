package com.boggle.example.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.domain.PlaylistEntity;

@SpringBootTest
@Transactional
public class ReviewServiceTest {
	
	@Autowired
	ReviewService reviewService;
	
//	@Test
//	public void writeFormTest() {
//		//Given
//	
//		
//		//When
//		MultiValueMap<String, List> map = reviewService.writeForm();
//		
//		//Then
//		Assertions.assertNotNull(map);
//		Assertions.assertNotNull(map.get("emotionList"));
//		Assertions.assertNotNull(map.get("fontList"));
//		Assertions.assertNotNull(map.get("wallpaperList"));
//		
//		Assertions.assertEquals(map.get("emotionList").get(0).getClass(), EmotionEntity.class);
//		Assertions.assertEquals(map.get("fontList").get(0).getClass(), FontEntity.class);
//		Assertions.assertEquals(map.get("wallpaperList").get(0).getClass(), WallpaperEntity.class);
//	}
	
	@Test
	public void getMyPlaylistTest() {
		//Given
		Long userId = Long.parseLong("1");
		
		//When
		List<PlaylistEntity> playlistList = reviewService.getMyPlaylist(userId);
		
		//Then
		Assertions.assertNotNull(playlistList);
	}
}
