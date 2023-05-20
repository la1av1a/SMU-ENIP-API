package com.smu.smuenip.infrastructure.config.exception;

import lombok.Getter;

@Getter
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String message) {
        super(message);
    }
}
