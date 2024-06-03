package com.boggle.example.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity(name = "review_user")
@Table(name = "review_user")
public class ReviewUserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_user_id")
	private Long reviewUserId;
	
	@Column(name = "user_id")
	private Long userId;
	
//	@Column(name = "review_id")
//	@Transient
//	private Long reviewId;
	
	@ManyToOne
	@JoinColumn(name = "review_id")
	private ReviewEntity reviewEntity;
	
	@Column(name = "addedAt")
	private LocalDateTime addedAt;
	
	private ReviewUserEntity(Long userId, ReviewEntity reviewEntity, LocalDateTime addedAt) {
		this.userId = userId;
		this.reviewEntity = reviewEntity;
		this.addedAt = addedAt;
	}
	
	public static ReviewUserEntity of(Long userId, ReviewEntity reviewEntity, LocalDateTime addedAt) {
		return new ReviewUserEntity(userId, reviewEntity, addedAt);
	}
}
