package com.boggle.example.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.dto.LikeStatus;
import com.boggle.example.dto.PlaylistDTO;
import com.boggle.example.dto.ReviewSearchCriteria;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewPlaylistEntity;
import com.boggle.example.entity.UserPrincipal;
import com.boggle.example.service.PlaylistService;
import com.boggle.example.service.ReviewService;
import com.boggle.example.util.PagingUtil;


@Controller
public class PlaylistController {
	
	@Autowired
	PlaylistService plService;
	@Autowired
	ReviewService reviewService;
	
	final int PAGE_SIZE = 5;
	final int MODAL_PAGE_SIZE = 5;
	final int CURRENT_PAGE = 0;
	final Sort SORT = Sort.by(Sort.Order.desc("addedAt"));
	final String STR_SORT = "addedAt,desc";
	
/*
 	페이지
		GET 		/my-playlists								- 플레이리스트 페이지 	
		GET 		/playlists/{playlistId} 					- 특정 플레이리스트 조회 
	
	조회/생성/수정/삭제
	 	GET			/playlists?									- 1) 플레이리스트 목록 조회
	 	GET			/reviews_modal								- 2) 플레이리스트 목록 조회
	 	GET			/getMyPlaylist								- 3) 플레이리스트 목록 조회
		POST 		/playlists 									- 플레이리스트 생성 
		PUT 		/playlists/{playlistId} 					- 플레이리스트 수정  (X)
		DELETE 		/playlists/{playlistId} 					- 플레이리스트 삭제  (X)
	
	플리에 서평 추가/삭제
		GET			/playlists/{playlistId}/reviews				- 플리 서평 목록 조회
		POST		/playlists/{playlistId}/reviews				- 1)플리에 서평 추가
		POST		/playlists/{playlistId}/reviews/{reviewId}	- 2)플리에 서평 추가
		POST		/api/review_playlist						- 3)플리에 서평 추가
		DELETE		/playlists/{playlistId}/reviews/{reviewId}	- 플리에 서평 삭제
	
	좋아요/취소
		POST		/plyalists/{playlistId}/likes				- 플레이리스트 좋아요
		DELETE		/playlists/{playlistId}/likes	
 */
	
/*
 * 페이지
 	GET 		/my-playlists					- 페이지 
 	GET 		/playlists/{playlistId} 		- 특정 플레이리스트 조회
 */
	
	@GetMapping("/my-playlists")
	public String playlist(
//			@PathVariable(value = "nickname", required = true) String nickname, 
		    Model model,
		    @AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("PlaylistController.playlist()");  
		
//		if(!Objects.isNull(authUser) && nickname.equals(authUser.getNickname())) {
//			model.addAttribute("result", "sameUser");
//		} else {
//			model.addAttribute("result", "otherUser");
//		}
		
		String nickname = userDetails.getNickname();
		if(nickname.equals(userDetails.getNickname())) {
			model.addAttribute("result", "sameUser");
		} else {
			model.addAttribute("result", "otherUser");
		}
		
		Map<String, List<PlaylistEntity>> map = plService.findPlaylistsByNickname(nickname);
		
		model.addAttribute("likeList", map.get("likeList"));
		model.addAttribute("ppList", map.get("ppList"));
		model.addAttribute("myList", map.get("myList"));
		
		return "playlist/like-playlist";
	}
	
	@GetMapping("/playlists/{playlistId}")
	public String playlistFolder(
			@PathVariable(value = "playlistId") Long playlistId,
			@PageableDefault(size = PAGE_SIZE, page = CURRENT_PAGE) Pageable pageable,
			Model model,
			HttpSession session,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("PlaylistController.playlistFolder()"); 

		Map<String, Object> map = plService.findPlaylistFolder(userDetails.getUserId(), playlistId, pageable);
		
		model.addAttribute("reviewList", map.get("reviewList"));
		model.addAttribute("playlistCover", map.get("playlistCover"));
		model.addAttribute("startPageBtnNo", map.get("startPage"));
		model.addAttribute("endPageBtnNo", map.get("endPage"));
		model.addAttribute("result", map.get("result"));
		model.addAttribute("prev", null);
		model.addAttribute("next", null);
		
		return "playlist/click-playlist";
	}
	
/*	
 	조회/생성/수정/삭제
 	GET			/playlists									- 1) 플레이리스트 목록 조회
 	GET			/reviews_modal								- 2) 플레이리스트 목록 조회
 	GET			/getMyPlaylist								- 3) 플레이리스트 목록 조회
 	
	POST 		/playlists 									- 플레이리스트 생성 
	PUT 		/playlists/{playlistId} 					- 플레이리스트 수정  (X)
	DELETE 		/playlists/{playlistId} 					- 플레이리스트 삭제  (X)	
 */	
	
