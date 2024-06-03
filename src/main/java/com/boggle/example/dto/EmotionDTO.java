package com.boggle.example.dto;

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
public class EmotionDTO {

	private Long emotionId;
	private String emotionName;
	private Long totalCount;
	private String percentage;
}
