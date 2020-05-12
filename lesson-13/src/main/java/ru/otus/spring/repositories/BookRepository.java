package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, Long> {

    List<Book> findAllByAuthorsContaining(Author author);

    List<Book> findAllByGenresContaining(Genre genre);

}
