package com.boggle.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaylistDTO {

	private Long playlistId;
	
	private String playlistName;
	
	private boolean hasReview;
}
