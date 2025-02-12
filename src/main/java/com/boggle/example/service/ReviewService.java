package com.boggle.example.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.dto.LikeResult;
import com.boggle.example.dto.ReviewSearchCriteria;
import com.boggle.example.dto.review.RegisterReviewRequest;
import com.boggle.example.dto.review.WriteFormResponse;
import com.boggle.example.entity.BookEntity;
import com.boggle.example.entity.EmotionEntity;
import com.boggle.example.entity.FontEntity;
import com.boggle.example.entity.GenreEntity;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewPlaylistEntity;
import com.boggle.example.entity.ReviewUserEntity;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.entity.WallpaperEntity;
import com.boggle.example.exception.EmotionNotFoundException;
import com.boggle.example.exception.ReviewNotFoundException;
import com.boggle.example.exception.UnauthorizedException;
import com.boggle.example.exception.UserNotFoundException;
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
import com.boggle.example.util.SearchType;

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
 	
 	서평 조회/생성/삭제/수정
 		fineReview					서평 조회
 		createReview				서평 생성
 		deleteReview				서평 삭제
 		updateReview				서평 수정
 		
 	목록 조회
		findReviewsByCreatedAt		최신순, 오래된순
		findReviewsByLikeCountDesc	인기순
		findReviewsByEmotion		감정순 
	 	findReviewsByEmotionId		감정순
 		findReviewsByPlaylistId		특정 플리
	  	findReviewsByUserId		    특정 유저(모달용)
 	
 	플리에 추가/삭제
 	 	managePlaylistReviewAdd			서평을 플리에 등록
 	 	managePlaylistReviewAddList		서평을 플리에 추가
		managePlaylistReviewRemove		서평을 플리에서 삭제
		managePlaylistReviewToggle
		
	좋아요
		toggleReviewLike			서평 좋아요 & 좋아요 취소
 */
	
	@Transactional(readOnly = true)
	public WriteFormResponse writeForm(Long isbn, Long reviewId) {
		
		BookEntity bookEntity = bookRepository.findByIsbn(isbn).orElse(null);
		ReviewEntity reviewEntity = reviewRepository.findByReviewId(reviewId).orElse(null);
		
//		BookEntity bookEntity = null;
//		ReviewEntity reviewEntity = null;
//		
//		if(!Objects.isNull(reviewId)) { // 책 + 감정 + 스타일 + 리뷰 정보
//			reviewEntity = reviewRepository.findByReviewId(reviewId);
//		}
//		
//		else if(!Objects.isNull(isbn)) { // 책 정보
//			bookEntity = bookRepository.findByIsbn(isbn);
//		}
		
		//감정과 font, wallpaper Entity
		List<EmotionEntity> emotionEntityList = emotionRepository.findAll();
		List<FontEntity> fontEntityList = fontRepository.findAll();
		List<WallpaperEntity> wallpaperEntityList = wallpaperRepository.findAll();
		
		return WriteFormResponse.of(
			emotionEntityList, 
			fontEntityList, 
			wallpaperEntityList, 
			bookEntity, 
			reviewEntity
		);
	}	
	
