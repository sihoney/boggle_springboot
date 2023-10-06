package com.boggle.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boggle.example.domain.PlaylistEntity;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long>{
	
	List<PlaylistEntity> findByUserId(Long userId);
	
	// 인기있는 플리
	@Query("SELECT p, count(pu) AS likeCount FROM playlist p LEFT JOIN playlist_user pu ON p.playlistId = pu.playlistId GROUP BY p.playlistId")
	List<Object[]> findOrderByLikeCount(Pageable pageable);
	
	// 내가 좋아요한 플리
	@Query("SELECT p FROM playlist p LEFT JOIN playlist_user pu ON p.playlistId = pu.playlistId WHERE pu.userId = ?1")
	List<PlaylistEntity> findByPlaylistUser_UserId(Long userId);
}
