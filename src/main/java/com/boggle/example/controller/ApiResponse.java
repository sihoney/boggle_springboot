package com.boggle.example.controller;

import java.util.List;

import com.boggle.example.domain.BookEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class ApiResponse {

//	private boolean success;
//	private String resultMessage;
	private Integer totalResults;
	private List<BookEntity> item;

	public ApiResponse () {
		
	}
}
