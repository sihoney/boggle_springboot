package com.boggle.example.repository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.domain.PlaylistEntity;

@SpringBootTest
@Transactional
public class PlaylistRepositoryTest {

	@Autowired
	PlaylistRepository playlistRepository;
	
	@Test
	public void saveTest() {
		//Given
		String playlistName = "다시 읽고 싶은 책들 모음";
		Long userId = Long.parseLong("1");
		PlaylistEntity entity = PlaylistEntity.of(playlistName, userId);
		
		//When
		PlaylistEntity resultEntity = playlistRepository.save(entity);
		System.out.println(resultEntity);
		
		//Then
		Assertions.assertNotNull(resultEntity);
//		Assertions.assertEquals(LocalDateTime.class, resultEntity.getCreatedAt().getClass());
		Assertions.assertEquals(Long.class, resultEntity.getPlaylistId().getClass());
		Assertions.assertEquals(playlistName, resultEntity.getPlaylistName());
		Assertions.assertEquals(userId, resultEntity.getUserId());
	}
}