/*
 	서평 조희/생성/삭제/수정
 		fineReview					서평 조회
 		createReview				서평 생성
 			validateReviewRequest
 			getOrCreateBook
 			createNewBook
 			buildReviewEntity
 		deleteReview				서평 삭제
 		updateReview				서평 수정
 			updateReviewFields
 */	
	/* 조희 */
	public ReviewEntity findReview(Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findByReviewId(reviewId)
            .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
        
        UserEntity user = userRepository.findById(reviewEntity.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("User not found for review: " + reviewId));
        
        reviewEntity.setNickname(user.getNickname());
        return reviewEntity;
	}
	
	/* 생성 */
	@Transactional
	public Long createReview(RegisterReviewRequest reviewRequest, Long userId) {
			
        validateReviewRequest(reviewRequest);

        BookEntity bookEntity = getOrCreateBook(reviewRequest);
        ReviewEntity reviewEntity = buildReviewEntity(reviewRequest, userId, bookEntity);
        
        return reviewRepository.save(reviewEntity).getReviewId();
	}
	
    // Private helper methods
    private void validateReviewRequest(RegisterReviewRequest request) {
        if (request == null || StringUtils.isBlank(request.getContent())) {
            throw new IllegalArgumentException("Review content cannot be empty");
        }
    }
    
    private BookEntity getOrCreateBook(RegisterReviewRequest request) {
        return bookRepository.findByIsbn(request.getIsbn())
            .orElseGet(() -> createNewBook(request));
    }

    private BookEntity createNewBook(RegisterReviewRequest request) {
        GenreEntity genre = genreRepository.save(
            GenreEntity.of(request.getGenreName(), request.getGenreId())
        );

        return BookEntity.of(
            request.getIsbn(),
            request.getBookName(),
            request.getAuthor(),
            genre.getGenreId(),
            request.getBookUrl(),
            request.getCoverUrl()
        );
    }
    
    private ReviewEntity buildReviewEntity(RegisterReviewRequest request, Long userId, BookEntity book) {
        ReviewEntity review = ReviewEntity.of(
            request.getContent(),
            userId,
            EmotionEntity.of(request.getEmotionId()),
            FontEntity.of(request.getFontId()),
            WallpaperEntity.of(request.getWallpaperId()),
            LocalDateTime.now()
        );
        review.setBookEntity(book);
        return review;
    }
    
    /* 삭제 */
	@Transactional
	public void deleteReview(Long userId, Long reviewId) {
        ReviewEntity review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
                
            // Validate ownership
            if (!review.getUserId().equals(userId)) {
                throw new UnauthorizedException("User is not authorized to delete this review");
            }

            // Delete associated records
            rvUsRepository.deleteByReviewEntity(review);
            reviewPlaylistRepository.deleteByReviewEntity(review);
            reviewRepository.delete(review);
	}
	
	/* 수정 */
	@Transactional
	public ReviewEntity updateReview(RegisterReviewRequest updateRequest) {
		
		ReviewEntity existingReview = reviewRepository.findByReviewId(updateRequest.getReviewId())
				.orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + updateRequest.getReviewId()));
		
        updateReviewFields(existingReview, updateRequest);
        return reviewRepository.save(existingReview);
	}
	
    private void updateReviewFields(ReviewEntity review, RegisterReviewRequest request) {
        review.setModifiedAt(LocalDateTime.now());

        Optional.ofNullable(request.getContent())
            .ifPresent(review::setContent);

        Optional.ofNullable(request.getIsbn())
            .ifPresent(isbn -> review.setBookEntity(createNewBook(request)));

        Optional.ofNullable(request.getEmotionId())
            .ifPresent(emotionId -> review.setEmotionEntity(EmotionEntity.of(emotionId)));

        Optional.ofNullable(request.getFontId())
            .ifPresent(fontId -> review.setFontEntity(FontEntity.of(fontId)));

        Optional.ofNullable(request.getWallpaperId())
            .ifPresent(wallpaperId -> review.setWallpaperEntity(WallpaperEntity.of(wallpaperId)));
    }
	
