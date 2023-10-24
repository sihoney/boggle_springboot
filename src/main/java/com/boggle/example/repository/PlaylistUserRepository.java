package com.boggle.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.PlaylistUserEntity;

public interface PlaylistUserRepository extends JpaRepository<PlaylistUserEntity, Long>{

	Optional<PlaylistUserEntity> findByUserIdAndPlaylistId(Long userId, Long playlistId);
}
