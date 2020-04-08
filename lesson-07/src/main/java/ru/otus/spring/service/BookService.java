package ru.otus.spring.service;

import ru.otus.spring.dto.LibraryBook;

import java.util.List;

public interface BookService {
    void createBook(LibraryBook libraryBook, List<String> authorNames, List<String> genreNames);

    List<LibraryBook> getListBook();

    void deleteBookById(long bookId);

    String getBookByNumber(long bookId);

    List<String> getListAuthor();

    List<LibraryBook> getListBookByAuthorName(String authorName);

    List<String> getListGenres();

    List<LibraryBook> getListBookByGenreName(String genreName);
}
