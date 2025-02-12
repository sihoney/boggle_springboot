package com.boggle.example.exception;

public class AladinApiException extends RuntimeException {
	public AladinApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
