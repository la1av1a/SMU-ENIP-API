package com.smu.smuenip.infrastructure.config.exception;

import com.smu.smuenip.application.login.dto.ResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationExceptions(
        MethodArgumentNotValidException e) {
        e.printStackTrace();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Extract field names and default messages from FieldErrors
        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + " : " + error.getDefaultMessage())
            .collect(Collectors.toList());

        String combinedErrorMessage = String.join(", ", errorMessages);

        ResponseDto responseDto = new ResponseDto(false, combinedErrorMessage);

        return new ResponseEntity<>(responseDto, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException e) {
        e.printStackTrace();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseDto responseDto = new ResponseDto(false, e.getMessage());

        return new ResponseEntity<>(responseDto, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ResponseDto> handleUnAuthorizedException(UnAuthorizedException e) {
        e.printStackTrace();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseDto responseDto = new ResponseDto(false, e.getMessage());

        return new ResponseEntity<>(responseDto, responseHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnExpectedErrorException.class)
    public ResponseEntity<ResponseDto> handleUnExpectedErrorException(UnExpectedErrorException e) {
        e.printStackTrace();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseDto responseDto = new ResponseDto(false, e.getMessage());

        return new ResponseEntity<>(responseDto, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
