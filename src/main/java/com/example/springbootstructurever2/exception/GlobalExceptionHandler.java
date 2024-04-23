package com.example.springbootstructurever2.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception exception, WebRequest request) {
        log.info("========================> handleValidationException");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        String message = exception.getMessage();
        if (exception instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf("[");
            int end = message.lastIndexOf("]");
            message = message.substring(start + 1, end - 1);
            errorResponse.setError("Payload invalid");
        } else if (exception instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(" ") + 1);
            errorResponse.setError("Path variable invalid");
        }
        errorResponse.setMessage(message);

        return errorResponse;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInternalServerException(Exception exception, WebRequest request) {
        log.info("========================> handleInternalServerException");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        if (exception instanceof MethodArgumentTypeMismatchException) {
            errorResponse.setMessage("Failed to convert value of type");
        }

        return errorResponse;
    }
}
