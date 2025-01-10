package com.boggle.example.util;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PagingUtilTest {

	@Autowired
	PagingUtil pagingUtil;
	
	@Test
	public void pagination() {
//		//Given
//		
//		
//		//When
//		// 개수가 0인 경우 -> start = 1 end = 1
//		Map<String, Integer> map01 = PagingUtil.pagination(0, 8, 1);
//		
//		// totalPages > 5, page = 1 -> start = 1 end = 5
//		Map<String, Integer> map02 = PagingUtil.pagination(100, 8, 1);
//		
//		// totalPages < 5, page = 1 -> start = 1 end = 4
//		Map<String, Integer> map03 = PagingUtil.pagination(30, 8, 1);
//		
//		// totalPages > 5, page = 5 -> start = 1 end = 5
//		Map<String, Integer> map04 = PagingUtil.pagination(45, 8, 5);
//		
//		// totalPages > 5, page = 6 -> start = 6 end = 6
//		Map<String, Integer> map05 = PagingUtil.pagination(45, 8, 6);
//	
//		// totalPages < 5, page = 2 -> start = 1 end = 4
//		Map<String, Integer> map06 = PagingUtil.pagination(32, 8, 2);
//
//		// totalPages < 5, page = 4 -> start = 1 end = 4
//		Map<String, Integer> map07 = PagingUtil.pagination(32, 8, 4);
//		
//		
//		//Then
//		Assertions.assertEquals(1, map01.get("startPage"));
//		Assertions.assertEquals(1, map01.get("endPage"));
//		
//		Assertions.assertEquals(1, map02.get("startPage"));
//		Assertions.assertEquals(5, map02.get("endPage"));
//		
//		Assertions.assertEquals(1, map03.get("startPage"));
//		Assertions.assertEquals(4, map03.get("endPage"));
//		
//		Assertions.assertEquals(1, map04.get("startPage"));
//		Assertions.assertEquals(5, map04.get("endPage"));
//		
//		Assertions.assertEquals(6, map05.get("startPage"));
//		Assertions.assertEquals(6, map05.get("endPage"));
//		
//		Assertions.assertEquals(1, map06.get("startPage"));
//		Assertions.assertEquals(4, map06.get("endPage"));
//		
//		Assertions.assertEquals(1, map07.get("startPage"));
//		Assertions.assertEquals(4, map07.get("endPage"));
	}
}
