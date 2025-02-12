package com.boggle.example.dto;

import java.util.List;

import com.boggle.example.entity.BookEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchedBooksResponse {

	private Integer startPage;
	private Integer endPage;
    private final int totalPages;
    private final int currentPage;
    private final boolean hasNext;
    private final boolean hasPrevious;	
	
	private List<BookDTO> bookList;

 }
