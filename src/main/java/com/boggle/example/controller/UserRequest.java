package com.boggle.example.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {

	private String userName;
	private String nickname;
	private String email;
	private String password;
	private String userProfile;
	
	private UserRequest (String nickname, String password, String userProfile) {
		this.nickname = nickname;
		this.password = password;
		this.userProfile = userProfile;
	}
	
	public static UserRequest of(String nickname, String password, String userProfile) {
		return new UserRequest(nickname, password, userProfile);
	}
}
