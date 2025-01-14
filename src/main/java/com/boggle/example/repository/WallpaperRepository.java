package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.entity.WallpaperEntity;

public interface WallpaperRepository extends JpaRepository<WallpaperEntity, Long>{

}
