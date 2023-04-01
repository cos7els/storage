package org.cos7els.storage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

//@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDetails> notFoundExceptionHandler(
            NotFoundException exception
    ) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                HttpStatus.NOT_FOUND,
                LocalDateTime.now(),
                exception.getMessage()
        );
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadDataException.class)
    public ResponseEntity<ExceptionDetails> badDataExceptionHandler(
            BadDataException exception
    ) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                HttpStatus.CONFLICT,
                LocalDateTime.now(),
                exception.getMessage()
        );
        return new ResponseEntity<>(exceptionDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionDetails> customExceptionHandler(
            CustomException exception
    ) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                exception.getMessage()
        );
        return new ResponseEntity<>(exceptionDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