/*
 	목록 조회
 		findReviewsByPlaylist
 		findReviewsByEmotion
 			enrichReviewsWithUserData
 			enrichReviewWithUserData
 			createResponseMap
 		findReviews
 			resolveUserId
 			fetchReviewsBySearchType
 			enrichReviewsWithLikeInfo
 			findReviewsByPopularity
 			findReviewsByEmotion
 			findReviewsBySearch
 */	

    /**
     * Find reviews by playlist with additional data
     */
    @Transactional(readOnly = true)
    public Map<String, Object> findReviewsByPlaylist(Long playlistId, Long authUserId, Pageable pageable) {
        PlaylistEntity playlist = playlistRepository.findByPlaylistId(playlistId)
            .orElseThrow(() -> new EntityNotFoundException("Playlist not found with id: " + playlistId));

        Page<ReviewPlaylistEntity> reviewPlaylistPage = rvPlRepository.findAllByPlaylistEntity(playlist, pageable);
        
        List<ReviewEntity> enrichedReviews = reviewPlaylistPage.getContent().stream()
            .map(ReviewPlaylistEntity::getReviewEntity)
            .map(review -> enrichReviewWithUserData(review, authUserId))
            .collect(Collectors.toList());

        return createResponseMap(
    		enrichedReviews, 
    		playlist.getEmotionId(), 
    		reviewPlaylistPage, 
    		pageable
    	);
    } 
    
    /**
     * Find reviews by emotion with additional data
     */
    @Transactional(readOnly = true)
    public Map<String, Object> findReviewsByEmotion(Long emotionId, Long authUserId, Pageable pageable) {
        EmotionEntity emotion = emotionRepository.findById(emotionId)
            .orElseThrow(() -> new EntityNotFoundException("Emotion not found with id: " + emotionId));

        Page<ReviewEntity> reviewPage = reviewRepository.findAllByEmotionEntity(emotion, pageable);
        
        List<ReviewEntity> enrichedReviews = enrichReviewsWithUserData(reviewPage.getContent(), authUserId);
        
        return createResponseMap(
    		enrichedReviews, 
    		emotionId, 
    		reviewPage, 
    		pageable
    	);
    }

    // Helper methods
    private List<ReviewEntity> enrichReviewsWithUserData(List<ReviewEntity> reviews, Long authUserId) {
        return reviews.stream()
            .map(review -> enrichReviewWithUserData(review, authUserId))
            .collect(Collectors.toList());
    }

    private ReviewEntity enrichReviewWithUserData(ReviewEntity review, Long authUserId) {
        if (authUserId != null) {
            review.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, review));
            review.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, review));
        }
        
        userRepository.findById(review.getUserId())
            .ifPresent(user -> review.setNickname(user.getNickname()));
            
        return review;
    }

    private Map<String, Object> createResponseMap(List<ReviewEntity> reviews, Long emotionId, Page<?> page, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("reviewList", reviews);
        response.put("musicList", musicRepository.findAllByEmotionId(emotionId));
        response.put("pagination", PagingUtil.pagination(
            (int) page.getTotalElements(), 
            pageable.getPageSize(), 
            pageable.getPageNumber() + 1
        ));
        return response;
    }
    
    /**
     * Find reviews
     */
    @Transactional(readOnly = true)
    public Page<ReviewEntity> findReviews(ReviewSearchCriteria criteria, Pageable pageable) {
        Long targetUserId = resolveUserId(criteria.getNickname(), criteria.getUserId());
        
        Page<ReviewEntity> reviews = fetchReviewsBySearchType(criteria, targetUserId, pageable);
//        enrichReviewsWithLikeInfo(reviews.getContent(), criteria.getAuthUserId());
        
        return reviews;
    }
    

    private Long resolveUserId(String nickname, Long userId) {
        if (nickname != null) {
            return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("User not found with nickname: " + nickname))
                .getUserId();
        }
        return userId;
    }
    
    private Page<ReviewEntity> fetchReviewsBySearchType(ReviewSearchCriteria criteria, Long userId, Pageable pageable) {
    	Page<ReviewEntity> reviews;
    	
    	reviews = switch (criteria.getSearchType()) {
	        case POPULARITY -> findReviewsByPopularity(userId, criteria.getAuthUserId(), pageable);
	        case EMOTION -> findReviewsByEmotion(userId, criteria.getEmotionName(), pageable);
	        case SEARCH -> findReviewsBySearch(userId, criteria.getQuery(), pageable);
	        default -> reviewRepository.findAllByUserId(userId, pageable);
	    };
        
    	if(criteria.getSearchType() != SearchType.POPULARITY) {
    		enrichReviewsWithLikeInfo(reviews.getContent(), criteria.getAuthUserId());
    	}
       
        return reviews;
    }

    private void enrichReviewsWithLikeInfo(List<ReviewEntity> reviews, Long authUserId) {
        reviews.forEach(review -> {
            review.setLikeCount(reviewUserRepository.countByUserIdAndReviewEntity(authUserId, review));
            review.setLikeByAuthUser(reviewUserRepository.existsByUserIdAndReviewEntity(authUserId, review));
        });
    }

    private Page<ReviewEntity> findReviewsByPopularity(Long userId, Long authUserId, Pageable pageable) {
        Page<ReviewEntity> reviews = reviewRepository.findAllByUserId(
    		userId, 
    		PageRequest.of(
    				pageable.getPageNumber(),
    				pageable.getPageSize()
		));
        
        List<ReviewEntity> sortedReviews = reviews.getContent().stream()
    		.map(entity -> {
    			entity.setLikeCount(rvUsRepository.countByUserIdAndReviewEntity(authUserId, entity));
    			entity.setLikeByAuthUser(rvUsRepository.existsByUserIdAndReviewEntity(authUserId, entity));
    						
    			return entity;
    		})
            .sorted(Comparator.comparingInt(ReviewEntity::getLikeCount).reversed())
            .collect(Collectors.toList());
        
        return new PageImpl<>(sortedReviews, pageable, reviews.getTotalElements());
    }

    private Page<ReviewEntity> findReviewsByEmotion(Long userId, String emotionName, Pageable pageable) {
        EmotionEntity emotion = emotionRepository.findByEmotionName(emotionName)
            .orElseThrow(() -> new EmotionNotFoundException("Emotion not found: " + emotionName));
            
        return reviewRepository.findAllByUserIdAndEmotionEntity_EmotionId(
            userId,
            emotion.getEmotionId(),
            pageable
        );
    }

    private Page<ReviewEntity> findReviewsBySearch(Long userId, String query, Pageable pageable) {
        return query != null 
            ? reviewRepository.findAllByUserIdAndContentContaining(userId, query, pageable)
            : reviewRepository.findAllByUserId(userId, pageable);
    }

	/*
//	최신순, 오래된순
	@Transactional(readOnly = true)
	public Page<ReviewEntity> findReviewsByCreatedAt(String nickname, Long userId, Pageable pageable, Long authUserId) {
		
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
	public Page<ReviewEntity> findReviewsByLikeCountDesc(Long userId, Long authUserId, Pageable pageable) {
		
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
	public Page<ReviewEntity> findReviewsByEmotion(Long userId, String emotionName, Pageable pageable, Long authUserId) {
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
	
//	모달 > 서평 목록 
	@Transactional(readOnly = true)
	public Page<ReviewEntity> findReviewsByUserId(Long userId, String query, Pageable pageable) {
		
		if(!Objects.isNull(query)) {
			return reviewRepository.findAllByUserIdAndContentContaining(userId, query, pageable);
		} else {
			return reviewRepository.findAllByUserId(userId, pageable);
		}
	}
	*/
	/* 감정순 */
	/*
	@Transactional
	public Map<String, Object> findReviewsByEmotionId(Long emotionId, Long authUserId, Pageable pageable) {
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
	*/
	/*
	@Transactional
	public Map<String, Object> findReviewsByPlaylistId(Long playlistId, Long authUserId, Pageable pageable) {
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
	*/
