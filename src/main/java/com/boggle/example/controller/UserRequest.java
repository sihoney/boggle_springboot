package com.boggle.example.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
