package ru.skypro.lessons.springboot.weblibrary.exceptions;

public class IncorrectEmployeeIdException extends RuntimeException {
    public IncorrectEmployeeIdException(Integer id) {
        throw new IncorrectEmployeeIdException(id);
    }
}
