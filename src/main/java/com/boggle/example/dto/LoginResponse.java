package com.boggle.example.dto;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class LoginResponse {

	private boolean result;
	private Long userId;
	private String userName;
	private String nickname;
	private String userProfile;
	
	private LoginResponse(boolean result) {
		if(Objects.isNull(result))
			throw new IllegalArgumentException("reuslt is null");
		
		this.result = result;
	}
	
	private LoginResponse(boolean result, Long userId, String userName, String nickname, String userProfile) {
		if(Objects.isNull(result))
			throw new IllegalArgumentException("result is null");
		
		this.result = result;
		this.userId = userId;
		this.userName = userName;
		this.nickname = nickname;
		this.userProfile = userProfile;
	}
	
	
	public static LoginResponse of(boolean result) {
		 return new LoginResponse(result);
	}
	
	public static LoginResponse of(boolean result, Long userId, String userName, String nickname, String userProfile) {
		return new LoginResponse(result, userId, userName, nickname, userProfile);
	}
}
