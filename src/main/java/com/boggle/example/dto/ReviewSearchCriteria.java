package com.boggle.example.dto;

import com.boggle.example.util.SearchType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewSearchCriteria {
	private Long userId;
	private Long authUserId;
	private String nickname;
	private Long emotionId;
	private String emotionName;
	private String query;
	private boolean sortByLikes;
	private SearchType searchType;
}
