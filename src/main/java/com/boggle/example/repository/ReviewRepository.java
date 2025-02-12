package com.boggle.example.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boggle.example.entity.EmotionEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.projection.EmotionProjection;
import com.boggle.example.projection.ReviewCountProjection;
import com.boggle.example.projection.ReviewProjection;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{

	Optional<ReviewEntity> findByReviewId(Long reviewId);
	
	Page<ReviewEntity> findAllByUserId(Long userId, Pageable pageable);
	
	Page<ReviewEntity> findAllByUserIdAndContentContaining(Long userId, String query, Pageable pageable);

	Page<ReviewEntity> findAllByUserIdAndEmotionEntity_EmotionId(Long userId, Long emotionId, Pageable pageable);

	Page<ReviewEntity> findAllByBookEntity_isbn(Long isbn, Pageable pageable);

//	@Query("SELECT r, count(ru) AS likeCount FROM review r " + 
//				"LEFT JOIN review_user ru ON r.reviewId = ru.reviewId " + 
//				"WHERE r.userId = ?1 " + 
//				"GROUP BY r.reviewId")
//	Page<Object[]> getAllReviewSortByLikeCount(Long userId, Pageable pageable);
	
	void deleteByUserIdAndReviewId(Long userId, Long reviewId);

	Page<ReviewEntity> findAllByEmotionEntity(EmotionEntity emotionEntity, Pageable pageable);
	
//		       "LEFT JOIN review_user ru ON ru.reviewId = r.reviewId AND ru.userId = ?2 " +
//		       "GROUP BY r.reviewId"
//	@Query("SELECT r, u.nickname " +
//				"FROM review r " +
//		       "INNER JOIN r.review_playlist rp " +
//		       "INNER JOIN r.users u " + 
//		       "WHERE rp.playlistId = :playlistId")
//	Page<Object[]> getAllReviewByPlaylistId(Long playlistId, Pageable pageable);
	
    @Query("SELECT COUNT(reviewId) " + 
    		"FROM review " + 
    		"WHERE userId = :userId " + 
    		"AND createdAt BETWEEN :firstDay AND CURRENT_TIMESTAMP()")
    Integer getTotalCountByUserIdAndCreatedAt(@Param("userId") Long userId, @Param("firstDay") LocalDateTime firstDay);
    
//    주, 월, 년 기간 서평 총 개수
    @Query("SELECT DATE_FORMAT(r.createdAt, '%Y-%u') AS period, COUNT(r) AS reviewCount " +
           "FROM review r " +
           "WHERE r.userId = :userId " +
           "AND r.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE_FORMAT(r.createdAt, '%Y-%u') " +
           "ORDER BY period")
    List<ReviewCountProjection> findWeeklyReviewCountsByUserIdAndPeriod(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DATE_FORMAT(r.createdAt, '%Y-%m') AS period, COUNT(r) AS reviewCount " +
           "FROM review r " +
           "WHERE r.userId = :userId " +
           "AND r.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE_FORMAT(r.createdAt, '%Y-%m') " +
           "ORDER BY period")
    List<ReviewCountProjection> findMonthlyReviewCountsByUserIdAndPeriod(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DATE_FORMAT(r.createdAt, '%Y') AS period, COUNT(r) AS reviewCount " +
           "FROM review r " +
           "WHERE r.userId = :userId " +
           "AND r.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE_FORMAT(r.createdAt, '%Y') " +
           "ORDER BY period")
    List<ReviewCountProjection> findYearlyReviewCountsByUserIdAndPeriod(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	
    @Query("SELECT g.genreId AS genreId, g.genreName AS genreName, COUNT(g.genreId) AS totalCount " +
            "FROM review r " +
//            "INNER JOIN book b ON r.isbn = b.isbn " +
            "INNER JOIN genre g ON r.bookEntity.genreId = g.genreId " +
            "WHERE r.userId = :userId " +
            "AND r.createdAt BETWEEN :firstDay AND CURRENT_TIMESTAMP() " +
            "GROUP BY g.genreId " +
            "ORDER BY totalCount DESC")
    List<Tuple> findGenresByUserIdAndPeriod(@Param("userId") Long userId, @Param("firstDay") LocalDateTime firstDay, Pageable pageable);     
    	
//    @Query("SELECT r.emotionEntity.emotionId AS emotionId, r.emotionEntity.emotionName AS emotionName, COUNT(r.emotionEntity.emotionId) AS totalCount " + 
//    	    "FROM review r " +
//    	    "WHERE r.userId = :userId " +
//    	    "AND r.createdAt BETWEEN :firstDay AND CURRENT_TIMESTAMP() " +
//    	    "GROUP BY r.emotionEntity.emotionId " + 
//    	    "ORDER BY totalCount DESC")
//    List<Tuple> findEmotionsByUserIdAndPeriod(@Param("userId") Long userId, @Param("firstDay") LocalDateTime firstDay, Pageable pageable);     

    @Query("SELECT r.emotionEntity.emotionId AS emotionId, r.emotionEntity.emotionName AS emotionName, COUNT(r.emotionEntity.emotionId) AS totalCount " + 
    	    "FROM review r " +
    	    "WHERE r.userId = :userId " + 
    	    "AND r.createdAt BETWEEN :firstDay AND CURRENT_TIMESTAMP() " +
    	    "GROUP BY r.emotionEntity.emotionId " +
    	    "ORDER BY totalCount DESC")
    List<EmotionProjection> findEmotionsByUserIdAndPeriod(@Param("userId") Long userId, @Param("firstDay") LocalDateTime firstDay, Pageable pageable);     
  
//    @Query(value = "SELECT b.book_name AS bookName, r.content AS content, COUNT(ru.review_user_id) AS likeCount, r.created_at AS createdAt " +
//	            "FROM review r " +
//	            "INNER JOIN book b ON r.isbn = b.isbn " +
//	            "INNER JOIN review_user ru ON r.review_id = ru.review_id " +
//	            "WHERE r.user_id = :userId " +
//	            "AND r.created_at BETWEEN :firstDay AND CURRENT_TIMESTAMP() " +
//	            "GROUP BY r.review_id " +
//	            "ORDER BY likeCount DESC " +
//	            "LIMIT 1", nativeQuery = true)
//    Tuple findTopReviewByUserIdAndPeriod(@Param("userId") Long userId, @Param("firstDay") LocalDateTime firstDay);

  @Query("SELECT r.reviewId AS reviewId, r.content AS content, r.createdAt AS createdAt, r.bookEntity.bookName AS bookName, r.emotionEntity.emotionName AS emotionName, COUNT(ru.reviewUserId) AS likeCount " +
	    "FROM review r " +
//	    "INNER JOIN book b ON r.isbn = b.isbn " +
	    "INNER JOIN review_user ru ON r.reviewId = ru.reviewEntity.reviewId " +
	    "WHERE r.userId = :userId " +
	    "AND r.createdAt BETWEEN :firstDay AND CURRENT_TIMESTAMP() " +
	    "GROUP BY r.reviewId " +
	    "ORDER BY likeCount DESC ")
	List<ReviewProjection> findTopReviewByUserIdAndPeriod(@Param("userId") Long userId, @Param("firstDay") LocalDateTime firstDay, Pageable pageable);
}