package ru.otus.spring.service;

import java.util.List;

public interface LibraryService {

    String createLibraryBook(String bookTitle, List<String> authorNames, List<String> genreNames);

    List<String> getListBook();

    String deleteBookByNumber(long bookId);

    String getBookByNumber(long bookNumber);

    List<String> getListAuthor();

    List<String> getListBookByAuthorName(String authorName);

    List<String> getListGenres();

    List<String> getListBookByGenreName(String genreName);
}
