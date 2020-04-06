package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    Optional<Author> getByName(String authorName);

    Author insert(String authorName);

    List<Author> getListByBookId(long bookId);

    List<Author> getAll();

    void deleteAuthorsWithoutBooks();
}
