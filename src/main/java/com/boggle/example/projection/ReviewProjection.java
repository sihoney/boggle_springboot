package com.boggle.example.projection;

import java.time.LocalDateTime;

public interface ReviewProjection {
    Long getReviewId();
    String getContent();
    LocalDateTime getCreatedAt();
	String getBookName();
	String getEmotionName();
    Long getLikeCount();
}
