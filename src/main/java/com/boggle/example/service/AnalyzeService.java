package com.boggle.example.service;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boggle.example.dto.AnalyzeResponse;
import com.boggle.example.dto.EmotionDTO;
import com.boggle.example.dto.ReviewCountDTO;
import com.boggle.example.projection.EmotionProjection;
import com.boggle.example.projection.ReviewCountProjection;
import com.boggle.example.projection.ReviewProjection;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.UserRepository;

@Service
public class AnalyzeService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ReviewRepository reviewRepository;
	
	@Transactional
	public AnalyzeResponse main(String nickname, String period) {
		
		System.out.println("AnalyzeService.analyze()");
		
		Long userId = userRepository.findByNickname(nickname).getUserId();
		Integer totalCountByPeriod = null;
		
		LocalDateTime today = LocalDateTime.now();
		List<Tuple> rankingGenre = null;
		List<EmotionProjection> rankingEmotion = null;
		List<ReviewProjection> topReview = null;
        Pageable pageable = PageRequest.of(0, 4); // 첫 번째 페이지에서 4개의 결과를 가져옵니다.
        List<ReviewCountDTO> reviewCountList = null;
        
		if(period.equals("week")) {
	        LocalDateTime monday = today.with(DayOfWeek.MONDAY);
//	        System.out.println("현재 주의 월요일 날짜: " + monday);
	        
	        totalCountByPeriod = reviewRepository.getTotalCountByUserIdAndCreatedAt(userId, monday);
	        LocalDateTime date4WeeksAgo = today.minus(4, ChronoUnit.WEEKS);
	        reviewCountList = getWeeklyReviewCounts(reviewRepository.findWeeklyReviewCountsByUserIdAndPeriod(userId, date4WeeksAgo, today), date4WeeksAgo, today);
	        
//	        rankingGenre = reviewRepository.findGenresByUserIdAndPeriod(userId, monday, pageable);
	        rankingEmotion = reviewRepository.findEmotionsByUserIdAndPeriod(userId, monday, pageable);
	        topReview = reviewRepository.findTopReviewByUserIdAndPeriod(userId, monday, PageRequest.of(0, 1));
	        
		} else if(period.equals("month")) {
	        LocalDateTime firstDayOfMonth = today.withDayOfMonth(1);
//	        System.out.println("현재 달의 첫째 일 날짜: " + firstDayOfMonth);
	        
	        totalCountByPeriod = reviewRepository.getTotalCountByUserIdAndCreatedAt(userId, firstDayOfMonth);
	        LocalDateTime date4MonthsAgo = today.minus(4, ChronoUnit.MONTHS);
	        reviewCountList = getMonthlyReviewCounts(reviewRepository.findMonthlyReviewCountsByUserIdAndPeriod(userId, date4MonthsAgo, today), date4MonthsAgo, today);
	        
//	        rankingGenre = reviewRepository.findGenresByUserIdAndPeriod(userId, firstDayOfMonth, pageable);
	        rankingEmotion = reviewRepository.findEmotionsByUserIdAndPeriod(userId, firstDayOfMonth, pageable);
	        topReview = reviewRepository.findTopReviewByUserIdAndPeriod(userId, firstDayOfMonth, PageRequest.of(0, 1));
	        
		} else if(period.equals("year")) {
	        LocalDateTime firstDayOfYear = today.withDayOfYear(1);
//	        System.out.println("해당 날짜의 년도의 첫째 날짜: " + firstDayOfYear);
	        
	        totalCountByPeriod = reviewRepository.getTotalCountByUserIdAndCreatedAt(userId, firstDayOfYear);
	        LocalDateTime date4YearsAgo = today.minus(4, ChronoUnit.YEARS);
	        reviewCountList = getYearlyReviewCounts(reviewRepository.findYearlyReviewCountsByUserIdAndPeriod(userId, date4YearsAgo, today), date4YearsAgo, today);
	        
//	        rankingGenre = reviewRepository.findGenresByUserIdAndPeriod(userId, firstDayOfYear, pageable);
	        rankingEmotion = reviewRepository.findEmotionsByUserIdAndPeriod(userId, firstDayOfYear, pageable);
	        topReview = reviewRepository.findTopReviewByUserIdAndPeriod(userId, firstDayOfYear, PageRequest.of(0, 1));
		}

		return new AnalyzeResponse(
//				changeTupleToGenreDTO(rankingGenre, totalCountByPeriod),
				null,
				changeProjectionToEmotionDTO(rankingEmotion, totalCountByPeriod),
				totalCountByPeriod,
				topReview,
				reviewCountList
				);
	}
	
    private List<ReviewCountDTO> getWeeklyReviewCounts(List<ReviewCountProjection> reviewCounts, LocalDateTime startDate, LocalDateTime endDate) {
        
        Map<String, Long> reviewCountMap = reviewCounts.stream()
            .collect(Collectors.toMap(ReviewCountProjection::getPeriod, ReviewCountProjection::getReviewCount));

        List<ReviewCountDTO> result = new ArrayList<>();
        
        LocalDateTime currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            String period = currentDate.format(DateTimeFormatter.ofPattern("yyyy-ww"));
            Long reviewCount = reviewCountMap.getOrDefault(period, 0L);
            
            result.add(new ReviewCountDTO(period, reviewCount));
            currentDate = currentDate.plusWeeks(1);
        }

        return result;
    }
    
    private List<ReviewCountDTO> getMonthlyReviewCounts(List<ReviewCountProjection> reviewCounts, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Long> reviewCountMap = reviewCounts.stream()
            .collect(Collectors.toMap(ReviewCountProjection::getPeriod, ReviewCountProjection::getReviewCount));

        List<ReviewCountDTO> result = new ArrayList<>();
        
        LocalDateTime currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            String period = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            Long reviewCount = reviewCountMap.getOrDefault(period, 0L);
            result.add(new ReviewCountDTO(period, reviewCount));
            currentDate = currentDate.plusMonths(1);
        }

        return result;
    }   
    
    private List<ReviewCountDTO> getYearlyReviewCounts(List<ReviewCountProjection> reviewCounts, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Long> reviewCountMap = reviewCounts.stream()
            .collect(Collectors.toMap(ReviewCountProjection::getPeriod, ReviewCountProjection::getReviewCount));

        List<ReviewCountDTO> result = new ArrayList<>();
        
        LocalDateTime currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            String period = currentDate.format(DateTimeFormatter.ofPattern("yyyy"));
            Long reviewCount = reviewCountMap.getOrDefault(period, 0L);
            result.add(new ReviewCountDTO(period, reviewCount));
            currentDate = currentDate.plusYears(1);
        }

        return result;
    }    
	
	private List<EmotionDTO> changeProjectionToEmotionDTO(List<EmotionProjection> list, Integer totalCountByPeriod) {
		
		List<EmotionDTO> emotionDTOs = new ArrayList<>();
		
		double etcPercentage = 100;
		DecimalFormat df = new DecimalFormat("0.0");
		
		for(EmotionProjection projection : list) {
			
//			df.format( ((double) projection.getTotalCount() / (double) totalCountByPeriod) * 100);
			double value = ((double) projection.getTotalCount() / (double) totalCountByPeriod) * 100;
			double roundedValue = Math.round(value * 10.0) / 10.0;
			
	        emotionDTOs.add(new EmotionDTO(
	    		projection.getEmotionId(), 
	    		projection.getEmotionName(), 
	    		projection.getTotalCount(), 
	    		roundedValue + "%"
    		));	
	        
	        etcPercentage -= roundedValue;
		}
		
		emotionDTOs.add(new EmotionDTO(
				null,
				"그 외",
				null,
				Math.round(etcPercentage * 10.0) / 10.0 + "%"
				));
		
		return emotionDTOs;
	}
	
//	private List<GenreDTO> changeTupleToGenreDTO(List<Tuple> list, Integer totalCountByPeriod) {
//		
//	    List<GenreDTO> genreDTOs = new ArrayList<>();
//	    
//	    for (Tuple tuple : list) {
//	        Long genreId = tuple.get("genreId", Long.class);
//	        String genreName = tuple.get("genreName", String.class);
//	        Long totalCount = tuple.get("totalCount", Long.class);
//	        
//	        // DecimalFormat을 사용하여 퍼센티지 소수점 두 자리로 포맷팅
//	        DecimalFormat df = new DecimalFormat("0.0");
//	        
//	        genreDTOs.add(new GenreDTO(
//	        		genreId, 
//	        		genreName, 
//	        		totalCount, 
//	        		df.format( ((double) totalCount / (double) totalCountByPeriod) * 100) + "%"
//	        ));
//	    }
//	    
//	    return genreDTOs;
//	}
}
