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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.dto.review.RegisterReviewRequest;
import com.boggle.example.dto.review.WriteFormResponse;
import com.boggle.example.entity.BookEntity;
import com.boggle.example.entity.EmotionEntity;
import com.boggle.example.entity.FontEntity;
import com.boggle.example.entity.GenreEntity;
import com.boggle.example.entity.MusicEntity;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewPlaylistEntity;
import com.boggle.example.entity.ReviewUserEntity;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.entity.WallpaperEntity;
import com.boggle.example.repository.BookRepository;
import com.boggle.example.repository.EmotionRepository;
import com.boggle.example.repository.FontRepository;
import com.boggle.example.repository.GenreRepository;
import com.boggle.example.repository.MusicRepository;
import com.boggle.example.repository.PlaylistRepository;
import com.boggle.example.repository.ReviewPlaylistRepository;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.ReviewUserRepository;
import com.boggle.example.repository.UserRepository;
import com.boggle.example.repository.WallpaperRepository;
import com.boggle.example.util.PagingUtil;

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
	@Autowired
	UserRepository userRepository;
	@Autowired
	ReviewUserRepository rvUsRepository;
	@Autowired
	ReviewPlaylistRepository rvPlRepository;
	@Autowired
	MusicRepository musicRepository;
	@Autowired
	PlaylistRepository plRepository;
	@Autowired
	ReviewUserRepository reviewUserRepository;
	
/*
 		writeForm					서평 등록 페이지
 	
 	서평 생성/삭제/수정
 		registerReview				서평 생성
 		deleteReview				서평 삭제
 		updateReview				서평 수정
 		
 	목록 조회
		getReviewsByCreatedAt		최신순, 오래된순
		getReviewsOrderByLikeCount	인기순
		getReviewByEmotion			감정순 
	 	reviewsByEmotionId			감정순
 		reviewsByPlaylistId			특정 플리
	  	getAllReviewByUserId		특정 유저(모달용)
 	
 	플리에 추가/삭제
 	 	addReviewListToPl			서평을 플리에 추가
 	 	addReviewToPlaylist			서평을 플리에 등록
		deleteReviewFromPlaylist	서평을 플리에서 삭제
		toggleReviewPlaylist
		
	좋아요
		likeOrDislikeReview			서평 좋아요 & 좋아요 취소
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
	
/*
 	서평 생성/삭제/수정
 		registerReview				서평 생성
 		deleteReview				서평 삭제
 		updateReview				서평 수정
 */	
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
	
//	서평 삭제
	@Transactional
	public void deleteReview(Long userId, Long reviewId) {
		
		// 좋아요 기록 삭제
		rvUsRepository.deleteByReviewEntity(reviewRepository.findByReviewId(reviewId));
		
		// 플리 목록에서 삭제
		ReviewEntity reviewEntity = new ReviewEntity();
		reviewEntity.setReviewId(reviewId);
		reviewPlaylistRepository.deleteByReviewEntity(reviewEntity);
		
		// 서평 삭제
		reviewRepository.deleteByUserIdAndReviewId(userId, reviewId);
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
	
/*
 	목록 조회
		getReviewsByCreatedAt		최신순, 오래된순
		getReviewsOrderByLikeCount	인기순
		getReviewByEmotion			감정순 
	 	reviewsByEmotionId			감정순
 		reviewsByPlaylistId			특정 플리
	  	getAllReviewByUserId		특정 유저(모달용)
 */	
	
//	최신순, 오래된순
	@Transactional(readOnly = true)
	public Page<ReviewEntity> getReviewsByCreatedAt(String nickname, Long userId, Pageable pageable, Long authUserId) {
		
		if( !Objects.isNull(nickname) ) {
			userId = userRepository.findByNickname(nickname).getUserId();
		}
		
		Page<ReviewEntity> reviewList = reviewRepository.findAllByUserId(userId, pageable);
		reviewList.getContent().stream().forEach(reviewEntity -> {
//			reviewEntity.setLikeCount(reviewEntity.getReviewUserEntityList().size()); //rvUsRepository.getLikeCount(reviewEntity.getReviewId())
			reviewEntity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, reviewEntity));
			reviewEntity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, reviewEntity));
		});

		return reviewList;
	}
	
