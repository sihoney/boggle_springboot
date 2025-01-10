package com.boggle.example.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.PlaylistUserEntity;

public interface PlaylistUserRepository extends JpaRepository<PlaylistUserEntity, Long>{

	Optional<PlaylistUserEntity> findByUserIdAndPlaylistId(Long userId, Long playlistId);
	
	Page<PlaylistUserEntity> findAllByUserId(Long userId, Pageable pageable);
}
