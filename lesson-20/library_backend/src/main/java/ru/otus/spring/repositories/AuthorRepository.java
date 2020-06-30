package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    Mono<Author> findByName(String name);
}
