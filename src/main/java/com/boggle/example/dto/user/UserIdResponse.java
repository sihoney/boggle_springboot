package com.boggle.example.dto.user;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;

@Getter
public class UserIdResponse {

	@JsonProperty("id")
//	@JsonSerialize(using = ToStringSerializer.class) // 사용하는 이유?
	private Long userId;
	
	private UserIdResponse(Long userId) {
		if(Objects.isNull(userId))
			throw new IllegalArgumentException("userId is null");
		
		this.userId = userId;
	}
	
	public static UserIdResponse from(Long userId) {
		return new UserIdResponse(userId);
	}
}
