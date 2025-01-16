package com.boggle.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.dto.PlaylistDTO;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewPlaylistEntity;
import com.boggle.example.service.MainService;
import com.boggle.example.service.MyBookService;
import com.boggle.example.service.PlaylistService;
import com.boggle.example.service.ReviewService;
import com.boggle.example.util.PagingUtil;


@Controller
public class PlaylistController {
	
	@Autowired
	PlaylistService plService;
	@Autowired
	MyBookService mybookService;
	@Autowired
	ReviewService reviewService;
	@Autowired
	MainService mainService;
	
	final int PAGE_SIZE = 5;
	final int MODAL_PAGE_SIZE = 5;
	final int CURRENT_PAGE = 0;
	final Sort SORT = Sort.by(Sort.Order.desc("addedAt"));
	final String STR_SORT = "addedAt,desc";
	
/*
	GET 		/my-playlists								- 플레이리스트 페이지 	
	GET 		/playlists/{playlistId} 					- 특정 플레이리스트 조회 
	
 	GET			/playlists?									- 1) 플레이리스트 목록 조회
 	GET			/reviews_modal								- 2) 
 	GET			/getMyPlaylist								- 3)
	POST 		/playlists 									- 플레이리스트 생성 
	PUT 		/playlists/{playlistId} 					- 플레이리스트 수정  (X)
	DELETE 		/playlists/{playlistId} 					- 플레이리스트 삭제  (X)
	
	GET			/playlists/{playlistId}/reviews				- 플리 서평 목록 조회
	POST		/playlists/{playlistId}/reviews				- 1)플리에 서평 추가
	POST		/playlists/{playlistId}/reviews/{reviewId}	- 2)
	POST		/api/review_playlist						- 3)
	DELETE		/playlists/{playlistId}/reviews/{reviewId}	- 플리에 서평 삭제
	
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
		    HttpSession session, 
		    Model model,
		    @AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("PlaylistController.playlist()");
		
//		if (session.getAttribute("authUser") == null || 
//				session.getAttribute("authUser").equals("") || 
//				nickname.equals(null)) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//			   
//			   return "/WEB-INF/views/user/loginForm.jsp";
//		}		
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");  
		
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
		
		Map<String, List<PlaylistEntity>> map = plService.getPlaylists(nickname);
		
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
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("PlaylistController.playlistFolder()");
		
//		if (session.getAttribute("authUser") == null ) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//			   
//			   return "/WEB-INF/views/user/loginForm.jsp";
//		}	
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 

		Map<String, Object> map = plService.getPlaylistFolder(userDetails.getUserId(), playlistId, pageable);
		
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
 	GET			/playlists?									- 1) 플레이리스트 목록 조회
 	GET			/reviews_modal								- 2) 
 	GET			/getMyPlaylist								- 3)
 	
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
		    @AuthenticationPrincipal CustomUserDetails userDetails
		    ) { 
		System.out.println("MainController.getMyPlaylistModal");
		
		List<PlaylistDTO> playlists = mainService.getMyPlaylists(reviewId, userDetails.getUserId());
	
		return ResponseEntity.ok(playlists);		
		
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
		
//		if(authUser != null) {
//			List<PlaylistDTO> playlists = mainService.getMyPlaylists(reviewId, authUser.getUserId());
//		
//			return ResponseEntity.ok(playlists);
//		}
		
//		return ResponseEntity.internalServerError().build();
	}
	
//	모달 > 플리 정보 - 서평 등록 페이지
	@ResponseBody
	@GetMapping("/getMyPlaylist")
//	@GetMapping("/playlists")
	public ResponseEntity<List<PlaylistEntity>> getMyPlaylist(
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("ReviewController.getMyPlaylist()");
	
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//		LoginResponse authUser = LoginResponse.of(true, Long.parseLong("1"), "이강인", "강잉뉴", null);
	
		return ResponseEntity.ok(reviewService.getMyPlaylist(userDetails.getUserId()));
	}
	
	/* 모달 > 리스트 + 페이징 - 플레이리스트 모달*/
	@ResponseBody
	@RequestMapping("/reviews_modal")
	public ResponseEntity<Map<String, Object>> modalListPage(
			@PageableDefault(size = MODAL_PAGE_SIZE, page = CURRENT_PAGE) Pageable pageable,
			@RequestParam(required = false) String query,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("Controller.modalListPage");
		
//		if (session.getAttribute("authUser") == null) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//		}
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 

		Page<ReviewEntity> page = plService.getAllReviewByUserId(userDetails.getUserId(), query, pageable);
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
	   	    HttpSession session,
	   	    @AuthenticationPrincipal CustomUserDetails userDetails
	   	    ) { 
		System.out.println("MainController.addNewPlaylist");

		int httpStatus = mainService.makePlaylist(map.get("playlistName"), userDetails.getUserId());
		
		return ResponseEntity.status(httpStatus).build();
	}
	
/*
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
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("PlaylistController.getPlaylistFolder()");
		
//		if (session.getAttribute("authUser") == null) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//		}
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 
		
		Map<String, Object> map = plService.getPlaylistFolder(userDetails.getUserId(), playlistId, pageable);
				
		return ResponseEntity.ok(map);
	}
	
	/* 모달 > 1. 플리에 서평 추가 - 플레이리스트 페이지 */
	@PostMapping("/playlists/{playlistId}/reviews")
	public String addReviews(
	        @RequestParam("checkedReview") List<Integer> checkedReview,
	        @PathVariable Long playlistId,
			HttpSession session
			) {
		System.out.println("PlaylistController.addReviews");
		
//		if (session.getAttribute("authUser") == null) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//		}
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 
		
		plService.addReviewListToPl(checkedReview, playlistId);
		
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
		
		return reviewService.addReviewToPlaylist(reviewId, playlistId);
	}
	
//	3. 플리에 서평 추가 - 메인 페이지
	@ResponseBody
	@PostMapping("/api/review_playlist")
	public ResponseEntity<Object> toggleReviewToPly(
			@RequestBody Map<String, Long> map
			) {
		System.out.println("MainController.toggleReviewToPly");
		
		//playlistService.addReviewListToPl(List.of(map.get("reviewId").intValue()), map.get("playlistId"));
		int httpStatus = mainService.toggleReviewPlaylist(map.get("reviewId"), map.get("playlistId"));
		
		return ResponseEntity.status(httpStatus).build();
	}
	
