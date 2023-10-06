package com.boggle.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boggle.example.domain.PlaylistEntity;
import com.boggle.example.domain.ReviewEntity;
import com.boggle.example.service.PlaylistService;
import com.boggle.example.util.PagingUtil;


@Controller
public class PlaylistController {
	
	@Autowired
	PlaylistService plService;
	
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
	
	@RequestMapping("/playlists/{playlistId}")
	public String playlistFolder(
			@PathVariable(value = "playlistId") Long playlistId,
			@RequestParam(value = "userId") Long plUserId,
			Model model,
			HttpSession session) {
		System.out.println("PlaylistController.playlistFolder()");
		
		if (session == null 
				  || session.getAttribute("authUser") == null 
				  || session.getAttribute("authUser").equals("")) {
			   System.out.println("세션만료 혹은 잘못된 접근");
			   
			   return "/WEB-INF/views/user/loginForm.jsp";
		}	
		
		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");  
		if(!Objects.isNull(authUser) && authUser.getUserId() == plUserId) {
			model.addAttribute("result", "sameUser");
		} else {
			model.addAttribute("result", "otherUser");
		}
		
		Page<ReviewEntity> page = plService.getReviewByPlaylist(
				authUser.getUserId(), 
				plUserId, 
				playlistId, 
				PageRequest.of(0, 7, Sort.by(Sort.Order.desc("createdAt")))
				);
		
		//2) ★페이지 카운트 가져오기 
//		(Integer totalEntity, Integer pageSize, Integer currentPage)
//		PagingUtil.pagination(null, null, null);
		
		//3) ★포장---------------------------------
//		Map<String,Object> playlistPage = new HashMap<String,Object>();
//		playlistPage.put("prev", prev);
//		playlistPage.put("startPageBtnNo", startPageBtnNo);
//		playlistPage.put("endPageBtnNo", endPageBtnNo);
//		playlistPage.put("next", next);
//		playlistPage.put("playList", playList);
//		playlistPage.put("playlistCover", playlistCover);
		
		model.addAttribute("reviewList", page.getContent());
		
		return "/WEB-INF/views/playlist/click-playlist.jsp";
	}

}
