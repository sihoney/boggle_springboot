package com.boggle.example.exception;

public class ReviewNotFoundException extends RuntimeException {
	public ReviewNotFoundException(String message) {
		super(message);
	}
}
