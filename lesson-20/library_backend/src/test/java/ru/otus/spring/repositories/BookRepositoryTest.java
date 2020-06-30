package ru.otus.spring.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Репозиторий для работы с книгами должен")
@DataMongoTest
class BookRepositoryTest {

    private Author author;
    private Genre genre;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private BookRepository repository;

    @BeforeEach
    void setUp() {
        author = mongoTemplate.save(new Author("Николай Гоголь", LocalDate.of(1809, 03, 19))).block();
        genre = mongoTemplate.save(new Genre("Роман", "Литературный жанр")).block();
    }

    @DisplayName("сохранять книгу в базе данных")
    @Test
    void shouldSaveBook() {
        Book bookAdded = getNewBook();
        Mono<Book> bookMono = repository.save(bookAdded);

        StepVerifier.create(bookMono)
                .assertNext(book -> {
                    assertNotNull(book.getId());
                    assertEquals(bookAdded.getTitle(), book.getTitle());
                    assertEquals(bookAdded.getDescription(), book.getDescription());
                    assertEquals(bookAdded.getAuthors(), book.getAuthors());
                    assertEquals(bookAdded.getGenres(), book.getGenres());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать пустое значение, если книги не существует по заданному идентификатору")
    @Test
    void shouldReturnNullIfBookIsNotExistsById() {
        Mono<Book> errorBook = repository.findById("1");

        StepVerifier.create(errorBook)
                .expectComplete()
                .verify();
    }

    @DisplayName("удалять книгу по переданному идентификатору")
    @Test
    void shouldDeleteBookById() {
        Book bookBeforeDelete = repository.save(getNewBook()).block();

        repository.deleteById(bookBeforeDelete.getId()).block();

        StepVerifier.create(repository.findById(bookBeforeDelete.getId()))
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать весь список книг из базы данных")
    @Test
    void shouldReturnExpectedListOfBooks() {
        Book bookAdded = repository.save(getNewBook()).block();

        Flux<Book> booksActual = repository.findAll();

        StepVerifier.create(booksActual)
                .expectNextCount(repository.count().block() - 1)
                .assertNext(book -> {
                    assertNotNull(book.getId());
                    assertEquals(bookAdded.getTitle(), book.getTitle());
                    assertEquals(bookAdded.getDescription(), book.getDescription());
                    assertEquals(bookAdded.getAuthors(), book.getAuthors());
                    assertEquals(bookAdded.getGenres(), book.getGenres());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать список книг по переданному жанру")
    @Test
    void shouldReturnExpectedListOfBooksByGenre() {
        Book bookAdded = repository.save(getNewBook()).block();

        Flux<Book> booksActual = repository.findAllByGenresContaining(genre);

        StepVerifier.create(booksActual)
                .assertNext(book -> {
                    assertNotNull(book.getId());
                    assertEquals(bookAdded.getTitle(), book.getTitle());
                    assertEquals(bookAdded.getDescription(), book.getDescription());
                    assertEquals(bookAdded.getAuthors(), book.getAuthors());
                    assertEquals(bookAdded.getGenres(), book.getGenres());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать список книг по переданному автору")
    @Test
    void shouldReturnExpectedListOfBooksBtAuthor() {
        Book bookAdded = repository.save(getNewBook()).block();

        Flux<Book> booksActual = repository.findAllByAuthorsContaining(author);

        StepVerifier.create(booksActual)
                .assertNext(book -> {
                    assertNotNull(book.getId());
                    assertEquals(bookAdded.getTitle(), book.getTitle());
                    assertEquals(bookAdded.getDescription(), book.getDescription());
                    assertEquals(bookAdded.getAuthors(), book.getAuthors());
                    assertEquals(bookAdded.getGenres(), book.getGenres());
                })
                .expectComplete()
                .verify();
    }

    private Book getNewBook() {
        return new Book("Вечера на Хуторе близ Диканьки", "Фантастические истории",
                List.of(author), List.of(genre));
    }

}