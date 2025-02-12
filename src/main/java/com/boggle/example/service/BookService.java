package com.boggle.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.boggle.example.entity.BookEntity;
import com.boggle.example.entity.BookUserEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.repository.BookRepository;
import com.boggle.example.repository.BookUserRepository;
import com.boggle.example.repository.ReviewRepository;
import com.boggle.example.repository.UserRepository;
import com.boggle.example.util.PagingUtil;

@Service
public class BookService {

	@Autowired
	BookRepository bookRepository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	BookUserRepository bookUserRepository;
	@Autowired
	UserRepository userRepository;
	
/*
	bookDetail 
	toggleBookLike
 */
	
	public Map<String, Object> bookDetail(Long isbn, Pageable pageable, Long userId) {
		
		BookEntity bookEntity = bookRepository.findById(isbn).orElse(null);
		
		Page<ReviewEntity> page = reviewRepository.findAllByBookEntity_isbn(isbn, pageable);
		List<ReviewEntity> reviewList = page.getContent().stream().map(entity -> {
			UserEntity userEntity = userRepository.findById(entity.getUserId()).orElse(null);
			
			if(!Objects.isNull(userEntity)) {
				entity.setNickname(userEntity.getNickname());
			}
			
			return entity;
		}).collect(Collectors.toList());
		
//		(Integer totalEntity, Integer pageSize, Integer currentPage)
		Map<String, Integer> pagination = PagingUtil.pagination((int) page.getTotalElements(), pageable.getPageSize(), pageable.getPageNumber());
		
		boolean likeBook = bookUserRepository.existsByUserIdAndIsbn(userId, isbn);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bookEntity", bookEntity);
		map.put("reviewList", reviewList);
		map.put("startPage", pagination.get("startPage"));
		map.put("endPage", pagination.get("endPage"));
		map.put("likeBook", likeBook);
		
		return map;
	}
	
	public Long toggleBookLike(Long userId, Long isbn) {

		BookUserEntity existingEntity = bookUserRepository.findByUserIdAndIsbn(userId, isbn);
		
		if(Objects.isNull(existingEntity)) {
			
			BookUserEntity entity = new BookUserEntity();
			entity.setIsbn(isbn);
			entity.setUserId(userId);
			
			return bookUserRepository.save(entity).getBookUserId();
			
		} else {
			bookUserRepository.delete(existingEntity);
			
			return 0L;
		}
	}
}
