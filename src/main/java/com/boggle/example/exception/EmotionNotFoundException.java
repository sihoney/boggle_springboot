package com.boggle.example.exception;

public class EmotionNotFoundException extends RuntimeException {
	public EmotionNotFoundException(String message) {
		super(message);
	}
}
