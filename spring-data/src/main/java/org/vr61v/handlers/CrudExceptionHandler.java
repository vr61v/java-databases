package org.vr61v.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CrudExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return "Invalid request: " + ex.getMessage();
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException) ex).getBindingResult()
                    .getAllErrors()
                    .forEach(error -> {
                        String fieldName = error instanceof FieldError
                                ? ((FieldError) error).getField()
                                : error.getObjectName();
                        errors.put(fieldName, error.getDefaultMessage());
                    });
            log.warn("Validation failed for {} fields", errors.size());
        }
        else if (ex instanceof HandlerMethodValidationException) {
            ((HandlerMethodValidationException) ex).getAllErrors()
                    .forEach(error -> {
                        String fieldName = ((FieldError) error).getField();
                        errors.put(fieldName, error.getDefaultMessage());
                    });
            log.warn("Parameter validation failed for {} parameters", errors.size());
        }

        return errors;
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleSQLExceptions(DataAccessException ex) {
        log.error("Database error: {}", ex.getMessage(), ex);

        String message = "Database error";
        if (ex instanceof ObjectOptimisticLockingFailureException) {
            message = "Data constraint violation";
        } else if (ex instanceof DataIntegrityViolationException) {
            message = "Invalid data";
        }

        return message;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Http message not readable: {}", ex.getMessage(), ex);
        return "Invalid request: check your request body";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnknownException(Exception ex) {
        log.error("Unexpected error [{}]: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return "Internal server error. Please contact support.";
    }

}

