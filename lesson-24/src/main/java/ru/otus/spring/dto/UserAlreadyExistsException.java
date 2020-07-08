package ru.otus.spring.dto;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Пользователь с таким именем уже существует!");
    }
}
