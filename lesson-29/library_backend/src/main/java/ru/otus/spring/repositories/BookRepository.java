package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.spring.domain.Book;

//@RepositoryRestResource(path = "/api/book")
public interface BookRepository extends JpaRepository<Book, Long> {
}
