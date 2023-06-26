package ru.skypro.lessons.springboot.weblibrary.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
public class EmployeeExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeExceptionHandler.class);

    @ExceptionHandler(value = {SQLException.class, IOException.class, IllegalJsonFileException.class})
    public ResponseEntity<?> handleException (Exception exception) {
        LOG.error(exception.getMessage(), exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ReportNotFoundException.class, IncorrectEmployeeIdException.class})
    public ResponseEntity<?> notFound (Exception exception) {
        LOG.error(exception.getMessage(), exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
