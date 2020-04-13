package ru.otus.spring.repositories;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.LibraryBook;

import java.util.List;
import java.util.Optional;

public interface LibraryBookRepositoryJpa {

    LibraryBook save(LibraryBook book);

    List<LibraryBook> getAll();

    List<LibraryBook> getAllByAuthor(Author author);

    List<LibraryBook> getAllByGenre(Genre genre);

    void deleteById(long bookId);

    Optional<LibraryBook> getById(long bookId);
}
