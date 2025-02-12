package com.boggle.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long>{

	Optional<BookEntity> findByIsbn(Long isbn);
}
