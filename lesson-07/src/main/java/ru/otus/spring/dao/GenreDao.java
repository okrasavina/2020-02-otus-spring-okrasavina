package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> getByName(String genreName);

    Genre insert(String genreName);

    List<Genre> getListByBookId(long bookId);

    void deleteGenresWithoutBooks();

    List<Genre> getAll();
}
