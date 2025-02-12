package com.boggle.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import com.boggle.example.dto.BookDTO;
import com.boggle.example.dto.LikeRequest;
import com.boggle.example.dto.LikeResponse;
import com.boggle.example.dto.LikeResult;
import com.boggle.example.dto.ReviewSearchCriteria;
import com.boggle.example.dto.SearchedBooksResponse;
import com.boggle.example.dto.review.RegisterReviewRequest;
import com.boggle.example.dto.review.ReviewListResponse;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.UserPrincipal;
import com.boggle.example.exception.ApiException;
import com.boggle.example.exception.ExternalApiException;
import com.boggle.example.exception.InvalidRequestException;
import com.boggle.example.service.BookService;
import com.boggle.example.service.EmotionService;
import com.boggle.example.service.ReviewService;
import com.boggle.example.service.UserService;
import com.boggle.example.util.EmotionEnum;
import com.boggle.example.util.PagingUtil;
import com.boggle.example.util.PagingUtil.PagingResult;
import com.boggle.example.util.SearchType;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

//추가로 개선할 수 있는 부분들:
//예외 처리 추가
//API 문서화 (Swagger/OpenAPI)
//응답 객체의 표준화
//캐싱 전략 도입
//요청 값 검증(Validation) 추가

//추가로 고려할 수 있는 개선 사항:
//API 문서화 (Swagger/OpenAPI)
//요청/응답 DTO 분리
//캐싱 전략 도입
//트랜잭션 관리 강화
//테스트 코드 작성

@Slf4j
@Controller
public class ReviewController {
	
	@Autowired
	ReviewService reviewService;
	@Autowired
	AladinApiAdapter aladinApiAdapter;
	@Autowired
	BookService bookDetailService;
	@Autowired
	EmotionService emotionService;
	@Autowired
	UserService userService;
	
	Integer TOTAL_EMOTION = 8;
	final int MAIN_PAGE_SIZE = 5;
	final int MYBOOK_PAGE_SIZE = 8;
	final int BOOK_PAGE_SIZE = 8;
	
/*
	페이지 
	 	GET			/boggle							메인 페이지
		GET			/my-reviews						유저 페이지
		GET			/reviews/new					서평 등록 페이지	
		GET			/reviews/edit					서평 수정 페이지
		GET			/reviews/{reviewId}				서평 이미지 페이지
		
	알라딘 API
		GET			/searchbook						aladin-api 책 검색
		
	API
		GET			/reviews-with-style				서평 목록 불러오기 - main 페이지
		GET			/reviews						서평 목록 불러오기 - mybook 페이지
		GET			/api/books/{isbn}				특정 책의 서평 목록 조회 - book detail 페이지
		POST 		/reviews						게시물 작성 
		PUT 		/reviews/{id}					게시물 수정 
		DELETE 		/reviews/{id} 					게시물 삭제
		
	좋아요
		POST		/reviews/{reviewId}/likes 
		DELETE		/reviews/{reviewId}/likes
		GET			/reviews/{reviewId}/likes/count	좋아요 개수 조회
		GET			/reviews/{reviewId}/likes/me	현재 사용자의 좋아요 상태 확인
 */
	

/*
 	페이지 
	 	GET			/boggle							메인 페이지
		GET			/my-reviews						유저 페이지
		GET			/reviews/new					서평 등록 페이지	
		GET			/reviews/edit					서평 수정 페이지
		GET			/reviews/{reviewId}				서평 이미지 페이지	
 */	
	
	/* 메인 페이지 */
	@RequestMapping("/boggle")
	public String main(
			@PageableDefault(page = 0, size = MAIN_PAGE_SIZE, sort = "createdAt", direction=Direction.DESC) Pageable pageable,
			Model model,
			HttpSession session,
//			@AuthenticationPrincipal OAuth2User userPrincipal
			@AuthenticationPrincipal UserPrincipal userPrincipal
			) {
		System.out.println("MainController.main()");
		System.out.println(userPrincipal);
		
		Map<String, Object> mainMap;
		if (Objects.isNull(userPrincipal)) {
		    // userDetails 객체가 null인 경우에 대한 처리를 수행합니다.
		    // 예: 로깅 또는 기본값 설정 등
			mainMap = emotionService.main(null, pageable);
		} else {
		    // userDetails 객체가 null이 아닌 경우에만 getUserId() 메서드를 호출합니다.
			mainMap = emotionService.main(
				(Long) userPrincipal.getAttributes().get("userId"), 
				pageable
			);
		}		
		
//		model.addAttribute("reviewList", mainMap.get("reviewList"));
//		model.addAttribute("pagination", mainMap.get("pagination"));
		model.addAttribute("emotionList", mainMap.get("emotionList"));
		model.addAttribute("playlists", mainMap.get("playlists"));
		
		return "main/main";
	}	
	
