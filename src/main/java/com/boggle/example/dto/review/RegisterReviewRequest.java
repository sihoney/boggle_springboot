package com.boggle.example.dto.review;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegisterReviewRequest {
	
	@JsonProperty("cover")
	private String coverUrl;
	
	private String author;
	
	@JsonProperty("title")
	private String bookName;
	
	@NotNull(message = "ISBN은 필수입니다.")
	private Long isbn;
	
	@JsonProperty("url")
	private String bookUrl;
	
	@JsonProperty("category")
	private String genreName;
	
	@JsonProperty("categoryid")
	private Long genreId;
	
	@NotNull(message = "감정 ID는 필수입니다.")
	private Long emotionId;
	
	private String fontName;
	
	@NotNull(message = "폰트 ID는 필수입니다.")
	private Long fontId;
	
	@NotBlank(message = "서평 내용은 필수입니다.")
	@Size(max = 1000, message = "서평은 1000자를 초과할 수 없습니다.")
	@JsonProperty("reviewContent")
	private String content;
	
	private Long wallpaperId;
	
	private Long reviewId;
	
    public void validate(Long pathReviewId) {
        if (!reviewId.equals(pathReviewId)) {
            throw new IllegalArgumentException("요청 본문의 reviewId와 경로 변수의 reviewId가 일치하지 않습니다.");
        }
    }
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

