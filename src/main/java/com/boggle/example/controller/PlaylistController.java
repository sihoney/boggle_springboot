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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.service.MyBookService;
import com.boggle.example.service.PlaylistService;
import com.boggle.example.util.PagingUtil;


@Controller
public class PlaylistController {
	
	@Autowired
	PlaylistService plService;
	@Autowired
	MyBookService mybookService;
	
	final int PAGE_SIZE = 5;
	final int MODAL_PAGE_SIZE = 5;
	final int CURRENT_PAGE = 0;
	final Sort SORT = Sort.by(Sort.Order.desc("addedAt"));
	final String STR_SORT = "addedAt,desc";
	
	@RequestMapping("/{nickname}/playlist")
	public String playlist(
			@PathVariable(value = "nickname", required = true) String nickname, 
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
	
	@RequestMapping("/playlist_folder/{playlistId}")
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

	@ResponseBody
	@RequestMapping("/playlists/{playlistId}")
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
	
	/* 해당 플리 좋아요 & 좋아요 취소 */
	@ResponseBody
	@RequestMapping("/playlist_user")
	public Long addplaylistLike(
			@RequestParam(value="playlistId") Long playlistId,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("Controller.addplaylistLike");
		
//		if (session.getAttribute("authUser") == null) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//		}
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 

		return plService.toggleLikePlaylist(userDetails.getUserId(), playlistId);
	}
	
	/* 서평 좋아요 & 좋아요 취소 */
	@ResponseBody
	@PostMapping("/review_user")
	public ResponseEntity<Object> toggleLikeReview(
			@RequestParam("reviewId") Long reviewId,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
		) {
		System.out.println("PlaylistController.toggleLikeReview");
		
//		if (session.getAttribute("authUser") == null) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//		}
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 

		Integer httpStatus = mybookService.likeOrDislikeReview(userDetails.getUserId(), reviewId);
		
		return ResponseEntity.status(httpStatus).build();
	}
	
	/* 플리에서 서평 삭제 */
	@DeleteMapping("/review_playlist")
	public String deleteReviewFromPly(
		  @RequestParam("reviewId") Long reviewId,
	   	  @RequestParam("playlistId") Long playlistId,
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
	
	/* 모달 > 리스트 + 페이징 */
	/* 플리 모달 검색창 */
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
	
	/* 모달 > 서평 등록 */
	@PostMapping("/review_playlist")
	public String addReviews(
	        @RequestParam("checkedReview") List<Integer> checkedReview,
	        @RequestParam("playlistId") Long playlistId,
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
}