	/* 플리에서 서평 삭제 */
	@DeleteMapping("/playlists/{playlistId}/reviews/{reviewId}")
	public String deleteReviewFromPly(
//		  @RequestParam("reviewId") Long reviewId,
//	   	  @RequestParam("playlistId") Long playlistId,
		  @PathVariable Long playlistId,
		  @PathVariable Long reviewId,
	   	  @RequestParam(value = "page", required = false, defaultValue = "1") int crtPage,
	   	  Model model,
	   	  HttpSession session
		) {
		System.out.println("Controller.deleteReviewFromPly");

		plService.deleteReviewFromPlaylist(reviewId, playlistId);
		
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
	POST		/plyalists/{playlistId}/likes				- 플레이리스트 좋아요
	DELETE		/playlists/{playlistId}/likes	
 */
	
	/* 해당 플리 좋아요 & 좋아요 취소 */
	@ResponseBody
	@PostMapping("/playlists/{playlistId}/likes")
	public Long	likePlaylist(
//			@RequestParam(value="playlistId") Long playlistId,
			@PathVariable Long playlistId,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("PlaylistController.likePlaylist()");
		
//		if (session.getAttribute("authUser") == null) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//		}
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 

		return plService.toggleLikePlaylist(userDetails.getUserId(), playlistId);
	}
	
	/*
	@ResponseBody
	@DeleteMapping("/playlists/{playlistId}/likes")
	public Long unlikePlaylist(
//			@RequestParam(value="playlistId") Long playlistId,
			@PathVariable Long playlistId,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("PlaylistController.unlikePlaylist()");
		
//		if (session.getAttribute("authUser") == null) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//		}
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 

		return plService.toggleLikePlaylist(userDetails.getUserId(), playlistId);
	}
	*/
}
