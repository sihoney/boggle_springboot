package com.boggle.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {

	private Long userId;
	private String nickname;
	private String userProfile;
	private Integer totalReviewCount;
	private String recentReviewContent;
}
