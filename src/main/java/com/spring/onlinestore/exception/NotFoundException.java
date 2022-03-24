package com.spring.onlinestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1057128548231913507L;

	public NotFoundException(String message) {
		super(message);
	}
}
