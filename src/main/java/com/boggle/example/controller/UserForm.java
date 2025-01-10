package com.boggle.example.controller;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class UserForm {
	
    @NotEmpty(message = "닉네임은 필수 항목입니다.")
    @Size(min = 3, max = 15, message = "닉네임은 3자 이상 15자 이하이어야 합니다.")
    private String nickname;

//    @NotEmpty(message = "이메일은 필수 항목입니다.")
//    private String email;

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
    
    
    private MultipartFile file;

    // getter, setter 생략
}
