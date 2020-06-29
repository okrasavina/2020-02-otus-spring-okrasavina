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
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями книг должен")
@DataMongoTest
class CommentRepositoryTest {

    private Book book;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private CommentRepository repository;

    @BeforeEach
    void setUp() {
        Author author = mongoTemplate.save(new Author("Николай Гоголь", LocalDate.of(1809, 03, 19))).block();
        Genre genre = mongoTemplate.save(new Genre("Роман", "Литературный жанр")).block();
        book = mongoTemplate.save(new Book("Мертвые души", "Авантюра Чичикова",
                List.of(author), List.of(genre))).block();
    }


    @DisplayName("сохранять комментарий в базе данных")
    @Test
    void shouldSaveComment() {
        Comment commentBook = new Comment("Очень интересная книга", book);
        Mono<Comment> commentAdded = repository.save(commentBook);

        StepVerifier.create(commentAdded)
                .assertNext(comment -> {
                    assertNotNull(comment.getId());
                    assertEquals(comment.getTextComment(), commentBook.getTextComment());
                    assertEquals(comment.getBook(), commentBook.getBook());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать список комментариев по переданной книге")
    @Test
    void shouldReturnExpectedListCommentsByBook() {
        Comment commentBook = new Comment("Стоит почитать", book);
        repository.save(commentBook).block();

        Flux<Comment> commentsActual = repository.findAllByBook(book);

        StepVerifier.create(commentsActual)
                .expectNextCount(repository.count().block() - 1)
                .assertNext(comment -> {
                    assertNotNull(comment.getId());
                    assertEquals(comment.getTextComment(), commentBook.getTextComment());
                    assertEquals(comment.getBook(), commentBook.getBook());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("возвращать пустое значение, если комментария не существует по заданному идентификатору")
    @Test
    void shouldReturnNullIfCommentIsNotExistsById() {
        Mono<Comment> errorComment = repository.findById("1");

        StepVerifier.create(errorComment)
                .expectComplete()
                .verify();
    }

    @DisplayName("удалять комментарий по его идентификатору")
    @Test
    void shouldDeleteCommentById() {
        Comment commentBeforeDelete = repository.save(new Comment("Книга из школьной программы", book)).block();

        repository.deleteById(commentBeforeDelete.getId()).block();

        StepVerifier.create(repository.findById(commentBeforeDelete.getId()))
                .expectComplete()
                .verify();
    }

    @DisplayName("удалять комментарии по переданной книге")
    @Test
    void shouldDeleteAllCommentsByBook() {
        repository.save(new Comment("Еле осилил. Местами скучно.", book)).block();

        repository.deleteAllByBook(book).block();

        StepVerifier.create(repository.findAllByBook(book))
                .expectComplete()
                .verify();
    }

}