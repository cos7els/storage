package org.cos7els.storage.exception;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseBody
public class ExceptionControllerAdvice {

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<HttpStatus> noContentExceptionHandler() {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDetails> notFoundExceptionHandler(
            NotFoundException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ExceptionDetails(
                                HttpStatus.NOT_FOUND,
                                LocalDateTime.now(),
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(BadDataException.class)
    public ResponseEntity<ExceptionDetails> badDataExceptionHandler(
            BadDataException exception
    ) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.CONFLICT,
                LocalDateTime.now(),
                exception.getMessage()
        );
        return new ResponseEntity<>(details, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ExceptionDetails> customExceptionHandler(
            InternalException exception
    ) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                exception.getMessage()
        );
        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDetails> badCredentialsExceptionHandler(
            BadCredentialsException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ExceptionDetails(
                                HttpStatus.UNAUTHORIZED,
                                LocalDateTime.now(),
                                exception.getMessage()
                        )
                );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> exceptionHandler(Exception exception) {
        ExceptionDetails details = new ExceptionDetails(
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                exception.getMessage()
        );
        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ExceptionDetails> DbExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ExceptionDetails(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                LocalDateTime.now(),
                                exception.getMessage()
                        )
                );
    }
}