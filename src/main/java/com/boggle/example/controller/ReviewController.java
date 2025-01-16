package com.boggle.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.adapter.AladinApiAdapter;
import com.boggle.example.dto.ApiResponse;
import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.dto.LikeRequest;
import com.boggle.example.dto.RegisterReviewRequest;
import com.boggle.example.dto.ReviewListResponse;
import com.boggle.example.dto.SearchedBooksResponse;
import com.boggle.example.entity.PlaylistEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.ReviewPlaylistEntity;
import com.boggle.example.service.MainService;
import com.boggle.example.service.MyBookService;
import com.boggle.example.service.ReviewService;
import com.boggle.example.util.EmotionEnum;
import com.boggle.example.util.PagingUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class ReviewController {
	
	@Autowired
	MyBookService mybookService;
	@Autowired
	ReviewService reviewService;
	@Autowired
	AladinApiAdapter aladinApiAdapter;
	@Autowired
	MainService mainService;
	
	Integer TOTAL_EMOTION = 8;
	final int SIZE = 5;
	
/*
 	페이지 
 	GET		/boggle							메인 페이지
	GET		/my-reviews						유저 페이지
	GET		/reviews/new					서평 등록 페이지	
	GET		/reviews/edit					서평 수정 페이지
	
	GET		/searchbook						aladin-api 책 검색
	
	GET			/api/reviews				서평 목록 불러오기 - mybook 페이지
	GET			/api/review					서평 목록 불러오기 - main 페이지
	POST 		/reviews					게시물 작성 
	PUT 		/reviews/{id}				게시물 수정 
	DELETE 		/reviews/{id} 				게시물 삭제	
	
	POST		/reviews/{reviewId}/likes 
	DELETE		/reviews/{reviewId}/likes
	GET			/reviews/{reviewId}/likes/count		좋아요 개수 조회
	GET			/reviews/{reviewId}/likes/me		현재 사용자의 좋아요 상태 확인
 */
	
	/* 메인 페이지 */
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
	
	/* my-reviews 페이지 */
//	@Secured("ROLE_ADMIN")
	@RequestMapping("/my-reviews")
	public String mybook (
//			@PathVariable(value = "nickname", required=true) String nickname,
			@PageableDefault(size = 8, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, //, sort = "createdAt,desc"
			Model model,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.print("ReviewController.mybook()");
//		System.out.println("nickname: " + nickname);
		
		System.out.println(userDetails.getNickname());
		String nickname = userDetails.getNickname();
		
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
		Page<ReviewEntity> pageObj = mybookService.getReviewsByCreatedAt(nickname, null, pageable, userDetails.getUserId());
		Map<String, Integer> map = PagingUtil.pagination((int) pageObj.getTotalElements(), pageable.getPageSize(), pageable.getPageNumber() + 1);
		
		model.addAttribute("nickname", nickname);
		model.addAttribute("emotionList", EmotionEnum.getList());
		model.addAttribute("userProfile", mybookService.getUserProfile(nickname));
		model.addAttribute("reviewList", pageObj.getContent());
//		model.addAttribute("sort", pageObj.getPageable().getSort().toString());
		model.addAttribute("startPage", map.get("startPage"));
		model.addAttribute("endPage", map.get("endPage"));
		
		return "mybook/mybook_review";
	}
	
	/* 서평 등록 페이지 */
	@GetMapping("/reviews/new")
	public String registerReview(
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
		
		return "review_write/review_write";
	}
	
	/* 서평 수정 페이지 */
	@GetMapping("/reviews/edit")
	public String editReview(
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
		
		return "review_write/review_write";
	}
	
/*
	GET	/searchbook		aladin-api 책 검색	
 */
	
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
	
/*
	GET			/reviews-with-style		서평 목록 불러오기 - main 페이지
	GET			/reviews				서평 목록 불러오기 - mybook 페이지
	POST 		/reviews				게시물 작성 
	PUT 		/reviews/{id}			게시물 수정 
	DELETE 		/reviews/{id} 			게시물 삭제
 */	
	
	@ResponseBody
	@GetMapping("/reviews-with-style") 
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
	
//	서평 리스트 api
   @ResponseBody  
   @GetMapping("/reviews")  // /api/reviews -> /reviews 
   public ResponseEntity<ReviewListResponse> getReviews (
		   @RequestParam(value = "userId", required=false) Long userId,
		   @RequestParam(value = "emotionName", required=false) String emotionName, 
           @PageableDefault(size = 8, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		   HttpSession session,
           @AuthenticationPrincipal CustomUserDetails userDetails
           ){
	   System.out.println("MyBookController.getReviews()");

//	   LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//	  if (Objects.isNull(authUser)) {
//		   System.out.println("세션 만료");
//		   return ResponseEntity.badRequest().build();
//	   }
	  
	  String property = pageable.getSort().get().findFirst().orElse(null).getProperty();

	  Page<ReviewEntity> pageObj;
	  if(property.equals("likeCount")) {
		  System.out.println(">> 인기순");
		  
		  pageObj = mybookService.getReviewsOrderByLikeCount(
				  userId, 
				  userDetails.getUserId(), 
				  pageable);
	  }
	  else if("undefined".equals(emotionName) || "null".equals(emotionName) || Objects.isNull(emotionName) ) {
		  System.out.println(">> 최신순");
		  
		  pageObj = mybookService.getReviewsByCreatedAt(
				  null, 
				  userId, 
				  pageable, 
				  userDetails.getUserId());
	  }
	  else {
		  System.out.println(">> 감정별");
		  
		  pageObj = mybookService.getReviewByEmotion(
				  userId, 
				  emotionName, 
				  pageable, 
				  userDetails.getUserId());
	  }

	   Map<String, Integer> map = PagingUtil.pagination(
			   (int) pageObj.getTotalElements(), 
			   pageable.getPageSize(), 
			   pageable.getPageNumber() + 1);

	   return ResponseEntity.ok(
				   ReviewListResponse.of(
				   pageObj.toList(), 
				   pageObj.getPageable().getSort().toString(),
				   null, 
				   (int) map.get("startPage"), 
				   (int) map.get("endPage"))
			   );
   }	
	
	/* 서평 등록 */
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
	
	/* 서평 수정 */
	@ResponseBody
	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<Long> modifyReview(
			@PathVariable Long reviewId,
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
	
  /* 서평 삭제 */
  @ResponseBody 
  @DeleteMapping("/reviews/{reviewId}")
  public ResponseEntity deleteReview(
		   @PathVariable Long reviewId,
//		   @RequestParam(name = "reviewId", required=true) Long reviewId,
		   HttpSession session,
		   @AuthenticationPrincipal CustomUserDetails userDetails
		   ) {	   
	   try {
		   System.out.println("MyBookController.deleteReview()");
		   
//		   LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//		   if(Objects.isNull(authUser)) {
//			   return new ResponseEntity<>(
//					   "세션 정보 만료", 
//					   HttpStatus.BAD_GATEWAY
//					   );
//		   }

		   mybookService.deleteReview(userDetails.getUserId(), reviewId); 
		   
		   return new ResponseEntity<>(
				   "Entity with ID " + reviewId + " has been deleted successfully", 
				   HttpStatus.OK
				   );
	   } catch(Exception e) {
		   return new ResponseEntity<>(
				   "Failed to delete entity with ID " + reviewId, 
				   HttpStatus.INTERNAL_SERVER_ERROR
				   );
	   }
  }
	
/*
 	좋아요 기능
	POST		/reviews/{reviewId}/likes 
	DELETE		/reviews/{reviewId}/likes
	GET			/reviews/{reviewId}/likes/count		좋아요 개수 조회
	GET			/reviews/{reviewId}/likes/me		현재 사용자의 좋아요 상태 확인
 */
	
  /* 서평 좋아요 & 좋아요 취소*/
  @ResponseBody
  @PostMapping("/reviews/{reviewId}/likes")
  public ResponseEntity<String> like(
		  @PathVariable Integer reviewId,
		   @RequestBody LikeRequest likeRequest,
		   HttpSession session,
		   @AuthenticationPrincipal CustomUserDetails userDetails
		   ) {
	   System.out.println("MyBookController.like()");

//	   LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
	   Integer result = mybookService.likeOrDislikeReview(userDetails.getUserId(), likeRequest.getReviewId());
	   return ResponseEntity.status(result).build();
  }	
  
	/* 좋아요 취소 */
  	/*
	@ResponseBody
	@DeleteMapping("/reviews/{reviewId}/likes")
	public ResponseEntity<Object> toggleLikeReview(
//			@RequestParam("reviewId") Long reviewId,
			@PathVariable Long reviewId,
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
	*/
}
