package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.LibraryBook;

import java.util.List;

public interface BookService {
    List<LibraryBook> getListBook();

    LibraryBook getBookById(Long id);

    LibraryBook saveBook(LibraryBook libraryBook);

    void deleteBook(LibraryBook libraryBook);

    Book toDomain(LibraryBook libraryBook);

    LibraryBook toDto(Book book);
}
