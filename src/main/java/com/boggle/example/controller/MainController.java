package com.boggle.example.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.dto.PlaylistDTO;
import com.boggle.example.service.MainService;
import com.boggle.example.service.PlaylistService;

@Controller
public class MainController {

	@Autowired
	MainService mainService;
	@Autowired
	PlaylistService playlistService;
	
	Integer TOTAL_EMOTION = 8;
	final int SIZE = 5;
	
	@RequestMapping("/boggle")
	public String main(
			@PageableDefault(page = 0, size = SIZE, sort = "createdAt", direction=Direction.DESC) Pageable pageable,
			Model model,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("MainController.main()");
		
		Long authUserId;
		
		if (userDetails != null) {
		    // userDetails 객체가 null이 아닌 경우에만 getUserId() 메서드를 호출합니다.
			authUserId = userDetails.getUserId();
		} else {
		    // userDetails 객체가 null인 경우에 대한 처리를 수행합니다.
		    // 예: 로깅 또는 기본값 설정 등
			authUserId = null;
		}		
		
//		Long authUserId = userDetails.getUserId();
		Map<String, Object> mainMap = mainService.main(authUserId, pageable);
		
//		model.addAttribute("reviewList", mainMap.get("reviewList"));
//		model.addAttribute("pagination", mainMap.get("pagination"));
		model.addAttribute("emotionList", mainMap.get("emotionList"));
		model.addAttribute("playlists", mainMap.get("playlists"));
		
		return "main/main";
	}
	
	@ResponseBody
	@GetMapping("/api/review") 
	public Map<String, Object> apiReview(
			 @RequestParam(value="playlistId", required=false) Long playlistId,
			 @RequestParam(value="emotionId", required=false) Long emotionId,
			 @PageableDefault(page = 0, size = SIZE) Pageable pageable,
			 HttpSession session,
			 @AuthenticationPrincipal CustomUserDetails userDetails
			 ) {
		System.out.println("MainController.apiReview()");

		Long authUserId;
		if(userDetails != null) {
			authUserId = userDetails.getUserId();
		}
		authUserId = null;
		
		if(playlistId != null)
			return mainService.reviewsByPlaylistId(playlistId, authUserId, pageable);
		else if (emotionId != null){
			return mainService.reviewsByEmotionId(emotionId, authUserId, pageable);
		} else {
			return mainService.reviewsByEmotionId((long) Math.floor(TOTAL_EMOTION * Math.random()), authUserId, pageable);
		}
	}
	
	// 모달> 내 플리 목록 불러오기
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
	
	// 플리에 서평 추가
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
	
	// 새로운 플리 만들기
	@ResponseBody
	@PostMapping("/api/playlist")
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
	// 플리 재생 (특정 플리, 특정 감정 플리, 랜덤)
	@ResponseBody
	@GetMapping("/api/playlist/{playlistId}") 
	public Map<String, Object> getReviewList(
			 @PathVariable(value="playlistId", required=false) Long playlistId,
			 @PageableDefault(page = 0, size = 5) Pageable pageable,
			 HttpSession session
			 ) {
		System.out.println("MainController.getReviewList()");
		
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");

		return null;
	}
	
	// 서평작성 페이지에서 사용!(review_write)
	@ResponseBody
	@RequestMapping("/emotion")
	public List<Map<String, Object>> getEmotion() {
		System.out.println("MainController > getEmotion()");
		
		return mainService.getEmotion();
	}
	*/
}
