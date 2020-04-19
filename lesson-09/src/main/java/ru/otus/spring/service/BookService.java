package ru.otus.spring.service;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookService {
    void createBook(Book libraryBook);

    List<Book> getListBook();

    void deleteBookById(long bookId);

    Book getBookByNumber(long bookId);

    List<String> getListAuthor();

    List<Book> getListBookByAuthorName(String authorName);

    List<String> getListGenres();

    List<Book> getListBookByGenreName(String genreName);

    long addCommentToTheBook(Book book, String textComment);

    void clearCommentsOnTheBook(Book book);
}
