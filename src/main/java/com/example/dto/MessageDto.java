package com.example.dto;

import java.io.Serializable;

public class MessageDto implements Serializable {

	private static final long serialVersionUID = -5034360812590015991L;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return String.format("Message [message=%s]", message);
	}

}
