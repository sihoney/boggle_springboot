package com.boggle.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.boggle.example.adapter.AladinApiAdapter;

@Controller
public class TestController {

	@Autowired
	AladinApiAdapter apiAdapter;
	
	@GetMapping(path = "/test_view") 
	public String testView() {
		System.out.println(">>테스트 중입니다... restart test");
		
//		String response = apiAdapter.getBookInfo("파친코");
		
//		System.out.println(response);
//		return ResponseEntity.ok(response);
		return "user/test";
	}
	
//	스프링은 ModelAndView 뿐만 아니라 String이나 modelMap, 또는 Map과 같은 타입을 이용해서 뷰 이름과 모델 정보를 설정할 수 있도록 하고 있다.
	@GetMapping(path = "/test_jsp")
	public String testJsp(@RequestParam(value="id") Integer userId,
			Model model) {
		System.out.println(">> userId: " + userId);
		
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("user/test");
//		Map<String, Object> model = mav.getModel();
		model.addAttribute("message", userId);
		
		return "user/test";
//		return "review_write/review_write";
	}
}
