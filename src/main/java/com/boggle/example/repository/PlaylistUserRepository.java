package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.PlaylistUserEntity;

public interface PlaylistUserRepository extends JpaRepository<PlaylistUserEntity, Long>{

}
