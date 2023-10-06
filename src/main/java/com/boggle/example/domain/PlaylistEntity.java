package com.boggle.example.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity(name = "playlist")
@Table(name = "playlist")
public class PlaylistEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "playlist_id")
	private Long playlistId;
	
	@Column(name = "playlist_name")
	private String playlistName;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Transient
	private Long likeCount;
	
	private PlaylistEntity (String playlistName, Long userId) {
		this.playlistName = playlistName;
		this.userId = userId;
	}
	
	public static PlaylistEntity of(String playlistName, Long userId) {
		return new PlaylistEntity(playlistName, userId);
	}
}
