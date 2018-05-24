package com.chiaradia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3101356914907089705L;
	
	public EntityNotFoundException(String message)
    {
        super(message);
    }

}
