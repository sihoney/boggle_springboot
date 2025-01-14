package com.boggle.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boggle.example.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long>{

	BookEntity findByIsbn(Long isbn);
}
