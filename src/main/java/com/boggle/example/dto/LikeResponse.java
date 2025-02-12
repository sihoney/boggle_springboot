package com.boggle.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {
	   private final boolean liked;    // 현재 좋아요 상태
	   private final long likeCount;   // 현재 총 좋아요 수
}
