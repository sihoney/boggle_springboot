package com.boggle.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaylistDTO {

	private Long playlistId;
	
	private String playlistName;
	
	private boolean hasReview;
}
