package com.boggle.example.util;

import java.util.HashMap;
import java.util.Map;

public class PagingUtil {

	public static Map<String, Integer> pagination (Integer totalEntity, Integer pageSize, Integer currentPage) {
		
		Integer startPage;
		Integer endPage;
		
		Integer btnWidth = 5;
		Integer totalPages = (int) Math.ceil(totalEntity / (double) pageSize);

		if(currentPage < 0) {
			throw new Error("currentPage is minus");
		} 
		else if(totalPages > 0 && currentPage > totalPages) {
			throw new Error("currentPage is overflow.");
		}
		else if(totalPages == 0 || totalPages == 1) {
			startPage = 1;
			endPage = 1;
		} 
		else {
		startPage = (int) (Math.ceil(currentPage / (double) btnWidth) * 5 - 4);
		endPage = Math.min(totalPages, startPage + 4);			
		}
		
		Map<String, Integer> map = new HashMap<>();
		map.put("startPage", startPage);
		map.put("endPage", endPage);
		map.put("totalPageNo", totalPages);
		
		return map;
	}
}
