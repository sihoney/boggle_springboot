package com.boggle.example.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.dto.LikeStatus;
import com.boggle.example.dto.PlaylistDTO;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.entity.PlaylistUserEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewPlaylistEntity;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.exception.PlaylistNotFoundException;
import com.boggle.example.exception.ReviewNotFoundException;
import com.boggle.example.exception.UserNotFoundException;
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
	ReviewUserRepository rvUsRepository;
	@Autowired
	MusicRepository musicRepository;
	@Autowired
	ReviewService reviewService;
	
	private static final int POPULAR_PLAYLIST_SIZE = 4;
	
/*
	findPlaylistFolder				-> reviewService
	findReviewsByPlaylistId			-> reviewService
	
 	플리 조회/생성/삭제
 		findPlaylist				특정 플리 정보 조회
 		createPlaylist
 		
 	플리 목록 조회
 		findPlaylistsByNickname		특정 유저의 플리 목록 조회
	 		getPopularPlaylists
	 		emptyIfNull
	  	findPlaylistsByUserId	
		findPlaylistsByUserIdAndReviewId
			
 	플리 좋아요/취소
		togglePlaylistLike			플리 좋아요 & 좋아요 취소
	
 */

    /**
     * 플레이리스트 폴더 상세 정보를 조회합니다.
     * 
     * @param authUserId 인증된 사용자 ID
     * @param playlistId 플레이리스트 ID
     * @param pageable 페이지 정보
     * @return 플레이리스트 폴더 정보와 리뷰 목록
     */
	@Transactional(readOnly = true)
	public Map<String, Object> findPlaylistFolder(Long authUserId, Long playlistId, Pageable pageable) {
		
		PlaylistEntity plEntity = findPlaylist(authUserId, playlistId);

		Page<ReviewEntity> page = findReviewsByPlaylistId(authUserId, playlistId, pageable);
	
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

    /**
     * 플레이리스트에 포함된 리뷰 목록을 조회합니다.
     */
	private Page<ReviewEntity> findReviewsByPlaylistId(Long authUserId, Long playlistId, Pageable pageable) {
		
        PlaylistEntity playlist = plRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("플레이리스트를 찾을 수 없습니다: " + playlistId));
		
		Page<ReviewPlaylistEntity> page = rvPlRepository.findAllByPlaylistEntity(playlist, pageable);

		List<ReviewEntity> reviews = page.getContent().stream()
				.map(entity -> {
					ReviewEntity reviewEntity = entity.getReviewEntity();
					
					boolean likeByAuthUser = reviewUserRepository.existsByUserIdAndReviewEntity(authUserId, reviewEntity);
					reviewEntity.setLikeByAuthUser(likeByAuthUser);
					
					return reviewEntity;
				})
				.collect(Collectors.toList());
				
		return new PageImpl<>(
			reviews, 
			page.getPageable(), 
			page.getTotalElements()
		);
	}
	