	/* my-reviews 페이지 */
//	@Secured("ROLE_ADMIN")
	@RequestMapping("/my-reviews/{nickname}")
	public String mybook (
			@PathVariable(value = "nickname", required=true) String nickname,
			@PageableDefault(size = 8, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, //, sort = "createdAt,desc"
			Model model,
			HttpSession session,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("ReviewController.mybook()");		
		System.out.println(nickname);
		
//		Page<ReviewEntity> pageObj = reviewService.findReviewsByCreatedAt(
//				nickname, 
//				null, 
//				pageable, 
//				userDetails.getUserId()
//				);
		Page<ReviewEntity> pageObj = reviewService.findReviews(
			ReviewSearchCriteria.builder()
			.searchType(SearchType.DATE)
			.authUserId(userDetails.getUserId())
			.nickname(nickname)
			.build(), 
			pageable
		);
		Map<String, Integer> map = PagingUtil.pagination(
				(int) pageObj.getTotalElements(), 
				pageable.getPageSize(), 
				pageable.getPageNumber() + 1
				);
		
		model.addAttribute("nickname", nickname);
		model.addAttribute("pageUser", userService.findUserByNickname(nickname));
		
		model.addAttribute("emotionList", EmotionEnum.getList());
		model.addAttribute("reviewList", pageObj.getContent());
		model.addAttribute("startPage", map.get("startPage"));
		model.addAttribute("endPage", map.get("endPage"));
//		model.addAttribute("sort", pageObj.getPageable().getSort().toString());
		
		return "mybook/mybook_review";
	}
	
	/* 서평 등록 페이지 */
	@GetMapping("/reviews/new")
	public String registerReviewPage(
			@RequestParam(value = "isbn", required = false) Long isbn,
			@RequestParam(value = "reviewId", required = false) Long reviewId,
			Model model,
			@AuthenticationPrincipal UserPrincipal userDetails) {
		System.out.println("ReviewController.registerReviewPage()");
		
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
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println(">> ReviewWriteController.write()");
		
		model.addAttribute("writeFormResponse", reviewService.writeForm(isbn, reviewId));
		
		return "review_write/review_write";
	}
	
	/* 서평 이미지 페이지 */
	@RequestMapping("/reviews/{reviewId}")
	public String getViewOfReview(
			@PathVariable(value = "reviewId") Long reviewId,
			Model model
			) {
		System.out.println("ViewController.viewer()");
		
		model.addAttribute("review", reviewService.findReview(reviewId));
		
		return "viewer/viewer";
	}
	
/*
	알라딘 API
	GET			/searchbook				aladin-api 책 검색	
 */

    /**
     * 도서 검색 API
     * @param query 검색어
     * @param pageable 페이징 정보
     * @return 검색된 도서 목록과 페이징 정보
     * @throws ExternalApiException 
     */
	@ResponseBody
	@GetMapping(value="/searchbook") //, produces="applicatioin/json;charset=UTF-8"
	public ResponseEntity<ApiResponse<SearchedBooksResponse>> searchBooks(
			@RequestParam @NotBlank(message = "검색어는 필수입니다.") String query,
			@PageableDefault(size = 4, page = 1) Pageable pageable) {
		log.info("Searching books with query: {}, page: {}", query, pageable.getPageNumber());

		try {
            // 알라딘 API 호출
            ApiResponse<List<BookDTO>> aladinResponse = aladinApiAdapter.searchBooks(query, pageable);
            
            // 페이징 정보 계산
            PagingUtil.PagingResult pagingResult = PagingUtil.calculatePagination(
                aladinResponse.getTotalResults(),
                pageable.getPageSize(),
                pageable.getPageNumber()
            );
            
            // 응답 데이터 생성
            SearchedBooksResponse response = SearchedBooksResponse.builder()
                .bookList(aladinResponse.getItem())
                .startPage(pagingResult.getStartPage())
                .endPage(pagingResult.getEndPage())
                .totalPages(pagingResult.getTotalPages())
                .currentPage(pagingResult.getCurrentPage())
                .hasNext(pagingResult.isHasNext())
                .hasPrevious(pagingResult.isHasPrevious())
                .build();
            
            return ResponseEntity.ok(ApiResponse.success(response, "도서 검색이 완료되었습니다."));
            
		} catch(ApiException e) {
			log.error("Failed to search books from Aladin API", e);
            throw new ExternalApiException("알라딘 API 호출 중 오류가 발생했습니다.", e);
		}	
	}
	
/*
 	API
		GET			/reviews/with-style		서평 목록 불러오기 - main 페이지
		GET			/reviews				서평 목록 불러오기 - mybook 페이지
		GET			/api/books/{isbn}		특정 책의 서평 목록 조회 - book detail 페이지
		POST 		/reviews				게시물 작성 
		PUT 		/reviews/{id}			게시물 수정 
		DELETE 		/reviews/{id} 			게시물 삭제
 */	
	
	/* 서평 리스트 api - main 페이지 */
	@ResponseBody
	@GetMapping("/reviews-with-style") 
	public ResponseEntity<Map<String, Object>> getReviewsWithStyle(
			 @RequestParam(required=false) Long playlistId,
			 @RequestParam(required=false) Long emotionId,
			 @PageableDefault(page = 0, size = MAIN_PAGE_SIZE) Pageable pageable,
			 @AuthenticationPrincipal UserPrincipal userDetails) {
		System.out.println("MainController.apiReview()");

		Long authUserId = Optional.ofNullable(userDetails)
				.map(UserPrincipal::getUserId)
				.orElse(null);
		
		Map<String, Object> response;
		if(playlistId != null) {
			response = reviewService.findReviewsByPlaylist(playlistId, authUserId, pageable);
		} else {
			Long targetEmotionId = emotionId != null ? 
					emotionId : (long) Math.floor(TOTAL_EMOTION * Math.random());
			response = reviewService.findReviewsByEmotion(targetEmotionId, authUserId, pageable);
		}
		
		return ResponseEntity.ok(response);
	}	
	
   /* 서평 리스트 api - mybook 페이지 */
   @ResponseBody  
   @GetMapping("/reviews")
   public ResponseEntity<ReviewListResponse> getReviews (
		   @RequestParam(required=false) Long userId,
		   @RequestParam(required=false) String emotionName, 
           @PageableDefault(size = MYBOOK_PAGE_SIZE, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
           @AuthenticationPrincipal UserPrincipal userDetails){
	   
	   System.out.println("MyBookController.getReviews()");
	  
	   ReviewSearchCriteria.ReviewSearchCriteriaBuilder criteriaBuilder = ReviewSearchCriteria.builder()
			   .userId(userId)
			   .authUserId(userDetails.getUserId());
	   
	   if(StringUtils.hasText(emotionName) && !"undefined".equals(emotionName) && !"null".equals(emotionName)) {
		   criteriaBuilder.searchType(SearchType.EMOTION).emotionName(emotionName);
	   } else if(isPopularitySort(pageable)) {
		   criteriaBuilder.searchType(SearchType.POPULARITY);
	   } else {
		   criteriaBuilder.searchType(SearchType.DATE);
	   }
	   
	   Page<ReviewEntity> pageResult = reviewService.findReviews(
		   criteriaBuilder.build(), 
		   pageable
	   );

	   PagingResult pagination = PagingUtil.calculatePagination(
		   (int) pageResult.getTotalElements(), 
		   pageable.getPageSize(), 
		   pageable.getPageNumber() + 1
	   );
	   
       return ResponseEntity.ok(ReviewListResponse.of(
               pageResult.toList(),
               pageResult.getPageable().getSort().toString(),
               null,
               pagination.getStartPage(),
               pagination.getEndPage()
       ));
   }	

   private boolean isPopularitySort(Pageable pageable) {
       return pageable.getSort().stream()
               .findFirst()
               .map(order -> "likeCount".equals(order.getProperty()))
               .orElse(false);
   }
   
	/* 특정 책의 서평 목록 조회  - book detail 페이지*/
	@RequestMapping("/api/books/{isbn}")
	public ResponseEntity<Map<String, Object>> getBookReviews(
			@PathVariable(value="isbn") Long isbn,
			@PageableDefault(page = 0, size = BOOK_PAGE_SIZE) Pageable pageable,
			@AuthenticationPrincipal UserPrincipal userDetails) {
	
		Map<String, Object> response = bookDetailService.bookDetail(isbn, pageable, userDetails.getUserId());
		return ResponseEntity.ok(response);
	}
	

    /**
     * 서평 등록 API
     */
	@ResponseBody
	@PostMapping("/reviews")
	public ResponseEntity<Object> creatReview(
			@Valid @RequestBody RegisterReviewRequest reviewRequest,
			@AuthenticationPrincipal UserPrincipal userDetails
		) throws JsonProcessingException {
		log.info("Creating review for user: {}", userDetails.getUserId());
		
		Long reviewId = reviewService.createReview(reviewRequest, userDetails.getUserId());

//		return ResponseEntity.ok(reviewId);	
		return ResponseEntity.ok(ApiResponse.success(reviewId, "서평이 성공적으로 등록되었습니다."));
	}
	
	/**
     * 서평 수정 API
     */
	@ResponseBody
	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<Object> updateReview(
			@PathVariable Long reviewId,
			@Valid @RequestBody RegisterReviewRequest request,
			@AuthenticationPrincipal UserPrincipal userDetails) {
		log.info("Updating review: {} for user: {}", reviewId, userDetails.getUserId());

		request.validate(reviewId); // requestDTO의 reviewId와 pathVariable의 reviewId 일치 여부 검증
		
		ReviewEntity updatedReview = reviewService.updateReview(request);

		return ResponseEntity.ok(ApiResponse.success(
			updatedReview.getReviewId(), 
			"서평이 정상적으로 수정되었습니다."
		));
	}
	

    /**
     * 서평 삭제 API
     */
  @ResponseBody 
  @DeleteMapping("/reviews/{reviewId}")
  public ResponseEntity<Object> deleteReview(
		   @PathVariable Long reviewId,
		   @AuthenticationPrincipal UserPrincipal userDetails) {	   
	   
	   log.info("Deleting review: {} for user: {}", reviewId, userDetails.getUserId());
	
	   reviewService.deleteReview(userDetails.getUserId(), reviewId); 
	   
	   return ResponseEntity.ok(ApiResponse.success(null, "서평이 성공적으로 삭제되었습니다."));	   
  }
	
/*
 	좋아요 기능
		POST		/reviews/{reviewId}/likes 
		DELETE		/reviews/{reviewId}/likes
		GET			/reviews/{reviewId}/likes/count		좋아요 개수 조회
		GET			/reviews/{reviewId}/likes/me		현재 사용자의 좋아요 상태 확인
 */
  
//	
//  추가로 고려할 수 있는 개선 사항:
//
//	  좋아요 작업의 비동기 처리
//	  캐싱을 통한 성능 최적화
//	  좋아요 알림 기능 추가
//	  좋아요 내역 조회 API 추가
//	  테스트 코드 작성  

  /**
   * 서평 좋아요 토글 API
   */
  @ResponseBody
  @PostMapping("/reviews/{reviewId}/likes")
  public ResponseEntity<ApiResponse<LikeResponse>> toggleLike(
		   @PathVariable Long reviewId,
		   @RequestBody LikeRequest request,
		   @AuthenticationPrincipal UserPrincipal userDetails) {
	  log.info("Toggling like for review: {} by user: {}", reviewId, userDetails.getUserId());
      
      // PathVariable과 RequestBody의 reviewId 일치 여부 검증
      if (!reviewId.equals(request.getReviewId())) {
          throw new InvalidRequestException("요청 경로의 reviewId와 본문의 reviewId가 일치하지 않습니다.");
      }
      
//	   Integer result = reviewService.toggleReviewLike(userDetails.getUserId(), request.getReviewId());
//	   return ResponseEntity.status(result).build();
      
      LikeResult result = reviewService.toggleReviewLike(userDetails.getUserId(), reviewId);
      
      String message = result.isLiked() ? "좋아요가 추가되었습니다." : "좋아요가 취소되었습니다.";
      
      return ResponseEntity.ok(ApiResponse.success(
          new LikeResponse(result.isLiked(), result.getLikeCount()),
          message
      ));
  }	
  
	/* 좋아요 취소 */
  	/*
	@ResponseBody
	@DeleteMapping("/reviews/{reviewId}/likes")
	public ResponseEntity<Object> toggleLikeReview(
//			@RequestParam("reviewId") Long reviewId,
			@PathVariable Long reviewId,
			HttpSession session,
			@AuthenticationPrincipal UserPrincipal userDetails
		) {
		System.out.println("PlaylistController.toggleLikeReview");

		Integer httpStatus = mybookService.likeOrDislikeReview(userDetails.getUserId(), reviewId);
		return ResponseEntity.status(httpStatus).build();
	}
	*/
}
