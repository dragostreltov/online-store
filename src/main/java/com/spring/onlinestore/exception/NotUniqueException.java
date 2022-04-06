package com.spring.onlinestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotUniqueException extends RuntimeException{

	private static final long serialVersionUID = -2919199439110114345L;

	public NotUniqueException(String message) {
		super(message);
	}
}