/*
   	플리 조회/생성/삭제
 		findPlaylist			특정 플리 정보 조회
 		createPlaylist			플리 생성
 */
	
    /**
     * 플레이리스트 상세 정보(커버) 조회
     * @param playlistId 플레이리스트 ID
     * @param userId 조회하는 사용자 ID
     * @return 플레이리스트 엔티티
     * @throws PlaylistNotFoundException 플레이리스트가 존재하지 않는 경우
     */
	private PlaylistEntity findPlaylist(Long playlistId, Long userId) {
		
		// 플레이리스트와 작성자 정보를 한 번에 조회
		Tuple tuple = plRepository.findPlaylistWithCreator(playlistId)
				.orElseThrow(() -> new PlaylistNotFoundException("플레이리스트를 찾을 수 없습니다. ID: " + playlistId));
		
		PlaylistEntity plEntity = (PlaylistEntity) tuple.get(0);
		String nickname = (String) tuple.get(1);
		plEntity.setNickname(nickname);
		
		// 현재 사용자의 좋아요 여부 설정
		if(userId != null) {
//			Optional<PlaylistUserEntity> plUserEntity = plUserRepository.findByUserIdAndPlaylistId(userId, playlistId);
//			if(plUserEntity.isPresent()) {
//				plEntity.setLikeByAuthUser(true);
//			}
			boolean isLiked = plUserRepository.existsByUserIdAndPlaylistId(userId, playlistId);
			plEntity.setLikeByAuthUser(isLiked);
		}

		return plEntity;
	}
	
    /**
     * 새로운 플레이리스트 생성
     * @param playlistName 플레이리스트 이름
     * @param userId 생성자 ID
     * @return 생성된 플레이리스트
     * @throws IllegalArgumentException 유효하지 않은 입력값
     */
	public PlaylistEntity createPlaylist(String playlistName, Long userId) {
        // 입력값 검증
        if (StringUtils.isBlank(playlistName)) {
            throw new IllegalArgumentException("플레이리스트 이름은 필수입니다.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        
     // 플레이리스트 생성
        PlaylistEntity playlist = PlaylistEntity.builder()
			.playlistName(playlistName)
			.userId(userId)
			.createdAt(LocalDateTime.now())
			.build();

		return plRepository.save(playlist);
	}
	
/*
 	플리 목록 조회
 		findPlaylistsByNickname				특정 유저의 플리 목록 조회
	 		getPopularPlaylists
	 		emptyIfNull
	  	findPlaylistsByUserId	
		findPlaylistsByUserIdAndReviewId 
*/
	
    /**
     * 사용자의 닉네임으로 플레이리스트들을 조회합니다.
     * - 좋아요한 플레이리스트
     * - 인기 플레이리스트
     * - 생성한 플레이리스트
     */
	@Transactional(readOnly = true)
	public Map<String, List<PlaylistEntity>> findPlaylistsByNickname(String nickname) {
		// 유저 정보 불러오기
		UserEntity user = userRepository.findByNickname(nickname)
				.orElseThrow(() -> new UserNotFoundException("해당 닉네임의 사용자를 찾을 수 없습니다.: " + nickname));
		
		// 각 카테고리별 플레이리스트 조회
        List<PlaylistEntity> likedPlaylists = plRepository.findByPlaylistUser_UserId(user.getUserId());
        List<PlaylistEntity> popularPlaylists = getPopularPlaylists();
        List<PlaylistEntity> createdPlaylists = plRepository.findByUserId(user.getUserId());
		
        return Map.of(
                "likeList", emptyIfNull(likedPlaylists),
                "ppList", emptyIfNull(popularPlaylists),
                "myList", emptyIfNull(createdPlaylists)
            );
	}

    /**
     * 인기 플레이리스트 조회 (좋아요 수 기준)
     */
    private List<PlaylistEntity> getPopularPlaylists() {
    	/*
		// 인기있는 플레이리스트들
		List<Object[]> ppList = plRepository.findOrderByLikeCount(
				PageRequest.of(1, 4, Sort.by(Sort.Order.desc("likeCount")))
			);
		
		Iterator<Object[]> itr = ppList.iterator();
		List<PlaylistEntity> entityList = new ArrayList<>();
		while(itr.hasNext()) {
			Object[] obj = itr.next();
			
			PlaylistEntity plEntity = (PlaylistEntity) obj[0];
			
			entityList.add(plEntity);
		}
    	*/
        PageRequest pageRequest = PageRequest.of(0, POPULAR_PLAYLIST_SIZE, 
            Sort.by(Sort.Direction.DESC, "likeCount"));
            
        return plRepository.findPopularPlaylists(pageRequest).stream()
            .map(array -> (PlaylistEntity) array[0])
            .collect(Collectors.toList());
    }	

    /**
     * null인 리스트를 빈 리스트로 변환
     */
    private <T> List<T> emptyIfNull(List<T> list) {
        return list == null || list.isEmpty() ? Collections.emptyList() : list;
    }
	
    /**
     * 특정 사용자의 모든 플레이리스트 조회
     */
	@Transactional(readOnly = true)
	public List<PlaylistEntity> findPlaylistsByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
		return plRepository.findByUserId(userId);
	}

    /**
     * 특정 사용자의 플레이리스트 목록을 조회하고, 각 플레이리스트에 특정 리뷰 포함 여부를 표시
     */
	public List<PlaylistDTO> findPlaylistsByUserIdAndReviewId(Long userId, Long reviewId) {
        if (userId == null || reviewId == null) {
            throw new IllegalArgumentException("사용자 ID와 리뷰 ID는 필수입니다.");
        }
		

        ReviewEntity review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다: " + reviewId));
        

        return plRepository.findAllByUserId(userId).stream()
            .map(playlist -> PlaylistDTO.builder()
                .playlistId(playlist.getPlaylistId())
                .playlistName(playlist.getPlaylistName())
                .hasReview(rvPlRepository.existsByReviewEntityAndPlaylistEntity(review, playlist))
                .build())
            .collect(Collectors.toList());
	}
	
/*
 	플리 좋아요/취소
		togglePlaylistLike			플리 좋아요 & 좋아요 취소
 */	
	

    /**
     * 플레이리스트 좋아요를 토글합니다.
     * 
     * @param userId 사용자 ID
     * @param playlistId 플레이리스트 ID
     * @return LikeStatus 좋아요 상태 (좋아요 추가: true, 좋아요 취소: false)
     * @throws IllegalArgumentException 유효하지 않은 입력값
     * @throws PlaylistNotFoundException 플레이리스트가 존재하지 않는 경우
     */
	@Transactional
	public LikeStatus togglePlaylistLike(Long userId, Long playlistId) {
        // 입력값 검증
        if (userId == null || playlistId == null) {
            throw new IllegalArgumentException("사용자 ID와 플레이리스트 ID는 필수입니다.");
        }

        // 플레이리스트 존재 여부 확인
        if (!plRepository.existsById(playlistId)) {
            throw new PlaylistNotFoundException("플레이리스트를 찾을 수 없습니다. ID: " + playlistId);
        }
        
        // 좋아요 상태 확인 및 토글
        return plUserRepository.findByUserIdAndPlaylistId(userId, playlistId)
            .map(like -> {
                // 좋아요가 있으면 삭제
                plUserRepository.delete(like);
                return new LikeStatus(false, null);
            })
            .orElseGet(() -> {
                // 좋아요가 없으면 생성
                PlaylistUserEntity newLike = PlaylistUserEntity.builder()
                    .userId(userId)
                    .playlistId(playlistId)
                    .build();
                PlaylistUserEntity saved = plUserRepository.save(newLike);
                return new LikeStatus(true, saved.getPlaylistUserId());
            });
	}
}