//	인기순
	@Transactional
	public Page<ReviewEntity> getReviewsOrderByLikeCount(Long userId, Long authUserId, Pageable pageable) {
		
        Page<ReviewEntity> page = reviewRepository.findAllByUserId(userId, PageRequest.of(pageable.getPageNumber(),
        																					pageable.getPageSize()));
		List<ReviewEntity> list = page.getContent().stream().map(entity -> {
//			ReviewEntity reviewEntity = entity.getReviewEntity();
			
//			entity.setLikeCount(entity.getReviewUserEntityList().size());
			entity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, entity));
			entity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, entity));
						
			return entity;
		}).collect(Collectors.toList());
		
		List<ReviewEntity> sortedEntities = new ArrayList<>(list);
		sortedEntities.sort((entity1, entity2) -> {
			return entity2.getLikeCount() - entity1.getLikeCount();
		});
		
		System.out.println(page);
		System.out.println(sortedEntities);
		
		return new PageImpl<>(sortedEntities, page.getPageable(), page.getTotalElements());
	}

//	감정순
	@Transactional(readOnly = true)
	public Page<ReviewEntity> getReviewByEmotion(Long userId, String emotionName, Pageable pageable, Long authUserId) {
		System.out.println("MyBookService.getReviewByEmotion()");
		
		EmotionEntity emotionEntity = emotionRepository.findByEmotionName(emotionName);
		
		Page<ReviewEntity> reviewList = reviewRepository.findAllByUserIdAndEmotionEntity_EmotionId(
			userId,
			emotionEntity.getEmotionId(),
			pageable
		);
		reviewList.getContent().stream().forEach(reviewEntity -> {
//			reviewEntity.setLikeCount(reviewEntity.getReviewUserEntityList().size()); //rvUsRepository.getLikeCount(reviewEntity.getReviewId())
			reviewEntity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, reviewEntity));
			reviewEntity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, reviewEntity));
		});
		
		return reviewList;
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
	
	/* 모달 > 서평 목록 */
	@Transactional(readOnly = true)
	public Page<ReviewEntity> getAllReviewByUserId(Long userId, String query, Pageable pageable) {
		
		if(!Objects.isNull(query)) {
			return reviewRepository.findAllByUserIdAndContentContaining(userId, query, pageable);
		} else {
			return reviewRepository.findAllByUserId(userId, pageable);
		}
	}
	
/*
 	플리에 추가/삭제
 	 	addReviewListToPl			서평을 플리에 추가
 	 	addReviewToPlaylist			서평을 플리에 등록
		deleteReviewFromPlaylist	서평을 플리에서 삭제
		toggleReviewPlaylist

 */
	/* 서평 플리에 추가 */
	@Transactional
	public void addReviewListToPl(List<Integer> reviewList, Long playlistId) {
		
		Iterator<Integer> itr = reviewList.iterator();
		while(itr.hasNext()) {
			Long reviewId = (long) itr.next();
			
			ReviewPlaylistEntity entity = new ReviewPlaylistEntity();
			
			entity.setPlaylistEntity(plRepository.findByPlaylistId(playlistId));
			entity.setReviewEntity(reviewRepository.findByReviewId(reviewId));		
			entity.setAddedAt(LocalDateTime.now());
			
			ReviewPlaylistEntity result = rvPlRepository.save(entity);
			
			System.out.println(result);
		}
	}	
	
	@Transactional
	public ReviewPlaylistEntity addReviewToPlaylist(Long reviewId, Long playlistId) {
		return reviewPlaylistRepository.save(ReviewPlaylistEntity.of(reviewRepository.findById(reviewId).get(), playlistRepository.findById(playlistId).get()));
	}
	
	/* 플리 서평 삭제 */
	@Transactional
	public void deleteReviewFromPlaylist(Long reviewId, Long playlistId) {
		rvPlRepository.deleteByReviewEntityAndPlaylistEntity(reviewRepository.findByReviewId(reviewId), plRepository.findByPlaylistId(playlistId));
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
	
/*
	좋아요
		likeOrDislikeReview			서평 좋아요 & 좋아요 취소
 */
	
//	서평 좋아요 & 취소
	@Transactional
	public Integer likeOrDislikeReview(Long userId, Long reviewId) {

	    ReviewEntity reviewEntity = reviewRepository.findByReviewId(reviewId);

	    if (reviewEntity != null) {
	        ReviewUserEntity existingEntity = rvUsRepository.findByUserIdAndReviewEntity(userId, reviewEntity);
	        
	        if (existingEntity != null) {
	            System.out.println("삭제");
	            rvUsRepository.delete(existingEntity);
	            return 200;
	        } else {
	            System.out.println("저장");
	            rvUsRepository.save(ReviewUserEntity.of(userId, reviewEntity, LocalDateTime.now()));
	            return 201;
	        }
	    } else {
	        // 리뷰를 찾을 수 없음, 예외 처리 또는 로깅을 고려할 수 있음
	    	System.out.println("리뷰를 찾을 수 없음");
	        return null;
	    }
	}	
}
