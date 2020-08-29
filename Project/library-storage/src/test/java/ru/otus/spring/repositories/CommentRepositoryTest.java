package ru.otus.spring.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями книг должен")
@DataJpaTest
class CommentRepositoryTest {

   /* private Book book;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository repository;

    @BeforeEach
    void setUp() {
        Author author = entityManager.persist(new Author("Николай Гоголь", LocalDate.of(1809, 03, 19)));
        Genre genre = entityManager.persist(new Genre("Роман", "Литературный жанр"));
        book = entityManager.persist(new Book(0L, "Мертвые души", "Авантюра Чичикова",
                List.of(author), List.of(genre)));
        entityManager.flush();
    }


    @DisplayName("сохранять комментарий в базе данных")
    @Test
    void shouldSaveComment() {
        Comment commentAdded = new Comment("Очень интересная книга", book);
        commentAdded = entityManager.persist(commentAdded);
        entityManager.flush();

        Optional<Comment> commentFound = repository.findById(commentAdded.getId());

        assertThat(commentFound).isNotNull();
        assertThat(commentFound.get()).isEqualTo(commentAdded);
    }

    @DisplayName("возвращать список комментариев по переданной книге")
    @Test
    void shouldReturnExpectedListCommentsByBook() {
        Comment commentAdded = new Comment("Очень интересная книга", book);
        commentAdded = entityManager.persist(commentAdded);
        entityManager.flush();

        List<Comment> commentsActual = repository.findAllByBook(book);

        assertThat(commentsActual).isNotEmpty()
                .hasSize(1)
                .contains(commentAdded);
    }

    @DisplayName("возвращать пустое значение, если комментария не существует по заданному идентификатору")
    @Test
    void shouldReturnNullIfCommentIsNotExistsById() {
        Optional<Comment> errorComment = repository.findById(10L);

        assertThat(errorComment.isPresent()).isEqualTo(false);
    }

    @DisplayName("удалять комментарий по его идентификатору")
    @Test
    void shouldDeleteCommentById() {
        Comment commentBeforeDelete = new Comment("Книга из школьной программы", book);
        commentBeforeDelete = entityManager.persist(commentBeforeDelete);
        entityManager.flush();

        repository.deleteById(commentBeforeDelete.getId());

        Optional<Comment> commentAfterDelete = repository.findById(commentBeforeDelete.getId());

        assertThat(commentAfterDelete).isEmpty();
    }

    @DisplayName("удалять комментарии по переданной книге")
    @Test
    void shouldDeleteAllCommentsByBook() {
        entityManager.persist(new Comment("Книга из школьной программы", book));
        entityManager.persist(new Comment("Еле осилил. Местами скучно.", book));
        entityManager.flush();

        repository.deleteAllByBook(book);

        List<Comment> commentsAfterDelete = repository.findAllByBook(book);

        assertThat(commentsAfterDelete).isEmpty();
    }
*/
}