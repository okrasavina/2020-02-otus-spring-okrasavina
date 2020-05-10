package ru.otus.spring.service;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookService {
    void createBook(Book libraryBook);

    List<String> getListBookInfo();

    void deleteBookById(long bookId);

    Book getBookByNumber(long bookId);

    List<String> getListAuthor();

    List<String> getListBookInfoByAuthorName(String authorName);

    List<String> getListGenres();

    List<String> getListBookInfoByGenreName(String genreName);

    long addCommentToTheBook(Book book, String textComment);

    void clearCommentsOnTheBook(Book book);

    List<String> getListCommentsByBookId(long bookId);
}
