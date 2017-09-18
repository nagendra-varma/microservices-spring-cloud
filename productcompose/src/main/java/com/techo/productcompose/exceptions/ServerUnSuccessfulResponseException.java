package com.techo.productcompose.exceptions;


import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ServerUnSuccessfulResponseException extends RuntimeException {

    private HttpStatus httpStatus = INTERNAL_SERVER_ERROR;

    public ServerUnSuccessfulResponseException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
