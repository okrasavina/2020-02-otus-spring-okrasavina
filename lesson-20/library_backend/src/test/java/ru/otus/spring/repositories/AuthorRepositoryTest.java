package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.domain.Author;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Репозиторий для работы с авторами книг должен")
@DataMongoTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @DisplayName("сохранять автора в базе данных")
    @Test
    void shouldSaveAuthor() {
        Author authorDefault = new Author("Илья Ильф", LocalDate.of(1897, 10, 15));
        Mono<Author> authorAdded = repository.save(authorDefault);

        StepVerifier.create(authorAdded)
                .assertNext(author -> {
                    assertNotNull(author.getId());
                    assertEquals(authorDefault.getName(), author.getName());
                    assertEquals(authorDefault.getBirthDay(), author.getBirthDay());
                })
                .expectComplete()
                .verify();

    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnListAllAuthor() {
        Author authorAdded = new Author("Валентин Пикуль", LocalDate.of(1928, 07, 13));
        repository.save(authorAdded).block();

        Flux<Author> authorsActual = repository.findAll();

        StepVerifier.create(authorsActual)
                .expectNextCount(repository.count().block() - 1)
                .assertNext(author -> {
                    assertNotNull(author.getId());
                    assertEquals(authorAdded.getName(), author.getName());
                    assertEquals(authorAdded.getBirthDay(), author.getBirthDay());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать автора по его имени")
    @Test
    void shouldReturnExpectedAuthorByName() {
        Author authorAdded = new Author("Александр Пушкин", LocalDate.of(1799, 06, 06));
        repository.save(authorAdded).block();

        Mono<Author> authorFound = repository.findByName(authorAdded.getName());

        StepVerifier.create(authorFound)
                .assertNext(author -> {
                    assertNotNull(author.getId());
                    assertEquals(authorAdded.getName(), author.getName());
                    assertEquals(authorAdded.getBirthDay(), author.getBirthDay());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать пустое значение, если автора не существует по заданному имени")
    @Test
    void shouldReturnNullIfAuthorIsNotExistsByName() {
        Mono<Author> errorAuthor = repository.findByName("Александр Блок");

        StepVerifier.create(errorAuthor)
                .expectComplete()
                .verify();
    }

    @DisplayName("удалять переданного автора из базы данных")
    @Test
    void shouldDeleteAuthor() {
        Author authorBeforeDelete = new Author("Виктор Гюго", LocalDate.of(1802, 02, 26));
        authorBeforeDelete = repository.save(authorBeforeDelete).block();

        repository.delete(authorBeforeDelete).block();

        StepVerifier.create(repository.findById(authorBeforeDelete.getId()))
                .expectComplete()
                .verify();
    }

}