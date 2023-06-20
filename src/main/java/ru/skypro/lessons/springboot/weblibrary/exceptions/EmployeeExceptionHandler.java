package ru.skypro.lessons.springboot.weblibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
public class EmployeeExceptionHandler {

    @ExceptionHandler(value = {SQLException.class, IOException.class, IllegalJsonFileException.class})
    public ResponseEntity<?> handleException (Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ReportNotFoundException.class, IncorrectEmployeeIdException.class})
    public ResponseEntity<?> notFound (Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
