package com.boggle.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
