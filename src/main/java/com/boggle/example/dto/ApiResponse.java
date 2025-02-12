package com.boggle.example.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(force = true)  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 추가
@Getter
@Builder
public class ApiResponse<T> {

	private final boolean success;
	private final String message;
	private T data;
	private Integer totalResults;
	private final String errorCode;
	private List<BookDTO> item;
	
//	private String version;
//	private String title;
//	private String link;
//	private String pubDate;
//	private String imageUrl;

	public static Object success(Long reviewId, String string) {
		// TODO Auto-generated method stub
		return null;
	}
	

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}
