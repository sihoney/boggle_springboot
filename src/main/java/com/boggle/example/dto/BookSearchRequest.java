package com.boggle.example.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchRequest {
	private final String query;
	private final Integer maxResults;
	private final Integer start;
}
