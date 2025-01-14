package com.boggle.example.dto;

import java.util.List;

import com.boggle.example.entity.BookEntity;

import lombok.Getter;

@Getter
public class SearchedBooksResponse {

	private Integer totalPageNo;
	private Integer startPage;
	private Integer endPage;
	private List<BookEntity> bookList;
	
	private SearchedBooksResponse(Integer totalPageNo, Integer startPage, Integer endPage, List<BookEntity> bookList) {
		this.totalPageNo = totalPageNo;
		this.startPage = startPage;
		this.endPage = endPage;
		this.bookList = bookList;
	}
	
	public static SearchedBooksResponse of(List<BookEntity> bookList, Integer startPage, Integer endPage, Integer totalPageNo) {
		return new SearchedBooksResponse(totalPageNo, startPage, endPage, bookList);
	}
 }
