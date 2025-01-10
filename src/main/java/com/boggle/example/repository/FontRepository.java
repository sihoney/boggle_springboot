package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.domain.FontEntity;

public interface FontRepository extends JpaRepository<FontEntity, Long>{

}
