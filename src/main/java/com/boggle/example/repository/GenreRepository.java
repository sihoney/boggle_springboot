package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.GenreEntity;

public interface GenreRepository extends JpaRepository<GenreEntity, Long>{

//    @Query("SELECT g.genreId, g.genreName, COUNT(g.genreId) AS totalCount" +
//            "FROM ReviewEntity r " +
//            "INNER JOIN BookEntity b ON r.isbn = b.isbn " +
//            "INNER JOIN GenreEntity g ON b.genreId = g.genreId " +
//            "WHERE r.userId = :userId " +
//            "AND r.createdAt BETWEEN :firstDay AND CURRENT_TIMESTAMP() " +
//            "GROUP BY b.genreId")
//	List<GenreEntity> findAllByUserIdAndPeriod(Long userId, LocalDate firstDay);
    
//    @Query("SELECT g.genreId, g.genreName, COUNT(g.genreId) AS totalCount " +
//            "FROM review r " +
//            "INNER JOIN book b ON r.isbn = b.isbn " +
//            "INNER JOIN genre g ON b.genreId = g.genreId " +
//            "WHERE r.userId = :userId " +
//            "AND r.createdAt BETWEEN :firstDay AND CURRENT_TIMESTAMP() " +
//            "GROUP BY b.genreId")
//    List<Object> findAllByUserIdAndPeriod(@Param("userId") Long userId, @Param("firstDay") LocalDate firstDay);    

//    @Query("SELECT g.genreId, g.genreName, COUNT(g.genreId) AS totalCount " +
//            "FROM review r " +
//            "WHERE r.userId = :userId " +
//            "AND r.createdAt BETWEEN :firstDay AND CURRENT_TIMESTAMP() " +
//            "GROUP BY r.bookEntity.genreEntity.genreId")
//    List<Object> findAllByUserIdAndPeriod(@Param("userId") Long userId, @Param("firstDay") LocalDate firstDay);

//    @Query("SELECT g.genreId, g.genreName, COUNT(g.genreId) AS totalCount " +
//            "FROM Review r " +
//            "INNER JOIN Book b ON r.isbn = b.isbn " +
//            "INNER JOIN Genre g ON b.genreId = g.genreId " +
//            "WHERE r.user.id = :userId " +
//            "AND r.createdAt BETWEEN :firstDay AND CURRENT_TIMESTAMP() " +
//            "GROUP BY g.genreId, g.genreName")
//    List<Object> findAllByUserIdAndPeriod(@Param("userId") Long userId, @Param("firstDay") LocalDate firstDay);   
    
}
