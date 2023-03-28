package com.smu.smuenip.Infrastructure.config.exception;


public class UnAuthorizedException extends RuntimeException {
    
    public UnAuthorizedException(String message) {
        super(message);
    }
}
