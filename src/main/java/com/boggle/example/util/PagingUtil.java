package com.boggle.example.util;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

public class PagingUtil {

	private static final int DEFAULT_BUTTON_COUNT = 5;
	private static final int MIN_PAGE = 1;
	
	@Getter
	@Builder
	public static class PagingResult {
		private final int startPage;
		private final int endPage;
		private final int totalPages;
		private final int currentPage;
		private final boolean hasPrevious;
		private final boolean hasNext;
	}

    /**
     * 페이지네이션 정보를 계산합니다.
     *
     * @param totalElements 전체 항목 수
     * @param pageSize 페이지당 항목 수
     * @param currentPage 현재 페이지 번호 (1부터 시작)
     * @return PagingResult 페이지네이션 계산 결과
     * @throws IllegalArgumentException 잘못된 입력값이 제공될 경우
     */
    public static PagingResult calculatePagination(int totalElements, int pageSize, int currentPage) {
        validateInputs(totalElements, pageSize, currentPage);

        int totalPages = calculateTotalPages(totalElements, pageSize);
        
        // 결과가 없거나 단일 페이지인 경우
        if (totalPages <= 1) {
            return createSinglePageResult(totalPages, currentPage);
        }

        // 일반적인 경우의 페이지네이션 계산
        int startPage = calculateStartPage(currentPage, DEFAULT_BUTTON_COUNT);
        int endPage = calculateEndPage(startPage, totalPages);
        
        return PagingResult.builder()
                .startPage(startPage)
                .endPage(endPage)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .hasPrevious(currentPage > 1)
                .hasNext(currentPage < totalPages)
                .build();
    }

    /**
     * 사용자 정의 버튼 수로 페이지네이션 정보를 계산합니다.
     *
     * @param totalElements 전체 항목 수
     * @param pageSize 페이지당 항목 수
     * @param currentPage 현재 페이지 번호
     * @param buttonCount 화면에 표시할 페이지 버튼의 수
     * @return PagingResult 페이지네이션 계산 결과
     * @throws IllegalArgumentException 잘못된 입력값이 제공될 경우
     */
    public static PagingResult calculatePagination(int totalElements, int pageSize, int currentPage, int buttonCount) {
        validateInputs(totalElements, pageSize, currentPage);
        validateButtonCount(buttonCount);

        int totalPages = calculateTotalPages(totalElements, pageSize);
        
        if (totalPages <= 1) {
            return createSinglePageResult(totalPages, currentPage);
        }

        int startPage = calculateStartPage(currentPage, buttonCount);
        int endPage = calculateEndPage(startPage, totalPages, buttonCount);
        
        return PagingResult.builder()
                .startPage(startPage)
                .endPage(endPage)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .hasPrevious(currentPage > 1)
                .hasNext(currentPage < totalPages)
                .build();
    }

    private static void validateInputs(int totalElements, int pageSize, int currentPage) {
        if (totalElements < 0) {
            throw new IllegalArgumentException("Total elements cannot be negative");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be positive");
        }
        if (currentPage < MIN_PAGE) {
            throw new IllegalArgumentException("Current page must be greater than or equal to 1");
        }
    }

    private static void validateButtonCount(int buttonCount) {
        if (buttonCount <= 0) {
            throw new IllegalArgumentException("Button count must be positive");
        }
    }

    private static int calculateTotalPages(int totalElements, int pageSize) {
        return (int) Math.ceil(totalElements / (double) pageSize);
    }

    private static PagingResult createSinglePageResult(int totalPages, int currentPage) {
        return PagingResult.builder()
                .startPage(MIN_PAGE)
                .endPage(Math.max(totalPages, MIN_PAGE))
                .totalPages(totalPages)
                .currentPage(currentPage)
                .hasPrevious(false)
                .hasNext(false)
                .build();
    }

    private static int calculateStartPage(int currentPage, int buttonCount) {
        return Math.max(MIN_PAGE, (int) (Math.ceil(currentPage / (double) buttonCount) * buttonCount - (buttonCount - 1)));
    }

    private static int calculateEndPage(int startPage, int totalPages) {
        return Math.min(totalPages, startPage + DEFAULT_BUTTON_COUNT - 1);
    }

    private static int calculateEndPage(int startPage, int totalPages, int buttonCount) {
        return Math.min(totalPages, startPage + buttonCount - 1);
    }
	
	public static Map<String, Integer> pagination (Integer totalEntity, Integer pageSize, Integer currentPage) {
		
		Integer startPage;
		Integer endPage;
//		Integer prev;
//		Integer next;
		
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
//			prev = 0;
//			next = 0;
		} 
		else {
			startPage = (int) (Math.ceil(currentPage / (double) btnWidth) * 5 - 4);
			endPage = Math.min(totalPages, startPage + 4);	

		}
		
		Map<String, Integer> map = new HashMap<>();
		map.put("startPage", startPage);
		map.put("endPage", endPage);
		map.put("totalPageNo", totalPages);
//		map.put("prev", prev);
//		map.put("next", next);
		
		return map;
	}
}
