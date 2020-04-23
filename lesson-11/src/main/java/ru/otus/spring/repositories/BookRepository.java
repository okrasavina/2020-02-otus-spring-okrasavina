package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional(readOnly = true)
    List<Book> findAllByAuthorsContaining(Author author);

    @Transactional(readOnly = true)
    List<Book> findAllByGenresContaining(Genre genre);

}
