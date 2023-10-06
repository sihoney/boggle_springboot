package com.boggle.example.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RegisterReviewRequest {

	@JsonProperty("cover")
	private String coverUrl;
	
	private String author;
	
	@JsonProperty("title")
	private String bookName;
	
	private Long isbn;
	
	@JsonProperty("url")
	private String bookUrl;
	
	@JsonProperty("category")
	private String genreName;
	
	@JsonProperty("categoryid")
	private Long genreId;
	
	private Long emotionId;
	
	private String fontName;
	
	private Long fontId;
	
	@JsonProperty("reviewContent")
	private String content;
	
//	private RegisterReviewRequest (String coverUrl, String author, String bookName, Long isbn, String bookUrl, String genreName, Long genreId, 
//			Long emotionId, String fontName, String content) {
//
//		this.coverUrl = coverUrl;
//		this.author = author;
//		this.bookName = bookName;
//		this.isbn = isbn;
//		this.bookUrl = bookUrl;
//		this.genreName = genreName;
//		this.genreId = genreId;
//		this.emotionId = emotionId;
//		this.fontName = fontName;
//		this.content = content;
//	}
//	
//	public static RegisterReviewRequest of(String coverUrl, String author, String bookName, Long isbn, String bookUrl, String genreName, Long genreId, 
//			Long emotionId, String fontName, String content) {
//		return new RegisterReviewRequest(coverUrl, author,bookName, isbn, bookUrl, genreName, genreId, 
//			emotionId, fontName, content);
//	}
}

