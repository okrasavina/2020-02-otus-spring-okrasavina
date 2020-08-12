package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.GenreExcerpt;

import java.util.Optional;

@RepositoryRestResource(path = "genre", excerptProjection = GenreExcerpt.class)
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
