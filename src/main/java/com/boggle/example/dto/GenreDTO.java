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
public class GenreDTO {

	private Long genreId;
	private String genreName;
	private Long totalCount;
	private String percentage;
}
