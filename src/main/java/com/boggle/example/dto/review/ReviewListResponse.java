package com.boggle.example.dto.review;

import java.util.List;

import com.boggle.example.dto.user.LoginResponse;
import com.boggle.example.entity.ReviewEntity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewListResponse {

	private int startPage;
	private int endPage;
	private List<ReviewEntity> reviewList;
	private LoginResponse userProfile;
	private String sort;
	
	private ReviewListResponse(int startPage, int endPage, List<ReviewEntity> reviewList, LoginResponse userProfile, String sort) {
		this.startPage = startPage;
		this.endPage = endPage;
		this.reviewList = reviewList;
		this.userProfile = userProfile;
		this.sort = sort;
	}
	
	public static ReviewListResponse of(List<ReviewEntity> reviewList, String sort, LoginResponse userProfile, int startPage, int endPage) {
		return new ReviewListResponse(startPage, endPage, reviewList, userProfile, sort);
	}
}
