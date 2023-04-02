package com.smu.smuenip.Infrastructure.config.exception;

import com.smu.smuenip.application.auth.dto.ResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseDto responseDto = new ResponseDto(false, e.getMessage());

        return new ResponseEntity<>(responseDto, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ResponseDto> handleUnAuthorizedException(UnAuthorizedException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseDto responseDto = new ResponseDto(false, e.getMessage());

        return new ResponseEntity<>(responseDto, responseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnExpectedErrorException.class)
    public ResponseEntity<ResponseDto> handleUnExpectedErrorException(UnExpectedErrorException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseDto responseDto = new ResponseDto(false, e.getMessage());

        return new ResponseEntity<>(responseDto, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
