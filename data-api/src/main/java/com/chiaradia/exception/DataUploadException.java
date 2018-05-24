package com.chiaradia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DataUploadException extends RuntimeException {

	private static final long serialVersionUID = 4265754171433325236L;
	
	public DataUploadException(String message) {
		super(message);
	}

}
