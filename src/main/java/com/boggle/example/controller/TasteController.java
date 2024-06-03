package com.boggle.example.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.service.TasteService;

@Controller
public class TasteController {

	@Autowired
	TasteService tasteService;
	
	@RequestMapping("/{nickname}/taste")
	public String tasteMain(
			@PathVariable(value = "nickname") String nickname,
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
		
		model.addAttribute("nickname", nickname);
		
		Map<String, Object> map = tasteService.getTaste(nickname);
		
		model.addAttribute("reviewList", map.get("reviewList"));
		model.addAttribute("userList", map.get("userList"));
		model.addAttribute("bookList", map.get("bookList"));
		model.addAttribute("plList", map.get("plList"));
		
		return "/WEB-INF/views/taste/taste-main.jsp";
	}
}