/*
 	플리에 추가/삭제
 	 	managePlaylistReviewAdd			서평을 플리에 등록
 	 	managePlaylistReviewAddList		서평을 플리에 추가
		managePlaylistReviewRemove		서평을 플리에서 삭제
		managePlaylistReviewToggle

 */

	@Transactional
	public ReviewPlaylistEntity managePlaylistReviewAdd(Long reviewId, Long playlistId) {
		return reviewPlaylistRepository.save(ReviewPlaylistEntity.of(reviewRepository.findById(reviewId).get(), playlistRepository.findById(playlistId).get()));
	}
	
	/* 서평 플리에 추가 */
	@Transactional
	public void managePlaylistReviewAddList(List<Integer> reviewList, Long playlistId) {
		
		Iterator<Integer> itr = reviewList.iterator();
		while(itr.hasNext()) {
			Long reviewId = (long) itr.next();
		
			PlaylistEntity playlist = plRepository.findByPlaylistId(playlistId).orElseThrow();
			ReviewEntity review = reviewRepository.findByReviewId(reviewId).orElseThrow();
			
			ReviewPlaylistEntity entity = new ReviewPlaylistEntity();
		
			entity.setPlaylistEntity(playlist);
			entity.setReviewEntity(review);		
			entity.setAddedAt(LocalDateTime.now());
			
			ReviewPlaylistEntity result = rvPlRepository.save(entity);
			
			System.out.println(result);
		}
	}
	
	/* 플리 서평 삭제 */
	@Transactional
	public void managePlaylistReviewRemove(Long reviewId, Long playlistId) {
		
		ReviewEntity review = reviewRepository.findByReviewId(reviewId).orElseThrow();
		PlaylistEntity playlist = plRepository.findByPlaylistId(playlistId).orElseThrow();
		
		rvPlRepository.deleteByReviewEntityAndPlaylistEntity(
			review, 
			playlist
		);
	}
	
	// 서평을 플리에 추가 & 삭제
	public int managePlaylistReviewToggle(Long reviewId, Long playlistId) {
		
		ReviewEntity reviewEntity = reviewRepository.findByReviewId(reviewId).orElseThrow();
		PlaylistEntity playlistEntity = playlistRepository.findByPlaylistId(playlistId).orElseThrow();
		
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
		toggleReviewLike			서평 좋아요 & 좋아요 취소
 */
	
//	서평 좋아요 & 취소
	@Transactional
	public LikeResult toggleReviewLike(Long userId, Long reviewId) {

	    ReviewEntity reviewEntity = reviewRepository.findByReviewId(reviewId)
	    		.orElseThrow(() -> new ReviewNotFoundException("해당 서평을 찾을 수 없습니다."));

        boolean isLiked = rvUsRepository.findByUserIdAndReviewEntity(userId, reviewEntity).isPresent();
        
        if (isLiked) {
            System.out.println("삭제");
            rvUsRepository.deleteByUserIdAndReviewEntity(userId, reviewEntity);
        } else {
            System.out.println("저장");
            rvUsRepository.save(ReviewUserEntity.of(
        		userId, 
        		reviewEntity, 
        		LocalDateTime.now()
    		));
        }
        
        enrichReviewWithUserData(reviewEntity, userId);
        
        return new LikeResult(!isLiked, reviewEntity.getLikeCount());
	}	
}
