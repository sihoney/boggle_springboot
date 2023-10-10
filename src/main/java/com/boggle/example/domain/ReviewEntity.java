package com.boggle.example.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter 
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "review")
@Table(name = "review")
public class ReviewEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;
	
	@Column(name = "font_id")
	private Long fontId;
	
	@Column(name = "wallpaper_id")
	private Long wallpaperId;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "isbn")
	private BookEntity bookEntity;

	@ManyToOne
	@JoinColumn(name = "emotion_id")
	private EmotionEntity emotionEntity;
	
	@OneToMany
	@JoinColumn(name = "review_id", referencedColumnName = "review_id")
	private List<ReviewUserEntity> reviewUserEntityList = new ArrayList<ReviewUserEntity>();
	
	@Transient
	private Integer likeCount;
	
	@Transient
	private boolean likeByAuthUser;
	
	@Transient
	private String nickname;
	
	private ReviewEntity(String content, Long userId, EmotionEntity emotionEntity, Long fontId, LocalDateTime createAt) {
		this.content = content;
		this.userId = userId;
		this.emotionEntity = emotionEntity;
		this.fontId = fontId;
		this.createdAt = createAt;
	}
	
	public static ReviewEntity of(String content, Long userId, EmotionEntity emotionEntity, Long fontId, LocalDateTime createdAt) {
		return new ReviewEntity(content, userId, emotionEntity, fontId, createdAt);
	}
}
