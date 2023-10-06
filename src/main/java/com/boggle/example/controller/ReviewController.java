package com.boggle.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.adapter.AladinApiAdapter;
import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewPlaylistEntity;
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
	public String review_write(Model model) {
		System.out.println(">> ReviewWriteController.write()");
	  
		model.addAttribute("writeFormResponse", reviewService.writeForm());
		
		return "/WEB-INF/views/review_write/review_write.jsp";
	}
	
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
	
	//서평 등록
	@ResponseBody
	@PostMapping("/registerReview")
	public ResponseEntity<Long> addReview(
			@RequestBody RegisterReviewRequest reviewRequest,
			HttpSession session
		) throws JsonProcessingException {
		System.out.println(">> ReviewController.addReview()");
		
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
		
		if(!Objects.isNull(authUser)) {
			Long reviewId = reviewService.registerReview(reviewRequest, authUser.getUserId());
//			System.out.println(">> reviewId: " + reviewId);
			return ResponseEntity.ok(reviewId);
			
		} else {
			return ResponseEntity.badRequest().build(); // 로그인 페이지로 이동
		}
	}
	
	//모달>플리 정보
	/* 서평쓰기 (서평 플레이리스트에 추가 모달), 내 서재, 남서재, 상세페이지, 취향저격 홈 */
	@ResponseBody
	@GetMapping("/getMyPlaylist")
	public ResponseEntity<List<PlaylistEntity>> getMyPlaylist(HttpSession session) {
		System.out.println("ReviewController.getMyPlaylist()");
	
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//		LoginResponse authUser = LoginResponse.of(true, Long.parseLong("1"), "이강인", "강잉뉴", null);
	
		return ResponseEntity.ok(reviewService.getMyPlaylist(authUser.getUserId()));
	}
	
	//플리에 서평 저장
	@ResponseBody
	@PostMapping("/reviewPlaylist")
	public ReviewPlaylistEntity addReviewToPly(@RequestBody ReviewPlaylistEntity reviewPlayEntity) {
		System.out.println("ReviewWriterController.addReviewToPly()");
		
		return reviewService.addReviewToPlaylist(reviewPlayEntity);
	}

////	서평 등록
//	@ResponseBody
//	@RequestMapping("/addReview")
//	public Map<String, String> addReview(@RequestBody Map<String, Object> map,
//							HttpSession session) throws JsonProcessingException {
//		System.out.println("ReviewWriteController > addReview");
//	
//		System.out.println(map);
//
//		reviewWriteService.addReview(map);
//
//		UserVo authUser = (UserVo) session.getAttribute("authUser");
//		
//		String redirect;
//		
//		if(authUser == null) {
//			redirect = "user/loginForm";
//		} else {
//			redirect = authUser.getNickname();
//		}
//
//		Map<String, String> resultMap = new HashMap<String, String>();
//		resultMap.put("redirect", redirect);
//		resultMap.put("reviewNo", String.valueOf(map.get("reviewNo")) );
//		
//		return resultMap;
//	}
//	
////	책 정보
//	@ResponseBody
//	@RequestMapping("/getBookInfo")
//	public List<Map<String, Object>> getBookInfo(@RequestParam(value="bookTitle") String bookTitle) throws JsonParseException, JsonMappingException, IOException {
//		System.out.println("ReviewWriteController.getBookInfo");
//
//		return reviewWriteService.getBookInfo(bookTitle);
//	}
//	
////	서평 정보
//	@ResponseBody
//	@RequestMapping("/getPrevReviewInfo")
//	public Map<String, Object> getPrevReviewInfo(@RequestParam(value="reviewNo") int reviewNo) {
//		System.out.println("ReviewWriteController.getPrevReviewInfo");
//		
//		System.out.println("reviewNo: " + reviewNo);
//		
//		return reviewWriteService.getPrevReviewInfo(reviewNo);
//	}
//	
////	서평 수정
//	@ResponseBody
//	@RequestMapping("/modifyReview")
//	public Map<String, String> modifyReview(@RequestBody Map<String, Object> map,
//											HttpSession session) {
//		System.out.println("ReviewWriteController.modifyReview");
//		
//		System.out.println("요청에서 받은 값: " + map);
//		//{bookNo=9791166686603, bookTitle=다섯 번째 감각, author=김보영, bookURL=https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=287367114, genreNo=1, coverURL=https://image.aladin.co.kr/product/28736/71/cover500/k442836780_1.jpg, userNo=6, styleNo=5, reviewContent=세상은 원래부터 기괴하고 무섭고 아름답고 당황스러웠다. 그동안 우리는 두꺼운 습관의 담요를 뒤집어 쓰고 이를 무시하고 있었을 뿐이다. 그리고 김보영의 단편들을 읽는 것은 그 담요를 은근슬적 떨구는 과정이다. , genreName=소설/시/희곡, reviewNo=3}
//
//		int result = reviewWriteDao.checkReviewWriter(map);
//		
//		System.out.println("수정한 review를 userNo가 작성했는지: " + result);
//		
//		UserVo authUser = (UserVo) session.getAttribute("authUser");
//		
//		String redirect = null;
//		
//		if(result == 0) { // 작성자가 남의 서평을 수정하려고 할 경우
//			redirect =  "user/loginForm";
//		}
//		else { // 수정
//			int result2 = reviewWriteService.modifyReview(map);
//			
//			if(result2 == 1) {
//				redirect = authUser.getNickname();
//			}
//		}
//		
//		Map<String, String> resultMap = new HashMap<String, String>();
//		resultMap.put("redirect", redirect);
//		
//		return resultMap;
//	}
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
