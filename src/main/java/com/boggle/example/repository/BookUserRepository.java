package com.boggle.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.entity.BookUserEntity;

public interface BookUserRepository extends JpaRepository<BookUserEntity, Long>{

	boolean existsByUserIdAndIsbn(Long userId, Long isbn);
	
	BookUserEntity findByUserIdAndIsbn(Long userId, Long isbn);
}
