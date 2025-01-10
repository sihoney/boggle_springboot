package com.boggle.example.dto;

import java.util.List;

import com.boggle.example.projection.ReviewCountProjection;
import com.boggle.example.projection.ReviewProjection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeResponse {

	private List<GenreDTO> genreList;
	private List<EmotionDTO> emotionList;
	private Integer totalCount;
	private List<ReviewProjection> topReview;
	private List<ReviewCountDTO> reviewCountList;
}
