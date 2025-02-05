package com.boggle.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.dto.AnalyzeResponse;
import com.boggle.example.dto.GenreDTO;
import com.boggle.example.dto.auth.CustomUserDetails;
import com.boggle.example.service.AnalyzeService;

@Controller
public class AnalyzeController {

	@Autowired
	AnalyzeService analyzeService;
	
// 필요한 정보
//	- 유저 정보
//	- 주간, 월간, 연간 (기간)
	@RequestMapping("/{nickname}/analyze")
	public String analyze(
			@PathVariable(value = "nickname", required=true) String nickname,
			@AuthenticationPrincipal CustomUserDetails userDetails,
//			@RequestParam(value="userId", required=false) Long userId,
			@RequestParam(value="period", required=false, defaultValue="week") String period,
			Model model
			) {
		System.out.println("AnalyzeController.analyze()");

//		analyzeService.main(nickname, period);
//		model.addAttribute("analyze", analyzeService.main(nickname, period));
		
		return "analyze/user_analyze";
	}
	
	@ResponseBody
	@RequestMapping("/analyze")
	public AnalyzeResponse getAnalyze(
			@RequestParam(value = "period", required = true) String period,
			@RequestParam(value = "nickname", required = true) String nickname
			) {
		System.out.println("AnalyzeController.getAnalyze()");
		
//		List<GenreDTO> genreDTOs = analyzeService.main(nickname, period);
		
		return analyzeService.main(nickname, period);
	}
}
