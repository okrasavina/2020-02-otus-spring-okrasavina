package ru.otus.spring.service;

import ru.otus.spring.domain.LibraryBook;

import java.util.List;

public interface BookService {
    void createBook(LibraryBook libraryBook);

    List<LibraryBook> getListBook();

    void deleteBookById(long bookId);

    LibraryBook getBookByNumber(long bookId);

    List<String> getListAuthor();

    List<LibraryBook> getListBookByAuthorName(String authorName);

    List<String> getListGenres();

    List<LibraryBook> getListBookByGenreName(String genreName);

    long addCommentToTheBook(LibraryBook book, String textComment);

    void clearCommentsOnTheBook(LibraryBook book);
}
