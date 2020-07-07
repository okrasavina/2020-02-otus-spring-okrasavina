package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByGenresContaining(Genre genre);

    List<Book> findAllByAuthorsContaining(Author author);

    List<Book> findAllByTitleContaining(String title);
}
