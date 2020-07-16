package ru.otus.spring.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.library.domain.jpa.Author;
import ru.otus.spring.library.domain.jpa.Book;
import ru.otus.spring.library.domain.jpa.Comment;
import ru.otus.spring.library.domain.jpa.Genre;
import ru.otus.spring.library.domain.mongo.AuthorMongo;
import ru.otus.spring.library.domain.mongo.BookMongo;
import ru.otus.spring.library.domain.mongo.CommentMongo;
import ru.otus.spring.library.domain.mongo.GenreMongo;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("Сервис для перевода сущностей из Jpa в Mongo должен")
class DefaultJpaToMongoTransformServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    private DefaultJpaToMongoTransformService service;

    @BeforeEach
    void setUp() {
        service = new DefaultJpaToMongoTransformService(mongoTemplate);
    }

    @DisplayName("переводить автора из объекта Jpa в объект для Mongo")
    @Test
    void shouldReturnExpectedAuthorMongoByAuthor() {
        AuthorMongo expectedAuthor = mongoTemplate.save(new AuthorMongo("Александр Сергеевич Пушкин",
                LocalDate.of(1799, 6, 6)));
        AuthorMongo actualAuthor = service.transformAuthorToAuthorMongo(new Author(1L, expectedAuthor.getName(),
                expectedAuthor.getBirthDay()));

        assertThat(actualAuthor).isEqualTo(expectedAuthor);
    }

    @DisplayName("переводить жанр из объекта Jpa в объект для Mongo")
    @Test
    void shouldReturnExpectedGenreMongoByGenre() {
        GenreMongo expectedGenre = new GenreMongo("Трагедия", "Литературный жанр");
        GenreMongo actualGenre = service.transformGenreToGenreMongo(new Genre(1L, expectedGenre.getName(),
                expectedGenre.getDescription()));

        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

    @DisplayName("переводить книгу из объекта Jpa в объект для Mongo")
    @Test
    void shouldReturnExpectedBookMongoByBook() {
        AuthorMongo authorMongo = mongoTemplate.save(new AuthorMongo("Сергей Есенин",
                LocalDate.of(1895, 10, 3)));
        GenreMongo genreMongo = mongoTemplate.save(new GenreMongo("Лирическая поэма", "Литературный жанр"));
        BookMongo expectedBook = mongoTemplate.save(new BookMongo(1L, "Черный человек", "Фантастические истории",
                List.of(authorMongo), List.of(genreMongo)));

        BookMongo actualBook = service.transformBookToBookMongo(new Book(expectedBook.getJpaId(), expectedBook.getTitle(),
                expectedBook.getDescription(), List.of(new Author(2L, authorMongo.getName(), authorMongo.getBirthDay())),
                List.of(new Genre(2L, genreMongo.getName(), genreMongo.getDescription()))));

        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("переводить комментарий из объекта Jpa в объект для Mongo")
    @Test
    void shouldReturnExpectedCommentMongoByComment() {
        AuthorMongo authorMongo = mongoTemplate.save(new AuthorMongo("Николай Гоголь",
                LocalDate.of(1809, 03, 19)));
        GenreMongo genreMongo = mongoTemplate.save(new GenreMongo("Фантастика", "Литературный жанр"));
        BookMongo bookMongo = mongoTemplate.save(new BookMongo(2L, "Вечера на хуторе близ Диканьки", "Фантастические истории",
                List.of(authorMongo), List.of(genreMongo)));
        CommentMongo expectedComment = mongoTemplate.save(new CommentMongo(1L, "Увлекательная книга", bookMongo));

        CommentMongo actualComment = service.transformCommentToCommentMongo(new Comment(expectedComment.getJpaId(), expectedComment.getTextComment(),
                new Book(bookMongo.getJpaId(), bookMongo.getTitle(), bookMongo.getDescription(),
                        List.of(new Author(3L, authorMongo.getName(), authorMongo.getBirthDay())),
                        List.of(new Genre(3L, genreMongo.getName(), genreMongo.getDescription())))));

        assertThat(actualComment).isEqualTo(expectedComment);
    }
}