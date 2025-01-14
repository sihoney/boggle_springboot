package com.boggle.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boggle.example.dto.UserRequest;
import com.boggle.example.service.UserService;


@Controller
//@RequestMapping("/user")
public class UserController {
	
/*
 * 1. 로그인
 * 2. 회원가입
 * 3. 유저정보 수정	
 */
	
	@Autowired
	private UserService userService;
//	@Autowired
//	private CustomUserDetailsService userDetailsService;
//	@Autowired
//	private ResourceLoader resourceLoader;
	
	/* 로그인 페이지 - spring security */ 
	@RequestMapping("/user/loginForm")
	public String loginForm() {
		System.out.println("UserController.loginForm()");
		
		return "user/loginForm";
	}
	
	/* 회원가입 페이지 */
	@RequestMapping("/user/joinForm")
	public String joinForm() {
		System.out.println("UserController.joinForm()");
		
		return "user/joinForm";
	}
	
	/* 회원가입 */
	@PostMapping(value="/join") 	
	public String join(
			@ModelAttribute UserRequest userRequest
			) {
		System.out.println("UserController.join()");

		userService.join(userRequest);

		return "user/loginForm";
	}
	
	/* 회원가입-2 */
//    @PostMapping("/join")
//    public String registerUser(@ModelAttribute SignupForm form) {
//    	
//        userDetailsService.registerNewUser(form);
//        
//        return "redirect://WEB-INF/views/user/loginForm.jsp";  // 회원가입 후 로그인 페이지로 이동
//    }
	
	/* 회원가입 페이지 - 닉네임 중복체크 */
//	@RequestMapping(value="users/join/checkNickname")
//    @ResponseBody
//    public ResponseEntity<Integer> nicknameCheck(
//    		@RequestParam("nickname") String nickname
//    		){
//    	System.out.println("사용하고싶은 닉네임 : "+nickname);
//        
//    	boolean result = userService.checkNickname(nickname);
//        
//        return new ResponseEntity(result, HttpStatus.OK);
//    }	
	
	/* 로그아웃 */
//	@RequestMapping("/user/logout")
//	public String logout(HttpSession session) {
//		System.out.println("UserController.logout()");
//		
//		// 세션 정보 삭제
//		session.removeAttribute("authUser");
//		
//		return "/WEB-INF/views/user/loginForm.jsp";
//	}
	
	/* 수정 페이지 */
//	@RequestMapping("/user/modify")
//	public String modifyUserPage(
//			HttpSession session,
//			Model model,
//			@AuthenticationPrincipal CustomUserDetails userDetails
//			) {
//		System.out.println("UserController.modifyUser()");
//		
//	    model.addAttribute(userDetails);
//	    
//		return "/WEB-INF/views/user/user_modify.jsp";
//	}
//	
//	private void updateAuthentication(CustomUserDetails updatedUserDetails, Authentication authentication) {
//	    Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
//	        updatedUserDetails,
//	        authentication.getCredentials(),
//	        updatedUserDetails.getAuthorities()
//	    );
//
//	    SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
//	}
//
//	
//	/* 수정 */
//	@ResponseBody
//	@RequestMapping("/modifyUser")
//	public String modifyUser(
////			@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult,
//			@RequestParam(name = "image-file", required = false) MultipartFile file,
//			@RequestParam(name = "nickname", required = false) String nickname,
//			@RequestParam(name = "password", required = false) String password, 
//			HttpSession session,
//			@AuthenticationPrincipal CustomUserDetails userDetails
//			,RedirectAttributes redirectAttributes // RedirectAttributes를 매개변수로 추가
//			) {
//		
////	    if (bindingResult.hasErrors()) {
////	    	return "failure";
//////	        return "userForm"; // 오류가 있을 경우 다시 폼으로 이동
////	    }
//
//		try {
//			// 사용자 정보 업데이트
//			UserEntity updatedUserEntity = userService.modifyUser(userDetails.getUserId(), file, nickname, password);
//			
//	        if (updatedUserEntity == null) {
//	            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user information.");
//	            return "redirect:/failure";
//	        }		
//	        
//	        // 새 사용자 정보로 UserDetails 갱신
//	        CustomUserDetails updatedUserDetails = new CustomUserDetails(
//	                updatedUserEntity.getEmail(), 
//	                updatedUserEntity.getPassword(), 
//	                updatedUserEntity.getUserId(), 
//	                updatedUserEntity.getUserName(),
//	                updatedUserEntity.getNickname(),
//	                updatedUserEntity.getUserProfile(), 
//	                userDetails.getAuthorities()
//	        );	
//	        
////			SecurityContext context = SecurityContextHolder.getContext();
////			Authentication authentication = context.getAuthentication();
//	        
////			updateAuthentication(updatedUserDetails, authentication);		
//			Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
//			    updatedUserDetails, // 새로운 UserDetails
//			    null, // 사용자의 인증 확인을 위해 비밀번호가 전달되는 자리
//			    updatedUserDetails.getAuthorities() // 새로운 권한 정보
//			);
//			SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
//	        
//			return "success";
////	        redirectAttributes.addFlashAttribute("successMessage", "User information updated successfully.");
////	        return "redirect:/WEB-INF/views/success";
//			
//		} catch (Exception e) {
//			return "failure";
////	        // 예외 발생 시 로그 및 리다이렉트 처리
////	        redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while updating user information.");
////	        return "redirect:/WEB-INF/views/failure";			
//		}
//	
//		/*
//	    // userService.modifyUser 메서드의 결과에 따라 리다이렉트할 URL 선택
//	    if (!Objects.isNull(updatedUserEntity)) {
//	        // 성공한 경우
//	    	return "success";
//	    } else {
//	        // 실패한 경우
//	    	return "failure";
//	    }
//	    */	
//	}
//	
//	/* 수정 페이지 - 닉네임 중복 체크 */
//	@ResponseBody
//	@PostMapping("/check-duplicate")
//	public ResponseEntity<Boolean> checkDuplicate(
//			@RequestBody UserRequest nicknameToCheck
//		) {
//		
//		return ResponseEntity.ok(userService.checkNickname(nicknameToCheck.getNickname()));
//	}
//	
//	/* 수정 페이지 - 비밀번호 체크 */
//	@ResponseBody
//	@PostMapping("/check-password")
//	public ResponseEntity<Boolean> checkPassword(
//			@RequestBody UserRequest passwordToCheck,
//			HttpSession session,
//			@AuthenticationPrincipal CustomUserDetails userDetails
//		) {
////		System.out.println(passwordToCheck);
//		
////		LoginResponse authUser = (LoginResponse) session.getAttribute("authUser");
////	    if (session == null 
////			  || session.getAttribute("authUser") == null 
////			  || session.getAttribute("authUser").equals("")) {
////		   System.out.println("세션만료 혹은 잘못된 접근");
////	    }
//	    
//	    return ResponseEntity.ok(userService.checkPassword(userDetails.getNickname(), passwordToCheck.getPassword()));
//	}	
}
