package com.boggle.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boggle.example.service.ViewService;

@Controller
public class ViewController {

	@Autowired
	ViewService viewService;
	
	@RequestMapping("/reviews/{reviewId}")
	public String getViewOfReview(
			@PathVariable(value = "reviewId") Long reviewId,
			Model model
			) {
		System.out.println("ViewController.viewer()");
		
		model.addAttribute("review", viewService.getReview(reviewId));
		
		return "/WEB-INF/views/viewer/viewer.jsp";
	}
}
