package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryGenre;

import java.util.List;

public interface BookService {
    List<LibraryBook> getListBooksByGenreName(String genreName);

    List<LibraryBook> getListBook();

    LibraryBook getBookById(Long id);

    LibraryBook saveBook(LibraryBook libraryBook);

    List<LibraryBook> getListBooksByAuthorName(String authorName);

    List<LibraryBook> getListBooksByTitle(String title);

    void deleteBook(LibraryBook libraryBook);

    Book toDomain(LibraryBook libraryBook);

    LibraryBook toDto(Book book);
}
