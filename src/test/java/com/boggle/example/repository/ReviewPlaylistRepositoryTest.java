package com.boggle.example.repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.entity.ReviewPlaylistEntity;

@SpringBootTest
@Transactional
public class ReviewPlaylistRepositoryTest {

	@Autowired
	ReviewPlaylistRepository repository;
	
	/*
	 * @Test public void saveTest() { //Given Long reviewId = Long.parseLong("1");
	 * Long playlistId = Long.parseLong("6"); ReviewPlaylistEntity entity = new
	 * ReviewPlaylistEntity(playlistId, reviewId);
	 * 
	 * //When ReviewPlaylistEntity resultEntity = repository.save(entity);
	 * 
	 * //Then Assertions.assertNotNull(resultEntity);
	 * Assertions.assertEquals(reviewId, resultEntity.getReviewId());
	 * Assertions.assertEquals(playlistId, resultEntity.getPlaylistId()); }
	 */
	
//	@Test
//	public void findByIdTest() {
//		//Given
//		
//		
//		//When
//		
//		
//		//Then
//	}
}
