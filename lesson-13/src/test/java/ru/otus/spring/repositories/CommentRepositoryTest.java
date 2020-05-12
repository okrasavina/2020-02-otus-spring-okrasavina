package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.spring.config", "ru.otus.spring.repositories"})
@DisplayName("Репозиторий на основе Jpa для работы с комментариями книг должен")
class CommentRepositoryTest {

    public static final String DEFAULT_BOOK_NAME = "Золотой теленок";
    public static final String INSERTED_COMMENT_TEXT = "Всем советую прочитать.";
    public static final int EXPECTED_COMMENTS_COUNT = 3;

    @Autowired
    private CommentRepository repo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("сохранять комментарий по книге в БД")
    @Test
    void shouldSaveCommentOnTheBook() {

        Comment comment = new Comment(INSERTED_COMMENT_TEXT, getDefaultBook());
        comment = repo.save(comment);

        assertThat(comment.getId()).isNotEmpty();

        Comment commentExpected = mongoTemplate.findOne(query(where("textComment").is(INSERTED_COMMENT_TEXT))
                .addCriteria(where("book").is(getDefaultBook())), Comment.class);
        assertThat(comment).isNotNull().isEqualToComparingFieldByField(commentExpected);
    }

    @DisplayName("удалять комментарии по книге")
    @Test
    void shouldDeleteCommentsByTheBook() {
        List<Comment> commentsBeforeDelete = mongoTemplate.find(query(where("book").is(getDefaultBook())), Comment.class);
        repo.deleteAllByBook(getDefaultBook());
        List<Comment> commentsAfterDelete = mongoTemplate.find(query(where("book").is(getDefaultBook())), Comment.class);

        assertThat(commentsBeforeDelete).isNotEmpty().hasSize(EXPECTED_COMMENTS_COUNT);
        assertThat(commentsAfterDelete).isEmpty();
    }

    private Book getDefaultBook() {
        Book book = mongoTemplate.findOne(query(where("title").is(DEFAULT_BOOK_NAME)), Book.class);
        return book;
    }
}