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
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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

//	@Column(name = "playlist_id")
    @ManyToOne
    @JoinColumn(name = "playlist_id")
	private PlaylistEntity playlistEntity;

//	@Transient
//	@Column(name = "review_id")
//	private Long reviewId;
	
    @ManyToOne
    @JoinColumn(name = "review_id")
    private ReviewEntity reviewEntity;
	
	public ReviewPlaylistEntity(ReviewEntity review, PlaylistEntity playlistEntity) {
		this.reviewEntity = review;
		this.playlistEntity = playlistEntity;
	}
}
