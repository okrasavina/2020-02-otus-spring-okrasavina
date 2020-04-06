package ru.otus.spring.dto;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long bookId) {
        super(String.format("Book not found by number %d", bookId));
    }
}
