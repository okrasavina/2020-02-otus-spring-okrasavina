package ru.otus.spring.dto;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String authorName) {
        super(String.format("Author not found by name %s", authorName));
    }
}
