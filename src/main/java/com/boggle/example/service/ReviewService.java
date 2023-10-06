package com.boggle.example.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.controller.RegisterReviewRequest;
import com.boggle.example.controller.WriteFormResponse;
import com.boggle.example.domain.BookEntity;
import com.boggle.example.domain.EmotionEntity;
import com.boggle.example.domain.FontEntity;
import com.boggle.example.domain.GenreEntity;
import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.domain.ReviewPlaylistEntity;
import com.boggle.example.domain.WallpaperEntity;
import com.boggle.example.repository.BookRepository;
import com.boggle.example.repository.EmotionRepository;
import com.boggle.example.repository.FontRepository;
import com.boggle.example.repository.GenreRepository;
import com.boggle.example.repository.PlaylistRepository;
import com.boggle.example.repository.ReviewPlaylistRepository;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.WallpaperRepository;

@Service
public class ReviewService {

	@Autowired
	EmotionRepository emotionRepository;
	@Autowired
	FontRepository fontRepository;
	@Autowired
	WallpaperRepository wallpaperRepository;
	@Autowired
	GenreRepository genreRepository;
	@Autowired
	BookRepository bookRepository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	PlaylistRepository playlistRepository;
	@Autowired
	ReviewPlaylistRepository reviewPlaylistRepository;
	
	@Transactional(readOnly = true)
	public WriteFormResponse writeForm() {
		//감정과 font, wallpaper Entity
		List<EmotionEntity> emotionEntityList = emotionRepository.findAll();
		List<FontEntity> fontEntityList = fontRepository.findAll();
		List<WallpaperEntity> wallpaperEntityList = wallpaperRepository.findAll();
		
		return WriteFormResponse.of(emotionEntityList, fontEntityList, wallpaperEntityList);
	}

	@Transactional
	public Long registerReview(RegisterReviewRequest reviewRequest, Long userId) {
		
		BookEntity existingBook = bookRepository.findByIsbn(reviewRequest.getIsbn());
			
		ReviewEntity reviewEntity = ReviewEntity.of(
			reviewRequest.getContent(), 
			userId, 
			EmotionEntity.of(reviewRequest.getEmotionId()),
			reviewRequest.getFontId(),
			LocalDateTime.now());
		
		if(existingBook == null) {
			GenreEntity genreEntity = genreRepository.save(GenreEntity.of(
					reviewRequest.getGenreName(), 
					reviewRequest.getGenreId()));
			
			reviewEntity.setBookEntity(BookEntity.of(
					reviewRequest.getIsbn(), 
					reviewRequest.getBookName(), 
					reviewRequest.getAuthor(), 
					genreEntity.getGenreId(), 
					reviewRequest.getBookUrl(), 
					reviewRequest.getCoverUrl()));
		} else {
			reviewEntity.setBookEntity(existingBook);
		}

		ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);
		
		return savedReviewEntity.getReviewId();
	}
	
	@Transactional(readOnly = true)
	public List<PlaylistEntity> getMyPlaylist(Long userId) {
		return playlistRepository.findByUserId(userId);
	}
	
	@Transactional
	public ReviewPlaylistEntity addReviewToPlaylist(ReviewPlaylistEntity reviewPlayEntity) {
		return reviewPlaylistRepository.save(reviewPlayEntity);
	}
}
