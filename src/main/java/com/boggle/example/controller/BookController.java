package com.boggle.example.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.dto.auth.CustomUserDetails;
import com.boggle.example.dto.user.LoginResponse;
import com.boggle.example.service.BookDetailService;
import com.boggle.example.service.TasteService;

@Controller
public class BookController {

	@Autowired
	BookDetailService bookDetailService;
	@Autowired
	TasteService tasteService;
	
	private final int SIZE = 5;
	
/*
	페이지
	GET		/taste					취향 홈 페이지
	GET		/books/{isbn}			책 상세 페이지
	
	GET		/books/{isbn}/likes		책 좋아요
 */

	/* 취향 홈 페이지 */
	@RequestMapping("/taste")
	public String tasteMain(
//			@PathVariable(value = "nickname") String nickname,
			HttpSession session,
			Model model,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("TasteController.tastemain()");
		
//		if (session.getAttribute("authUser") == null) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//			   
//			   return "/WEB-INF/views/user/loginForm.jsp";
//		}		
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");  
		
		model.addAttribute("nickname", userDetails.getNickname());
		
		Map<String, Object> map = tasteService.getTaste(userDetails.getNickname());
		
		model.addAttribute("reviewList", map.get("reviewList"));
		model.addAttribute("userList", map.get("userList"));
		model.addAttribute("bookList", map.get("bookList"));
		model.addAttribute("plList", map.get("plList"));
		
		return "taste/taste-main";
	}	
	
	/* 책 상세 페이지 */
	@RequestMapping("/books/{isbn}")
	public String bookDetail(
			@PathVariable(value="isbn") Long isbn,
			@PageableDefault(page = 0, size = SIZE) Pageable pageable,
			Model model,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
//		if (session.getAttribute("authUser") == null) {
//			   System.out.println("세션만료 혹은 잘못된 접근");
//			   
//			   return "/WEB-INF/views/user/loginForm.jsp";
//		}	
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
		
		Map<String, Object> map = bookDetailService.bookDetail(isbn, pageable, userDetails.getUserId());
		
		model.addAttribute("nickname", userDetails.getNickname());
		model.addAttribute("bookEntity", map.get("bookEntity"));
		model.addAttribute("reviewList", map.get("reviewList"));
		model.addAttribute("startPage", map.get("startPage"));
		model.addAttribute("endPage", map.get("endPage"));
		model.addAttribute("likeBook", map.get("likeBook"));
	
		return "book_detail/book_detail";
	}
	
	/* 책 좋아요 */
	@ResponseBody
	@RequestMapping("/books/{isbn}/likes")
	public Long bookUser(
//			@RequestParam("isbn") Long isbn, 
			@PathVariable Long isbn,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
		
		return bookDetailService.bookUser(userDetails.getUserId(), isbn);
	}
}
