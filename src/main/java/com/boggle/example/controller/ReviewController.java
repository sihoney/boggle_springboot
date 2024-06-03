package com.boggle.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.adapter.AladinApiAdapter;
import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.domain.ReviewPlaylistEntity;
import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.service.ReviewService;
import com.boggle.example.util.PagingUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class ReviewController {
	
	@Autowired
	ReviewService reviewService;
	@Autowired
	AladinApiAdapter aladinApiAdapter;
	
	@RequestMapping("/write")
	public String review_write(
			@RequestParam(value = "isbn", required = false) Long isbn,
			@RequestParam(value = "reviewId", required = false) Long reviewId,
			HttpSession session,
			Model model,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println(">> ReviewWriteController.write()");
	  
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//		if(Objects.isNull(authUser)) {
//			return "/WEB-INF/views/user/loginForm.jsp";
//		}
		
		model.addAttribute("writeFormResponse", reviewService.writeForm(isbn, reviewId));
		
		return "/WEB-INF/views/review_write/review_write.jsp";
	}
	
//	책 api 검색
	@ResponseBody
	@GetMapping(value="/searchbook") //, produces="applicatioin/json;charset=UTF-8"
	public SearchedBooksResponse searchbook(
			@RequestParam String query,
			@PageableDefault(size = 4, page = 1)Pageable pageable
			) throws Exception  {
		System.out.println(">> ReviewWriteController.searchbook()");

		ApiResponse apiResponse = aladinApiAdapter.searchBooks(query, pageable);
		Map<String, Integer> paging = PagingUtil.pagination(apiResponse.getTotalResults(), pageable.getPageSize(), pageable.getPageNumber());
		
		return SearchedBooksResponse.of(apiResponse.getItem(), paging.get("startPage"), paging.get("endPage"), paging.get("totalPageNo"));
	}
	
//	서평 등록
	@ResponseBody
	@PostMapping("/reviews")
	public ResponseEntity<Long> addReview(
			@RequestBody RegisterReviewRequest reviewRequest,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
		) throws JsonProcessingException {
		System.out.println(">> ReviewController.addReview()");
		
		Long reviewId = reviewService.registerReview(reviewRequest, userDetails.getUserId());

		return ResponseEntity.ok(reviewId);		
		
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//		if(Objects.isNull(authUser)) {
//			return ResponseEntity.badRequest().build(); // 로그인 페이지로 이동		
//		} else {
//			Long reviewId = reviewService.registerReview(reviewRequest, authUser.getUserId());
//
//			return ResponseEntity.ok(reviewId);
//		}
	}
	
//	서평 수정
	@ResponseBody
	@PutMapping("/reviews")
	public ResponseEntity<Long> modifyReview(
			@RequestBody RegisterReviewRequest registerReview
			) {
		System.out.println("ReviewWriteController.modifyReview");

//		System.out.println(registerReview.getReviewId());
		
		ReviewEntity result = reviewService.updateReview(registerReview);
		
		if(result == null) {
			return ResponseEntity.badRequest().build();
		} else {
			return ResponseEntity.ok(result.getReviewId());
		}
	}
	
//	모달 > 플리 정보
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
	
//	서평 플리 추가
	@ResponseBody
	@PostMapping("/reviewPlaylist")
	public ReviewPlaylistEntity addReviewToPly(
			@RequestBody Map<String, Long> map
			) {
		System.out.println("ReviewWriterController.addReviewToPly()");
		
		return reviewService.addReviewToPlaylist(map.get("reviewId"), map.get("playlistId"));
	}

//		
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
}
