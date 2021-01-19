package com.pettycash.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestNotAllowedException extends RuntimeException {
    public RequestNotAllowedException(String exception) {
        super(exception);
    }
}
