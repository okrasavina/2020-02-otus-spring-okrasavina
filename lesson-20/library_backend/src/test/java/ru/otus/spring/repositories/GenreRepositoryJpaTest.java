package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.domain.Genre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Репозиторий для работы с жанрами книг должен")
@DataMongoTest
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("сохранять жанр в базе данных")
    @Test
    void shouldSaveGenre() {
        Genre genreAdded = getDefaultGenre();
        Mono<Genre> genreMono = repository.save(genreAdded);

        StepVerifier.create(genreMono)
                .assertNext(genre -> {
                    assertNotNull(genre.getId());
                    assertEquals(genreAdded.getName(), genre.getName());
                    assertEquals(genreAdded.getDescription(), genre.getDescription());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать список всех жанров")
    @Test
    void shouldReturnListAllGenre() {
        Genre genreAdded = repository.save(getDefaultGenre()).block();

        Flux<Genre> genres = repository.findAll();

        StepVerifier.create(genres)
                .expectNextCount(repository.count().block() - 1)
                .assertNext(genre -> {
                    assertNotNull(genre.getId());
                    assertEquals(genreAdded.getName(), genre.getName());
                    assertEquals(genreAdded.getDescription(), genre.getDescription());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать жанр по его имени")
    @Test
    void shouldReturnExpectedGenreByName() {
        Genre genreAdded = repository.save(getDefaultGenre()).block();

        Mono<Genre> genreMono = repository.findByName(genreAdded.getName());

        StepVerifier.create(genreMono)
                .assertNext(genre -> {
                    assertNotNull(genre.getId());
                    assertEquals(genreAdded.getName(), genre.getName());
                    assertEquals(genreAdded.getDescription(), genre.getDescription());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать пустое значение, если жанра не существует по заданному имени")
    @Test
    void shouldReturnNullIfGenreIsNotExistsByName() {
        Mono<Genre> errorGenre = repository.findByName("Сатира");

        StepVerifier.create(errorGenre)
                .expectComplete()
                .verify();
    }

    @DisplayName("удалять переданный жанр из базы данных")
    @Test
    void shouldDeleteGenre() {
        Genre genreBeforeDelete = repository.save(getDefaultGenre()).block();

        repository.delete(genreBeforeDelete).block();

        StepVerifier.create(repository.findById(genreBeforeDelete.getId()))
                .expectComplete()
                .verify();
    }

    private Genre getDefaultGenre() {
        return new Genre("Комедия", "Литературный жанр");
    }
}