package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.LibraryBook;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями книг должен")
@DataJpaTest
@Import(CommentRepositoryJpaImpl.class)
class CommentRepositoryJpaImplTest {

    public static final String FIRST_AUTHOR_NAME = "Илья Ильф";
    public static final long FIRST_AUTHOR_ID = 1L;
    public static final long SECOND_AUTHOR_ID = 2L;
    public static final String SECOND_AUTHOR_NAME = "Евгений Петров";
    public static final long DEFAULT_GENRE_ID = 1L;
    public static final String DEFAULT_GENRE_NAME = "Комедия";
    public static final long DEFAULT_BOOK_ID = 1L;
    public static final String DEFAULT_BOOK_NAME = "12 стульев";
    public static final String INSERTED_COMMENT_TEXT = "Это интересная книга.";

    @Autowired
    private CommentRepositoryJpaImpl repo;

    @Autowired
    private TestEntityManager em;

    @DisplayName("сохранять комментарий по книге в БД")
    @Test
    void shouldSaveCommentOnTheBook() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        LibraryBook book = new LibraryBook(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of());

        Comment comment = new Comment(0, INSERTED_COMMENT_TEXT, book);
        repo.save(comment);

        assertThat(comment.getId()).isGreaterThan(0);

        Comment commentActual = em.find(Comment.class, comment.getId());
        assertThat(commentActual).isNotNull().isEqualToComparingFieldByField(comment);
    }

    @DisplayName("удалять комментарии у книги")
    @Test
    void shouldDeleteCommentsByTheBook() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        LibraryBook book = new LibraryBook(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of());

        Comment comment = new Comment(0, INSERTED_COMMENT_TEXT, book);
        repo.save(comment);

        repo.deleteCommentsByBook(book);
        LibraryBook bookAfterDelete = em.find(LibraryBook.class, DEFAULT_BOOK_ID);
        assertThat(bookAfterDelete).isNotNull();
        assertThat(bookAfterDelete.getComments()).isEmpty();
    }

}