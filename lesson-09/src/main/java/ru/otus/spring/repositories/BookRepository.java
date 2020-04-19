package ru.otus.spring.repositories;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    List<Book> getAll();

    List<Book> getAllByAuthor(Author author);

    List<Book> getAllByGenre(Genre genre);

    void deleteById(long bookId);

    Optional<Book> getById(long bookId);
}
