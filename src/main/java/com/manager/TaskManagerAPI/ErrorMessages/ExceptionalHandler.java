package com.manager.TaskManagerAPI.ErrorMessages;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

/**
 * this class handles exceptions
 * @author Mohammad Rayyan Adhoni
 */
//@ControllerAdvice // makes this an exception handling class
public class ExceptionalHandler {

    /**
     * this handles NoSuchElementException calls
     * @param e message from the exception
     * @param request HTTP request data
     * @return Response Entity of 404 Not Found error
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomErrorMessages> handleNoSuchElementException(NoSuchElementException e, WebRequest request) {
        CustomErrorMessages err = new CustomErrorMessages(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getDescription(false).replace("url=",""));
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    /**
     * @param e message from the exception
     * @param request HTTP request data
     * @return Response Entity of 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorMessages> handleException(Exception e, WebRequest request) {
        CustomErrorMessages err = new CustomErrorMessages(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false).replace("url=","")
        );
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles Invalid parameters fro creation or updating a task
     * @param e message from the exception
     * @param request HTTP request data
     * @return Response Entity with Bad Request Error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorMessages> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        StringBuilder errorMessage = new StringBuilder("Invalid validation: ");
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMessage.append(fieldError.getField())
                    .append(" : ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }
        CustomErrorMessages err = new CustomErrorMessages(
                errorMessage.toString(),
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false).replace("url=","")
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
