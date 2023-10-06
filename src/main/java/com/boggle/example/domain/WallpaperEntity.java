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
@Entity(name = "wallpaper")
@Table(name = "wallpaper")
public class WallpaperEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wallpaper_id")
	private Long wallpaperId;
	
	@Column(name = "wallpaper_name")
	private String wallpaperName;
}
