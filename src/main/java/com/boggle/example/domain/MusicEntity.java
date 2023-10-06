package com.boggle.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Entity(name = "music")
@Table(name = "music")
public class MusicEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "music_id")
	private Long musicId;
	
	@Column(name = "music_name")
	private String musicName;
	
	@Column(name = "artist")
	private String artist;
	
	@Column(name = "music_path")
	private String musicPath;
}