	// 모달> 내 플리 목록 불러오기 - 메인 페이지
	@ResponseBody
	@GetMapping("/playlists") // getMyPlaylistModal
	public ResponseEntity<List<PlaylistDTO>> getMyPlaylistModal(
			@RequestParam(value="reviewId") Long reviewId,
//			@RequestParam(value="playlistId") Long playlistId,
		    HttpSession session,
		    @AuthenticationPrincipal UserPrincipal userDetails
		    ) { 
		System.out.println("MainController.getMyPlaylistModal");
		
		List<PlaylistDTO> playlists = plService.findPlaylistsByUserIdAndReviewId(reviewId, userDetails.getUserId());
	
		return ResponseEntity.ok(playlists);		
	}
	
//	모달 > 플리 정보 - 서평 등록 페이지
	@ResponseBody
	@GetMapping("/getMyPlaylist")
//	@GetMapping("/playlists")
	public ResponseEntity<List<PlaylistEntity>> getMyPlaylist(
			HttpSession session,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("ReviewController.getMyPlaylist()");
	
		return ResponseEntity.ok(plService.findPlaylistsByUserId(userDetails.getUserId()));
	}
	
	/* 모달 > 리스트 + 페이징 - 플레이리스트 모달*/
	@ResponseBody
	@RequestMapping("/reviews_modal")
	public ResponseEntity<Map<String, Object>> modalListPage(
			@PageableDefault(size = MODAL_PAGE_SIZE, page = CURRENT_PAGE) Pageable pageable,
			@RequestParam(required = false) String query,
			HttpSession session,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("Controller.modalListPage");

//		Page<ReviewEntity> page = reviewService.findReviews(userDetails.getUserId(), query, pageable);
		Page<ReviewEntity> page = reviewService.findReviews(
			ReviewSearchCriteria.builder()
				.query(query)
				.authUserId(userDetails.getUserId())
				.build(), 
			pageable
		);
		Map<String, Integer> pagination = PagingUtil.pagination((int) page.getTotalElements(), MODAL_PAGE_SIZE, pageable.getPageNumber() + 1);
		
		return ResponseEntity.ok(Map.of(
			"reviewList", page.getContent(), 
			"startPage", pagination.get("startPage"), 
			"endPage", pagination.get("endPage")
		));
	}
	
	// 새로운 플리 만들기
	@ResponseBody
	@PostMapping("/playlists")
	public ResponseEntity<Object> addNewPlaylist(
			@RequestBody Map<String, String> map,
	   	    @AuthenticationPrincipal UserPrincipal userDetails
	   	    ) { 
		System.out.println("MainController.addNewPlaylist");

		PlaylistEntity playlist = plService.createPlaylist(map.get("playlistName"), userDetails.getUserId());
		
		return ResponseEntity.of(null);
	}
	
/*
 	플리에 서평 추가/삭제
	GET			/playlists/{playlistId}/reviews				- 플리 서평 목록 조회
	POST		/playlists/{playlistId}/reviews				- 1)플리에 서평 추가
	POST		/playlists/{playlistId}/reviews/{reviewId}	- 2)
	POST		/api/review_playlist						- 3)
	DELETE		/playlists/{playlistId}/reviews/{reviewId}	- 플리에 서평 삭제	
 */
	
	@ResponseBody
	@GetMapping("/playlists/{playlistId}/reviews")
	public ResponseEntity<Map<String, Object>> getPlaylistFolder(
			@PathVariable(value = "playlistId") Long playlistId,
			@PageableDefault(size = PAGE_SIZE, page = CURRENT_PAGE) Pageable pageable,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("PlaylistController.getPlaylistFolder()");
		
		Map<String, Object> map = plService.findPlaylistFolder(userDetails.getUserId(), playlistId, pageable);
				
		return ResponseEntity.ok(map);
	}
	
