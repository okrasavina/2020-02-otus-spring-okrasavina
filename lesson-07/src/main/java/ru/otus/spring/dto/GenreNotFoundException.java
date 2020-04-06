package ru.otus.spring.dto;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String genreName) {
        super(String.format("Genre not found by name %s", genreName));
    }
}
