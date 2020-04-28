package ru.otus.spring.dto;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class objClass, String paramName, String paramValue) {
        super(String.format("%s not found by %s %s", objClass.getSimpleName(), paramName, paramValue));
    }
}
