package com.boggle.example.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException {
	public InvalidRequestException(String message) {
		super(message);
	}
}
