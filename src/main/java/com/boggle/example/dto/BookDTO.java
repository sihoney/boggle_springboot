package com.boggle.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(force = true)  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 추가
@Getter
@Builder
public class BookDTO {
	@JsonProperty("isbn13")
	private final Long isbn;
	
	@JsonProperty("title")
	private final String bookName;
	
	private final String author;
	
	@JsonProperty("categoryId")
	private final Long genreId;
	
	@JsonProperty("link")
	private final String bookUrl;
	
	@JsonProperty("cover")
	private final String coverUrl;
}
