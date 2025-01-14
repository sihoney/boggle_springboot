package com.boggle.example.dto;

import java.util.List;

import com.boggle.example.entity.BookEntity;
import com.boggle.example.entity.EmotionEntity;
import com.boggle.example.entity.FontEntity;
import com.boggle.example.entity.ReviewEntity;
import com.boggle.example.entity.WallpaperEntity;

import lombok.Getter;

@Getter
public class WriteFormResponse {

	private List<EmotionEntity> emotionList;
	private List<FontEntity> fontList;
	private List<WallpaperEntity> wallpaperList;
	private ReviewEntity reviewEntity;
	private BookEntity bookEntity;
	
	private WriteFormResponse(List<EmotionEntity> emotions, List<FontEntity> fonts, List<WallpaperEntity> wallpapers, BookEntity bookEntity, ReviewEntity reviewEntity) {
		this.emotionList = emotions;
		this.fontList = fonts;
		this.wallpaperList = wallpapers;
		this.bookEntity = bookEntity;
		this.reviewEntity = reviewEntity;
	}
	
	public static WriteFormResponse of(List<EmotionEntity> emotions, List<FontEntity> fonts, List<WallpaperEntity> wallpapers, BookEntity bookEntity, ReviewEntity reviewEntity) {
		return new WriteFormResponse(emotions, fonts, wallpapers, bookEntity, reviewEntity);
	}
}
