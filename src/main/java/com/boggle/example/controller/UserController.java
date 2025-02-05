package com.boggle.example.controller;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boggle.example.dto.user.UserForm;
import com.boggle.example.dto.user.UserRequest;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.entity.UserPrincipal;
import com.boggle.example.service.UserService;


@Controller
//@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
//	@Autowired
//	private CustomUserDetailsService userDetailsService;
//	@Autowired
//	private ResourceLoader resourceLoader;	
	
/*
	페이지
		GET		/users/login			로그인 페이지
		GET		/users/register			회원가입 페이지
		GET		/users/edit				수정 페이지
		
		POST	/users					등록
		PUT		/users					수정
		
		GET		users/nickname/exists	닉네임 중복 확인 
		GET		users/password/exists	비밀번호 중복 확인
 */
	
/*
	페이지
		GET		/users/login			로그인 페이지
		GET		/users/register			회원가입 페이지
		GET		/users/edit				수정 페이지	
 */
	
	/* 로그인 페이지 - spring security */ 
	@GetMapping("/users/login")
	public String loginForm() {
		System.out.println("UserController.loginForm()");
		
		return "user/loginForm";
	}
	
	/* 회원가입 페이지 */
	@GetMapping("/users/register")
	public String joinForm() {
		System.out.println("UserController.joinForm()");
		
		return "user/joinForm";
	}
	
	/* 수정 페이지 */
	@GetMapping("/users/edit")
	public String modifyUserPage(
			HttpSession session,
//			Model model,
			@AuthenticationPrincipal UserPrincipal userDetails
			) {
		System.out.println("UserController.modifyUserPage()");
		
//	    model.addAttribute(userDetails);
	    
		return "user/user_modify";
	}
	
/*
	POST	/users					등록
	PUT		/users					수정
 */
	
	/* 등록 */
	@PostMapping(value="/users") 	
	public String join(
			@RequestBody UserRequest userRequest
			) {
		System.out.println("UserController.join()");
		
		userService.join(userRequest);

		return "user/loginForm";
	}
	
	/* 수정 */
	@ResponseBody
	@PutMapping("/users")
	public ResponseEntity<String> modifyUser(
//			@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult,
			@ModelAttribute UserForm userForm,
			@AuthenticationPrincipal UserPrincipal userDetails,
			RedirectAttributes redirectAttributes, // RedirectAttributes를 매개변수로 추가
			HttpServletRequest request
			) {
		System.out.println("UserController.modifyUser()");
		System.out.println(userForm);
		
//	    if (bindingResult.hasErrors()) {
//	    	return "failure";
////	        return "userForm"; // 오류가 있을 경우 다시 폼으로 이동
//	    }		
		
		try {
			// 1. 사용자 정보 업데이트
			UserEntity updatedUserEntity = userService.modifyUser(
				userDetails.getUserId(), 
				userForm					
			);		
	        
	        // 2. authentication principal 갱신
	        userService.updateUserInfo(
        		UserPrincipal.create(updatedUserEntity), 
        		request
        	);        
	        
			return ResponseEntity.ok().build();
		} catch (EntityNotFoundException e) {
	        // 4. 사용자를 찾을 수 없는 경우
	        return ResponseEntity.notFound().build();
	    } catch (ValidationException e) {
	        // 5. 유효성 검증 실패 
	        return ResponseEntity.badRequest().build();
	    } catch (Exception e) {
	        // 6. 기타 서버 에러
	        return ResponseEntity.internalServerError().build();
	    }	
	}
	
/*
	GET		users/nickname/exists	닉네임 중복 확인 
	GET		users/password/exists	비밀번호 중복 확인
 */
	
	/* 닉네임 중복체크 - 회원가입 페이지 */
	@GetMapping(value="users/nickname/exists")
    @ResponseBody
    public ResponseEntity<Integer> nicknameCheck(
    		@RequestParam("nickname") String nickname,
			@AuthenticationPrincipal UserPrincipal userPrincipal
    		){
    	System.out.println("닉네임 중복 확인 : "+nickname);
        
    	boolean result = userService.checkNickname(nickname, userPrincipal.getNickname());
        
        return new ResponseEntity(result, HttpStatus.OK);
    }
	
	/* 비밀번호 체크 */
	@ResponseBody
	@GetMapping("/users/password/exists")
	public ResponseEntity<Boolean> checkPassword(
			@RequestBody UserRequest passwordToCheck,
			HttpSession session,
			@AuthenticationPrincipal UserPrincipal userDetails
		) {
//		System.out.println(passwordToCheck);
	    
	    return ResponseEntity.ok(userService.checkPassword(
	    		userDetails.getNickname(), 
	    		passwordToCheck.getPassword()
	    		));
	}
}
