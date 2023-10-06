package com.boggle.example.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.domain.ReviewUserEntity;
import com.boggle.example.service.MyBookService;
import com.boggle.example.util.EmotionEnum;
import com.boggle.example.util.PagingUtil;

@Controller
public class MyBookController {

	@Autowired
	MyBookService mybookService;
	
//	mybook 페이지
	@RequestMapping("/{nickname}/mybook")
	public String mybook (
			@PathVariable(value = "nickname", required=true) String nickname,
			HttpSession session,
			Model model
			) {
		System.out.print("MyBookService.mybook(), ");
		System.out.println("nickname: " + nickname);
		
		  if (session == null 
				  || session.getAttribute("authUser") == null 
				  || session.getAttribute("authUser").equals("")
				  || nickname.equals(null)) {
			   System.out.println("세션만료 혹은 잘못된 접근");
			   
			   return "/WEB-INF/views/user/loginForm.jsp";
		   }
		
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");  
		if(!Objects.isNull(authUser) && nickname.equals(authUser.getNickname())) {
			model.addAttribute("result", "sameUser");
		} else {
			model.addAttribute("result", "otherUser");
		}	
		
		model.addAttribute("emotionList", EmotionEnum.getList());
		model.addAttribute("userProfile", mybookService.getUserProfile(nickname));
		
		int page = 0;
		int size = 8;
		Sort sort = Sort.by(Sort.Order.desc("createdAt"));
		Page<ReviewEntity> pageObj = mybookService.getReviewsByCreatedAt(nickname, null, PageRequest.of(page, size, sort), authUser.getUserId());
		model.addAttribute("reviewList", pageObj.getContent());
		
		model.addAttribute("sort", pageObj.getPageable().getSort().toString());
		
		Map<String, Integer> map = PagingUtil.pagination(
				(int) pageObj.getTotalElements(), 
				size, 
				page + 1);
		ArrayList<Integer> pagination = new ArrayList<>();
		Integer startPage = map.get("startPage");
		Integer endPage = map.get("endPage");
		while(true) {
			if(startPage > endPage)
				break;
			pagination.add(startPage);
			
			startPage++;
		}
		model.addAttribute("pagination", pagination);
		
		return "/WEB-INF/views/mybook/mybook_review.jsp";
	}

//	서평 리스트 api
   @ResponseBody  
   @RequestMapping("/{userId}/reviews")   
   public ResponseEntity<ReviewListResponse> getReviews (
		   @PathVariable(value="userId", required=true) String userId,
		   @RequestParam(value = "emotionName", required=false) String emotionName,
		   HttpSession session, 
           Pageable pageable
           ){
	   System.out.println("MyBookController.getReviews()");

	   LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
	   
	  if (Objects.isNull(authUser) || Objects.isNull(userId)) {
		   System.out.println("세션 만료");
		   return ResponseEntity.badRequest().build();
	   }
	  
	  pageable = PageRequest.of(
			  pageable.getPageNumber() - 1, 
			  pageable.getPageSize(),
			  pageable.getSort());

	  Page<ReviewEntity> pageObj;
	  
	  String property = pageable.getSort().get().findFirst().orElse(null).getProperty();

	  if(property.equals("likeCount")) {
		  System.out.println(">> 인기순");
		  
		  pageObj = mybookService.getReviewsOrderByLikeCount(
				  Long.parseLong(userId), 
				  authUser.getUserId(), 
				  pageable);
	  }
	  else if("undefined".equals(emotionName) || "null".equals(emotionName) || Objects.isNull(emotionName) ) {
		  System.out.println(">> 최신순");
		  
		  pageObj = mybookService.getReviewsByCreatedAt(
				  null, 
				  Long.parseLong(userId), 
				  pageable, 
				  authUser.getUserId());
	  }
	  else {
		  System.out.println(">> 감정별");
		  
		  pageObj = mybookService.getReviewByEmotion(
				  Long.parseLong(userId), 
				  emotionName, 
				  pageable, 
				  authUser.getUserId());
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
   public ResponseEntity<ReviewUserEntity> like(
		   HttpSession session,
		   @RequestBody LikeRequest likeRequest) {
	   System.out.println("MyBookController.like()");

	   LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");

	   ReviewUserEntity reviewUserEntityResult = mybookService.likeOrDislikeReview(authUser.getUserId(), likeRequest.getReviewId());

	   return ResponseEntity.ok(reviewUserEntityResult);
   }
   
//   서평 삭제
   @ResponseBody 
   @DeleteMapping("/review/{reviewId}")
   public ResponseEntity deleteReview(
		   @PathVariable Long reviewId,
//		   @RequestParam(name = "reviewId", required=true) Long reviewId,
		   HttpSession session
		   ) {	   
	   try {
		   System.out.println("MyBookController.deleteReview()");
		   
		   LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
		   if(Objects.isNull(authUser)) {
			   return new ResponseEntity<>(
					   "세션 정보 만료", 
					   HttpStatus.BAD_GATEWAY
					   );
		   }

		   mybookService.deleteReview(authUser.getUserId(), reviewId); 
		   
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
