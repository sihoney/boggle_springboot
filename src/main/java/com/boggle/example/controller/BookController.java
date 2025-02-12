package com.boggle.example.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.dto.auth.CustomUserDetails;
import com.boggle.example.entity.UserPrincipal;
import com.boggle.example.service.BookService;
import com.boggle.example.service.TasteService;

@Controller
public class BookController {

	@Autowired
	BookService bookDetailService;
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
			Model model,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("TasteController.tastemain()"); 
		
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
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		
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
			@PathVariable Long isbn,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		return bookDetailService.toggleBookLike(userDetails.getUserId(), isbn);
	}
}
