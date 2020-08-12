package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.AuthorExcerpt;

import java.util.Optional;

@RepositoryRestResource(path = "author", excerptProjection = AuthorExcerpt.class)
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
}
