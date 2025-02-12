package com.boggle.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResult {
    private final boolean liked;    // true: 좋아요 추가, false: 좋아요 취소
    private final long likeCount;   // 최종 좋아요 수
}
