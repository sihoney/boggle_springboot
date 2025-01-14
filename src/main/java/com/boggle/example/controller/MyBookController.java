package com.boggle.example.controller;

import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.dto.LikeRequest;
import com.boggle.example.dto.ReviewListResponse;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.service.MyBookService;
import com.boggle.example.util.EmotionEnum;
import com.boggle.example.util.PagingUtil;

@Controller
public class MyBookController {

	@Autowired
	MyBookService mybookService;
	
//	mybook 페이지
//	@Secured("ROLE_ADMIN")
	@RequestMapping("/{nickname}/mybook")
	public String mybook (
			@PathVariable(value = "nickname", required=true) String nickname,
			HttpSession session,
			Model model,
			@PageableDefault(size = 8, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, //, sort = "createdAt,desc"
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.print("MyBookService.mybook(), ");
		System.out.println("nickname: " + nickname);
		
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

//	서평 리스트 api
   @ResponseBody  
   @RequestMapping("/api/reviews")   
   public ResponseEntity<ReviewListResponse> getReviews (
//		   @PathVariable(value="userId", required=true) String userId,
		   @RequestParam(value = "userId", required=false) Long userId,
		   @RequestParam(value = "emotionName", required=false) String emotionName,
		   HttpSession session, 
           @PageableDefault(size = 8, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
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
   
//   서평 좋아요
   @ResponseBody
   @RequestMapping("/reviewUser")
   public ResponseEntity<String> like(
		   HttpSession session,
		   @RequestBody LikeRequest likeRequest,
		   @AuthenticationPrincipal CustomUserDetails userDetails
		   ) {
	   System.out.println("MyBookController.like()");

//	   LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
	   Integer result = mybookService.likeOrDislikeReview(userDetails.getUserId(), likeRequest.getReviewId());
	   return ResponseEntity.status(result).build();
   }
   
//   서평 삭제
   @ResponseBody 
   @DeleteMapping("/review/{reviewId}")
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
}
