package com.boggle.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.dto.review.RegisterReviewRequest;
import com.boggle.example.dto.review.WriteFormResponse;
import com.boggle.example.entity.BookEntity;
import com.boggle.example.entity.EmotionEntity;
import com.boggle.example.entity.FontEntity;
import com.boggle.example.entity.GenreEntity;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewPlaylistEntity;
import com.boggle.example.entity.WallpaperEntity;
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
	
/*
 	writeForm				서평 등록 페이지
 	registerReview			서평 생성
 	addReviewToPlaylist		서평을 플리에 등록
 	updateReview			서평 수정
 */
	
	@Transactional(readOnly = true)
	public WriteFormResponse writeForm(Long isbn, Long reviewId) {
		
		//감정과 font, wallpaper Entity
		List<EmotionEntity> emotionEntityList = emotionRepository.findAll();
		List<FontEntity> fontEntityList = fontRepository.findAll();
		List<WallpaperEntity> wallpaperEntityList = wallpaperRepository.findAll();
		
		BookEntity bookEntity = null;
		ReviewEntity reviewEntity = null;
		
		if(!Objects.isNull(reviewId)) { // 책 + 감정 + 스타일 + 리뷰 정보
			reviewEntity = reviewRepository.findByReviewId(reviewId);
		}
		
		else if(!Objects.isNull(isbn)) { // 책 정보
			bookEntity = bookRepository.findByIsbn(isbn);
		}
		
		return WriteFormResponse.of(emotionEntityList, fontEntityList, wallpaperEntityList, bookEntity, reviewEntity);
	}

	@Transactional
	public Long registerReview(RegisterReviewRequest reviewRequest, Long userId) {
			
//		reviewEntity 생성
		ReviewEntity reviewEntity = ReviewEntity.of(
			reviewRequest.getContent(), 
			userId, 
			EmotionEntity.of(reviewRequest.getEmotionId()),
			FontEntity.of(reviewRequest.getFontId()),
			WallpaperEntity.of(reviewRequest.getWallpaperId()),
			LocalDateTime.now());
		
//		bookEntity 등록하기
//		bookEntity 존재 유무 체크
		BookEntity existingBook = bookRepository.findByIsbn(reviewRequest.getIsbn());
		if(Objects.isNull(existingBook)) {
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

//		reviewEntity 저장
		ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);
		
		return savedReviewEntity.getReviewId();
	}
	
	@Transactional(readOnly = true)
	public List<PlaylistEntity> getMyPlaylist(Long userId) {
		return playlistRepository.findByUserId(userId);
	}
	
	@Transactional
	public ReviewPlaylistEntity addReviewToPlaylist(Long reviewId, Long playlistId) {
		return reviewPlaylistRepository.save(ReviewPlaylistEntity.of(reviewRepository.findById(reviewId).get(), playlistRepository.findById(playlistId).get()));
	}
	
	@Transactional
	public ReviewEntity updateReview(RegisterReviewRequest registerReview) {
		
		ReviewEntity existingReview = reviewRepository.findByReviewId(registerReview.getReviewId());
		
		if(existingReview != null) {
			existingReview.setModifiedAt(LocalDateTime.now());
			
//			서평 글 수정한 경우
			if(registerReview.getContent() != null) {
				existingReview.setContent(registerReview.getContent());
			}
			
//			책 선택 수정한 경우
			if(registerReview.getIsbn() != null) {
				GenreEntity genreEntity = genreRepository.save(GenreEntity.of(
						registerReview.getGenreName(), 
						registerReview.getGenreId()));
				
				existingReview.setBookEntity(BookEntity.of(
						registerReview.getIsbn(), 
						registerReview.getBookName(), 
						registerReview.getAuthor(), 
						genreEntity.getGenreId(), 
						registerReview.getBookUrl(), 
						registerReview.getCoverUrl()
						));
			}
			
//			감정 수정한 경우
			if(registerReview.getEmotionId() != null) {
				existingReview.setEmotionEntity(EmotionEntity.of(registerReview.getEmotionId()));
			}
			
//			폰트 수정한 경우
			if(registerReview.getFontId() != null) {
				existingReview.setFontEntity(FontEntity.of(registerReview.getFontId()));
			}
			
//			배경화면 수정한 경우
			if(registerReview.getWallpaperId() != null) {
				existingReview.setWallpaperEntity(WallpaperEntity.of(registerReview.getWallpaperId()));
			}
		
			return reviewRepository.save(existingReview);
		}
		
		return null;
	}
}
