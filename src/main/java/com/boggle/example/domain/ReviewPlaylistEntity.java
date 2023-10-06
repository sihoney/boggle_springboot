package com.boggle.example.domain;

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
@Entity(name = "review_playlist")
@Table(name = "review_playlist")
public class ReviewPlaylistEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_playlist_id")
	private Long reviewPlaylistId;

	@Column(name = "added_at")
	private LocalDateTime addedAt;

	@Column(name = "playlist_id")
	private Long playlistId;

	@Column(name = "review_id")
	private Long reviewId;
	
	public ReviewPlaylistEntity(Long reviewId, Long playlistId) {
		this.reviewId = reviewId;
		this.playlistId = playlistId;
	}
}
