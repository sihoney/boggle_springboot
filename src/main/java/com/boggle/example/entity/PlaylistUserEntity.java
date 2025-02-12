package com.boggle.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@Entity(name = "playlist_user")
@Table(name = "playlist_user")
public class PlaylistUserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "playlist_user_id")
	private Long playlistUserId;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "playlist_id")
	private Long playlistId;
}
