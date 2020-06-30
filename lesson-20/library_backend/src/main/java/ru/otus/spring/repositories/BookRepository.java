package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findAllByGenresContaining(Genre genre);

    Flux<Book> findAllByAuthorsContaining(Author author);
}
