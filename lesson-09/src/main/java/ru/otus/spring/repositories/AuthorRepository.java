package ru.otus.spring.repositories;

import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> getAll();

    Optional<Author> getByName(String authorName);

    void deleteAuthorWithoutBooks();

    List<Author> findListByListName(List<String> names);
}
