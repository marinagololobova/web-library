package ru.skypro.lessons.springboot.weblibrary.exceptions;

public class IncorrectEmployeeIdException extends RuntimeException {
    public IncorrectEmployeeIdException(Integer id) {
        super("Работник с таким id: " + id + " не найден");
    }
}
