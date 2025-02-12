package com.boggle.example.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeRequest {

	@NotNull(message = "서평 ID는 필수입니다.")
	private Long reviewId;
}
