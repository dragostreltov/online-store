package com.spring.onlinestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalOperation extends RuntimeException{

	private static final long serialVersionUID = -1254539329299929811L;
	
	public IllegalOperation(String message) {
		super(message);
	}

}
