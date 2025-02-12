package com.boggle.example.exception;

public class PlaylistNotFoundException extends RuntimeException {
	public PlaylistNotFoundException(String message) {
		super(message);
	}
}
