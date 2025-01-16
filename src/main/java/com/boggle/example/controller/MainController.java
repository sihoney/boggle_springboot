package com.boggle.example.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.dto.PlaylistDTO;
import com.boggle.example.service.MainService;
import com.boggle.example.service.PlaylistService;

@Controller
public class MainController {

	@Autowired
	MainService mainService;
	@Autowired
	PlaylistService playlistService;
	
	Integer TOTAL_EMOTION = 8;
	final int SIZE = 5;
	
	/*
	// 플리 재생 (특정 플리, 특정 감정 플리, 랜덤)
	@ResponseBody
	@GetMapping("/api/playlist/{playlistId}") 
	public Map<String, Object> getReviewList(
			 @PathVariable(value="playlistId", required=false) Long playlistId,
			 @PageableDefault(page = 0, size = 5) Pageable pageable,
			 HttpSession session
			 ) {
		System.out.println("MainController.getReviewList()");
		
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");

		return null;
	}
	
	// 서평작성 페이지에서 사용!(review_write)
	@ResponseBody
	@RequestMapping("/emotion")
	public List<Map<String, Object>> getEmotion() {
		System.out.println("MainController > getEmotion()");
		
		return mainService.getEmotion();
	}
	*/
}
