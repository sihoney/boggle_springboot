package com.boggle.example.projection;

import java.time.LocalDateTime;

public interface ReviewCountProjection {
	String getPeriod();
	Long getReviewCount();
}
