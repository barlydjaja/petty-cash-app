package com.pettycash.exception.handler;

import com.pettycash.dto.GeneralResponse;
import com.pettycash.exception.RequestNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sun.java2d.loops.FillRect;
import sun.misc.Request;

import java.util.Date;

public class GlobalResponseExceptionHandler  extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RequestNotAllowedException.class)
    public final ResponseEntity<GeneralResponse> handleRequestNotAllowed(RequestNotAllowedException e, WebRequest webRequest){
        return new ResponseEntity<>(
                new GeneralResponse.Builder()
                        .withMessage(HttpStatus.BAD_REQUEST.toString())
                        .withPath(webRequest.getDescription(false))
                        .withResponse(e.getMessage())
                        .withDate(new Date())
                        .build(),
                HttpStatus.OK);
    }
}
