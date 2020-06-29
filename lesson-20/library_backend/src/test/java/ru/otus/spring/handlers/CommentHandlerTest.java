package ru.otus.spring.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.config.LibraryRouter;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Хэндлер для работы с комментариями должен")
@SpringBootTest(classes = {CommentHandler.class, LibraryRouter.class})
class CommentHandlerTest {
    @Autowired
    private RouterFunction routerFunction;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private AuthorHandler authorHandler;
    @MockBean
    private GenreHandler genreHandler;
    @MockBean
    private BookHandler bookHandler;

    private WebTestClient webTestClient;
    private Book book;
    private Comment comment;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
        Author author = new Author("1", "Николай Гоголь", LocalDate.of(1809, 3, 19));
        Genre genre = new Genre("2", "Роман", "Литературный жанр");
        book = new Book("3", "Мертвые души", "Авантюра Чичикова",
                List.of(author), List.of(genre));
        comment = new Comment("4", "Интересная книга", book);
    }

    @DisplayName("начитать комментарий по переданному идентификатору")
    @Test
    void shouldGetExpectedCommentById() {
        when(commentRepository.findById(comment.getId())).thenReturn(Mono.just(comment));

        webTestClient.get()
                .uri("/api/comment/{id}", comment.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo(comment.getId())
                .jsonPath("$.textComment").isEqualTo(comment.getTextComment());

        verify(commentRepository, times(1)).findById(comment.getId());
    }

    @DisplayName("начитать список комментариев по переданной книге")
    @Test
    void shouldGetListCommentByBook() {
        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(commentRepository.findAllByBook(book)).thenReturn(Flux.just(comment));

        webTestClient.get()
                .uri("/api/comment/list/{bookId}", book.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(comment.getId())
                .jsonPath("$[0].textComment").isEqualTo(comment.getTextComment());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(commentRepository, times(1)).findAllByBook(book);
    }

    @DisplayName("сохранять переданный комментарий")
    @Test
    void shouldSaveComment() {
        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(commentRepository.save(comment)).thenReturn(Mono.just(comment));

        webTestClient.post()
                .uri("/api/comment/{bookId}", book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LibraryComment(comment))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo(comment.getId())
                .jsonPath("$.textComment").isEqualTo(comment.getTextComment());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(commentRepository, times(1)).save(comment);
    }

    @DisplayName("удалять комментарий по переданному идентификатору")
    @Test
    void shouldDeleteCommentById() {
        when(commentRepository.deleteById(comment.getId())).thenReturn(Mono.when(Mono.empty()));

        webTestClient.delete()
                .uri("/api/comment/{id}", comment.getId())
                .exchange()
                .expectStatus().isOk();

        verify(commentRepository, times(1)).deleteById(comment.getId());
    }
}