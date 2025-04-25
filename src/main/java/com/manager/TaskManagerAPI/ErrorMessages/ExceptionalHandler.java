package com.manager.TaskManagerAPI.ErrorMessages;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

/**
 * thi s class handles exceptions
 */
@ControllerAdvice // makes this an exception handling class
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
     *
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
}
