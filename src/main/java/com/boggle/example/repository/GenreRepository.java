package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.GenreEntity;

public interface GenreRepository extends JpaRepository<GenreEntity, Long>{

	
}
