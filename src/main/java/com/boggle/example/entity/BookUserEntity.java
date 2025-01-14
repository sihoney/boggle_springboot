package com.boggle.example.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "book_user")
@Table(name = "book_user")
public class BookUserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_user_id")
	private Long bookUserId;
	
	@Column(name = "isbn")
	private Long isbn;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "added_at")
	private LocalDateTime addedAt;
	
}
