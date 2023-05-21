package com.smu.smuenip.infrastructure.config.exception;

import com.smu.smuenip.application.login.dto.ResponseDto;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto<Void> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        e.printStackTrace();

        // Extract field names and default messages from FieldErrors
        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());

        String combinedErrorMessage = String.join(", ", errorMessages);

        return new ResponseDto<>(null, false, combinedErrorMessage);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseDto<Void> handleBadRequestException(BadRequestException e) {
        e.printStackTrace();

        return new ResponseDto<>(null, false, e.getMessage());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseDto<Void> handleUnAuthorizedException(UnAuthorizedException e) {
        e.printStackTrace();

        return new ResponseDto<>(null, false, e.getMessage());
    }

    @ExceptionHandler(UnExpectedErrorException.class)
    public ResponseDto<Void> handleUnExpectedErrorException(UnExpectedErrorException e) {
        e.printStackTrace();

        return new ResponseDto<>(null, false, e.getMessage());
    }

}
