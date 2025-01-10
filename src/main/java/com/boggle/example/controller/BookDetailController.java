package com.boggle.example.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.service.BookDetailService;

@Controller
public class BookDetailController {

	@Autowired
	BookDetailService bookDetailService;
	
	private final int SIZE = 5;
	
	@RequestMapping("/book/{isbn}")
	public String bookDetail(
			@PathVariable(value="isbn") Long isbn,
			@PageableDefault(page = 0, size = SIZE) Pageable pageable,
			Model model,
			HttpSession session
			) {
		if (session.getAttribute("authUser") == null) {
			   System.out.println("세션만료 혹은 잘못된 접근");
			   
			   return "/WEB-INF/views/user/loginForm.jsp";
		}	
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
		
		Map<String, Object> map = bookDetailService.bookDetail(isbn, pageable, authUser.getUserId());
		
		model.addAttribute("nickname", authUser.getNickname());
		model.addAttribute("bookEntity", map.get("bookEntity"));
		model.addAttribute("reviewList", map.get("reviewList"));
		model.addAttribute("startPage", map.get("startPage"));
		model.addAttribute("endPage", map.get("endPage"));
		model.addAttribute("likeBook", map.get("likeBook"));
	
		return "/WEB-INF/views/book_detail/book_detail.jsp";
	}
	
	@RequestMapping("/api/book/{isbn}")
	public ResponseEntity<Map<String, Object>> bookDetailApi(
			@PathVariable(value="isbn") Long isbn,
			@PageableDefault(page = 0, size = SIZE) Pageable pageable,
			HttpSession session
			) {
	
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
		
		Map<String, Object> map = bookDetailService.bookDetail(isbn, pageable, authUser.getUserId());
		
		return ResponseEntity.ok(map);
	}
	
	@ResponseBody
	@RequestMapping("/bookUser")
	public Long bookUser(
			@RequestParam("isbn") Long isbn, 
			HttpSession session
			) {
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
		
		return bookDetailService.bookUser(authUser.getUserId(), isbn);
	}
}
