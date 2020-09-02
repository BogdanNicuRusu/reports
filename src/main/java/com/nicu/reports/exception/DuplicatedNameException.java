package com.nicu.reports.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(BAD_REQUEST)
public class DuplicatedNameException extends RuntimeException {

    public DuplicatedNameException(String message) {
        super(message);
    }
}
