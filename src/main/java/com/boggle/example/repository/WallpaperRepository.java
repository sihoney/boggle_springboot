package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.WallpaperEntity;

public interface WallpaperRepository extends JpaRepository<WallpaperEntity, Long>{

}
