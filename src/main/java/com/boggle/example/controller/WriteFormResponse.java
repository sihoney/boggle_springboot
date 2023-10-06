package com.boggle.example.controller;

import java.util.List;

import com.boggle.example.domain.EmotionEntity;
import com.boggle.example.domain.FontEntity;
import com.boggle.example.domain.WallpaperEntity;

import lombok.Getter;

@Getter
public class WriteFormResponse {

	private List<EmotionEntity> emotionList;
	private List<FontEntity> fontList;
	private List<WallpaperEntity> wallpaperList;
	
	private WriteFormResponse(List<EmotionEntity> emotions, List<FontEntity> fonts, List<WallpaperEntity> wallpapers) {
		this.emotionList = emotions;
		this.fontList = fonts;
		this.wallpaperList = wallpapers;
	}
	
	public static WriteFormResponse of(List<EmotionEntity> emotions, List<FontEntity> fonts, List<WallpaperEntity> wallpapers) {
		return new WriteFormResponse(emotions, fonts, wallpapers);
	}
}
