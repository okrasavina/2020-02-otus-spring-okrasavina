package ru.otus.spring.repositories;

import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepositoryJpa {
    List<Genre> getAll();

    Optional<Genre> getByName(String genreName);

    void deleteGenreWithoutBooks();

    List<Genre> findListByListName(List<String> names);
}
