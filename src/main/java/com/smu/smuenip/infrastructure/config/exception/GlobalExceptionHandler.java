package com.smu.smuenip.infrastructure.config.exception;

import com.smu.smuenip.application.login.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Void>> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        e.printStackTrace();

        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());

        String combinedErrorMessage = String.join(", ", errorMessages);

        return new ResponseEntity<>(new ResponseDto<>(null, combinedErrorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto<Void>> handleBadRequestException(BadRequestException e) {
        e.printStackTrace();

        return new ResponseEntity<>(new ResponseDto<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ResponseDto<Void>> handleUnAuthorizedException(UnAuthorizedException e) {
        e.printStackTrace();

        return new ResponseEntity<>(new ResponseDto<>(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnExpectedErrorException.class)
    public ResponseEntity<ResponseDto<Void>> handleUnExpectedErrorException(UnExpectedErrorException e) {
        e.printStackTrace();

        return new ResponseEntity<>(new ResponseDto<>(null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
