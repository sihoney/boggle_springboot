package com.boggle.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boggle.example.dto.auth.CustomUserDetails;

@Controller
public class ResultController {

	@RequestMapping("/success")
	public String success(
			HttpSession session,
			Model model,
			@AuthenticationPrincipal CustomUserDetails userDetails
		) {
		System.out.println("ResultController.success()");
		
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//		model.addAttribute("authUser", authUser);
		
		return "include/success";
	}
	
	@RequestMapping("/failure")
	public String failure(
			HttpSession session,
			Model model,
			@AuthenticationPrincipal CustomUserDetails userDetails
		) {
		System.out.println("ResultController.failure()");
		
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//		model.addAttribute("authUser", authUser);
		
		return "include/failure";
	}
}
