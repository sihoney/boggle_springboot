package com.boggle.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.service.PlaylistService;
import com.boggle.example.util.PagingUtil;


@Controller
public class PlaylistController {
	
	@Autowired
	PlaylistService plService;
	
	Integer PAGE_SIZE = 7;
	
	@RequestMapping("/{nickname}/playlist")
	public String playlist(
			@PathVariable(value = "nickname") String nickname, 
		    HttpSession session, 
		    Model model
			) {
		System.out.println("PlaylistController.playlist()");
		
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
		
		Map<String, List<PlaylistEntity>> map = plService.getPlaylists(nickname);
		model.addAttribute("likeList", map.get("likeList"));
		model.addAttribute("ppList", map.get("ppList"));
		model.addAttribute("myList", map.get("myList"));
		
		return "/WEB-INF/views/playlist/like-playlist.jsp";
	}
	
	@RequestMapping("/playlist_folder/{playlistId}")
	public String playlistFolder(
			@PathVariable(value = "playlistId") Long playlistId,
			Model model,
			HttpSession session) {
		System.out.println("PlaylistController.playlistFolder()");
		
		if (session.getAttribute("authUser") == null ) {
			   System.out.println("세션만료 혹은 잘못된 접근");
			   
			   return "/WEB-INF/views/user/loginForm.jsp";
		}	
		
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 
		
		Integer CURRENT_PAGE = 0;
		Sort SORT = Sort.by(Sort.Order.desc("createdAt"));
		
		Map<String, Object> map = plService.getPlaylistFolder(authUser.getUserId(), playlistId, PageRequest.of(CURRENT_PAGE, PAGE_SIZE, SORT));

		model.addAttribute("reviewList", map.get("reviewList"));
		model.addAttribute("playlistCover", map.get("playlistCover"));
		model.addAttribute("startPageBtnNo", map.get("startPage"));
		model.addAttribute("endPageBtnNo", map.get("endPage"));
		model.addAttribute("prev", null);
		model.addAttribute("next", null);
		
		return "/WEB-INF/views/playlist/click-playlist.jsp";
	}

	@ResponseBody
	@GetMapping("/playlists/{playlistId}")
	public ResponseEntity<Map<String, Object>> getPlaylistFolder(
			@PathVariable(value = "playlistId") Long playlistId,
			Pageable pageable,
			HttpSession session 
			) {
		if (session.getAttribute("authUser") == null) {
			   System.out.println("세션만료 혹은 잘못된 접근");
		}	
		
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser"); 
		
		Map<String, Object> map = plService.getPlaylistFolder(authUser.getUserId(), playlistId, pageable);
		
		return ResponseEntity.ok(map);
	}
}