	/* 모달 > 1. 플리에 서평 추가 - 플레이리스트 페이지 */
	@PostMapping("/playlists/{playlistId}/reviews")
	public String addReviews(
	        @RequestParam("checkedReview") List<Integer> checkedReview,
	        @PathVariable Long playlistId
			) {
		System.out.println("PlaylistController.addReviews");
		
		reviewService.managePlaylistReviewAddList(checkedReview, playlistId);
		
		return "forward:/playlists/" + playlistId;
//		+ 		"?page=" + CURRENT_PAGE + 
//				"&size=" + MODAL_PAGE_SIZE + 
//				"&sort=" + STR_SORT;
	}
	
//	2. 플리에 서평 추가 - 서평 작성 페이지
	@ResponseBody
	@PostMapping("/playlists/{playlistId}/reviews/{reviewId}")
	public ReviewPlaylistEntity addReviewToPly(
//			@RequestBody Map<String, Long> map
			@PathVariable Long playlistId,
			@PathVariable Long reviewId
			) {
		System.out.println("ReviewWriterController.addReviewToPly()");
		
		return reviewService.managePlaylistReviewAdd(reviewId, playlistId);
	}
	
//	3. 플리에 서평 추가 - 메인 페이지
	@ResponseBody
	@PostMapping("/api/review_playlist")
	public ResponseEntity<Object> toggleReviewToPly(
			@RequestBody Map<String, Long> map
			) {
		System.out.println("MainController.toggleReviewToPly");
		
		//playlistService.addReviewListToPl(List.of(map.get("reviewId").intValue()), map.get("playlistId"));
		int httpStatus = reviewService.managePlaylistReviewToggle(map.get("reviewId"), map.get("playlistId"));
		
		return ResponseEntity.status(httpStatus).build();
	}
	
	/* 플리에서 서평 삭제 */
	@DeleteMapping("/playlists/{playlistId}/reviews/{reviewId}")
	public String deleteReviewFromPly(
		  @PathVariable Long playlistId,
		  @PathVariable Long reviewId,
	   	  @RequestParam(value = "page", required = false, defaultValue = "1") int crtPage,
	   	  Model model
		) {
		System.out.println("Controller.deleteReviewFromPly");

		reviewService.managePlaylistReviewRemove(reviewId, playlistId);
		
		return "forward:/playlists/" + playlistId
				+ "?page=" + CURRENT_PAGE; 
//				"&size=" + MODAL_PAGE_SIZE + 
//				"&sort=" + STR_SORT;
	}
	
//	@ResponseBody
//	@RequestMapping("/removeReviewAtPly")
//	public int removeReviewAtPly(@RequestParam(value="playlistNo")int playlistNo,
//								 @RequestParam(value="reviewNo")int reviewNo) {
//		System.out.println("ReviewWriterController > removeReviewAtPly");
//		System.out.println(">> " + playlistNo + "번 플레에서 " + reviewNo + "번 서평 삭제합니다");
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("playlistNo", playlistNo);
//		map.put("reviewNo", reviewNo);
//		
//		return mainDao.deleteReviewFromPly(map);
//	}
	
/*
 	좋아요/취소
	POST		/plyalists/{playlistId}/likes				- 플레이리스트 좋아요
	DELETE		/playlists/{playlistId}/likes	
 */
	
	/* 해당 플리 좋아요 & 좋아요 취소 */
	@ResponseBody
	@PostMapping("/playlists/{playlistId}/likes")
	public LikeStatus	likePlaylist(
			@PathVariable Long playlistId,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("PlaylistController.likePlaylist()"); 

		return plService.togglePlaylistLike(userDetails.getUserId(), playlistId);
	}
	
	/*
	@ResponseBody
	@DeleteMapping("/playlists/{playlistId}/likes")
	public Long unlikePlaylist(
//			@RequestParam(value="playlistId") Long playlistId,
			@PathVariable Long playlistId,
			HttpSession session,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("PlaylistController.unlikePlaylist()"); 

		return plService.toggleLikePlaylist(userDetails.getUserId(), playlistId);
	}
	*/
}
