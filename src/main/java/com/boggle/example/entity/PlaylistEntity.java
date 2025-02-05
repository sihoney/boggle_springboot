package com.boggle.example.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	@Column(name = "emotion_id")
	private Long emotionId;
	
	@Transient
	private Long likeCount;
	
	@Transient
	private boolean likeByAuthUser;
	
	@Transient
	private String nickname;

	private PlaylistEntity (String playlistName, Long userId) {
		this.playlistName = playlistName;
		this.userId = userId;
	}
	
	private PlaylistEntity (String playlistName, Long userId, Long emotionId) {
		this.playlistName = playlistName;
		this.userId = userId;
//		this.emotionId = emotionId;
	}
	
	public static PlaylistEntity of(String playlistName, Long userId) {
		return new PlaylistEntity(playlistName, userId);
	}
	
	public static PlaylistEntity of(String playlistName, Long userId, Long emotionId) {
		return new PlaylistEntity(playlistName, userId, emotionId);
	}
}
