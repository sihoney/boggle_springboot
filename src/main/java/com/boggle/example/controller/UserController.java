package com.boggle.example.controller;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boggle.example.domain.UserEntity;
import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.service.UserService;


@Controller
//@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
//	@Autowired
//	private ResourceLoader resourceLoader;
	
	/* 회원가입폼 */
	@RequestMapping("/user/joinForm")
	public String joinForm() {
		System.out.println("UserController.joinForm()");
		
		return "/WEB-INF/views/user/joinForm.jsp";
	} 
	
	/* 가입 */
	@PostMapping(value="/join") 	
	public String join(
			@ModelAttribute UserRequest userRequest) {
		System.out.println("UserController.join()");

		userService.join(userRequest);

		return "/WEB-INF/views/user/loginForm.jsp";
	}

	/* 닉네임 중복 체크 */
	@RequestMapping(value="users/join/checkNickname")
    @ResponseBody
    public ResponseEntity<Integer> nicknameCheck(
    		@RequestParam("nickname") String nickname
    		){
    	System.out.println("사용하고싶은 닉네임 : "+nickname);
        
    	boolean result = userService.checkNickname(nickname);
        
        return new ResponseEntity(result, HttpStatus.OK);
    }
	
	/* 회원정보 수정 페이지 */
	@RequestMapping("/user/modify")
	public String modifyUserPage(
			HttpSession session,
			Model model,
			@AuthenticationPrincipal CustomUserDetails userDetails
			) {
		System.out.println("UserController.modifyUser()");
		
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//	    if (session == null 
//			  || session.getAttribute("authUser") == null 
//			  || session.getAttribute("authUser").equals("")) {
//		   System.out.println("세션만료 혹은 잘못된 접근");
//		   
//		   return "/WEB-INF/views/user/loginForm.jsp";
//	     }
		
	    model.addAttribute(userDetails);
	    
		return "/WEB-INF/views/user/user_modify.jsp";
	}
	
	/* 개인정보 수정, 닉네임 중복 체크 */
	@ResponseBody
	@PostMapping("/check-duplicate")
	public ResponseEntity<Boolean> checkDuplicate(
			@RequestBody UserRequest nicknameToCheck
		) {
		
		return ResponseEntity.ok(userService.checkNickname(nicknameToCheck.getNickname()));
	}
	
	/* 개인정보 수정, 비밀번호 체크 */
	@ResponseBody
	@PostMapping("/check-password")
	public ResponseEntity<Boolean> checkPassword(
			@RequestBody UserRequest passwordToCheck,
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
		) {
//		System.out.println(passwordToCheck);
		
//		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
//	    if (session == null 
//			  || session.getAttribute("authUser") == null 
//			  || session.getAttribute("authUser").equals("")) {
//		   System.out.println("세션만료 혹은 잘못된 접근");
//	    }
	    
	    return ResponseEntity.ok(userService.checkPassword(userDetails.getNickname(), passwordToCheck.getPassword()));
	}
	
	private void updateAuthentication(CustomUserDetails updatedUserDetails, Authentication authentication) {
	    Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
	        updatedUserDetails,
	        authentication.getCredentials(),
	        updatedUserDetails.getAuthorities()
	    );

	    SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
	}
	
	/* 유저 정보 수정 */
	@ResponseBody
	@RequestMapping("/modifyUser")
	public String modifyUser(
			@RequestParam(name = "image-file", required = false) MultipartFile file,
			@RequestParam(name = "nickname", required = false) String nickname,
			@RequestParam(name = "password", required = false) String password, 
			HttpSession session,
			@AuthenticationPrincipal CustomUserDetails userDetails
			,RedirectAttributes redirectAttributes // RedirectAttributes를 매개변수로 추가
			) {

		System.out.println(userDetails);
		
		UserEntity updatedUserEntity = userService.modifyUser(userDetails.getUserId(), file, nickname, password);
 
		CustomUserDetails updatedUserDetails = new CustomUserDetails(
				updatedUserEntity.getEmail(), 
				updatedUserEntity.getPassword(), 
				updatedUserEntity.getUserId(), 
				updatedUserEntity.getNickname(),
				updatedUserEntity.getUserProfile(), 
				userDetails.getAuthorities());// 새로운 사용자 정보 가져오기
		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		
		updateAuthentication(updatedUserDetails, authentication);
//		Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
//		    updatedUserDetails, // 새로운 UserDetails
//		    authentication.getCredentials(), // 기존의 credentials 유지
//		    updatedUserDetails.getAuthorities() // 새로운 권한 정보
//		);
//		SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
		
	    
	    // userService.modifyUser 메서드의 결과에 따라 리다이렉트할 URL 선택
	    if (!Objects.isNull(updatedUserEntity)) {
	        // 성공한 경우
	    	return "success";
	    } else {
	        // 실패한 경우
	    	return "failure";
	    }
//	    if (!Objects.isNull(updatedUserEntity)) {
//	        // 성공한 경우
//	        redirectAttributes.addFlashAttribute("successMessage", "User information updated successfully.");
//	        return "redirect:/success";
//	    } else {
//	        // 실패한 경우
//	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user information.");
//	        return "redirect:/failure";
//	    }		
	}
	
	/* 로그인폼 */ 
	@RequestMapping("/user/loginForm")
	public String loginForm() {
		System.out.println("UserController.loginForm()");
		
		return "/WEB-INF/views/user/loginForm.jsp";
	}	
	
//	/* 로그인 */
//	@RequestMapping("/login")
//	public String login(
//			HttpSession session,
//			@ModelAttribute UserRequest userRequest,
//			Model model
//			) {
//		System.out.println("UserController.login()");
//		
////		데이터베이스에 클라이언트가 제공한 유저 정보와 일치하는 데이터가 존재하는지 확인
//		LoginResponse loginResponse = userService.login(userRequest.getEmail(), userRequest.getPassword());
//		
//		if(loginResponse.isResult() == false) {
//			System.out.println("로그인 실패");
//			
//			model.addAttribute("result", loginResponse);
//			return "/WEB-INF/views/user/loginForm.jsp";
//		} else {
//			System.out.println("로그인 성공");
//			
//			session.setAttribute("authUser", loginResponse);
//			
//			String forwardUrl = "forward:/" + loginResponse.getNickname() + "/mybook";
//			return forwardUrl;
//		}
//	}
	
	/* 로그아웃 */
	@RequestMapping("/user/logout")
	public String logout(HttpSession session) {
		System.out.println("UserController.logout()");
		
		// 세션 정보 삭제
		session.removeAttribute("authUser");
		
		return "/WEB-INF/views/user/loginForm.jsp";
	
	}
}
