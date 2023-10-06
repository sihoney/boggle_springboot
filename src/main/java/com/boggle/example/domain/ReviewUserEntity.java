package com.boggle.example.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity(name = "review_user")
@Table(name = "review_user")
public class ReviewUserEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_user_id")
	private Long reviewUserId;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "review_id")
	private Long reviewId;
	
	@Column(name = "addedAt")
	private LocalDateTime addedAt;
	
	public static ReviewUserEntity of(Long userId, Long reviewId, LocalDateTime addedAt) {
		return new ReviewUserEntity(userId, reviewId, addedAt);
	}
	
	private ReviewUserEntity(Long userId, Long reviewId, LocalDateTime addedAt) {
		this.userId = userId;
		this.reviewId = reviewId;
		this.addedAt = addedAt;
	}
}
