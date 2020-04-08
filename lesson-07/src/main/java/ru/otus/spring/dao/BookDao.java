package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book insert(Book book, List<Author> authors, List<Genre> genres);

    List<Book> getAll();

    void deleteById(long id);

    Optional<Book> getById(long id);

    List<Book> getAllByAuthorId(long authorId);

    List<Book> getAllByGenreId(long genreId);
}
