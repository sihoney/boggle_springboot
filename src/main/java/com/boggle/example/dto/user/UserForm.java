package com.boggle.example.dto.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
public class UserForm {
	
    @NotEmpty(message = "닉네임은 필수 항목입니다.")
    @Size(min = 3, max = 15, message = "닉네임은 3자 이상 15자 이하이어야 합니다.")
    private String nickname;

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
    
    private MultipartFile file;

    // getter, setter 생략
}
