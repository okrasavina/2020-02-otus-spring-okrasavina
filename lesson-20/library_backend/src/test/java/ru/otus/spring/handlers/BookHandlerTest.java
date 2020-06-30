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
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Хэндлер для работы с книгами должен")
@SpringBootTest(classes = {BookHandler.class, LibraryRouter.class})
class BookHandlerTest {

    @Autowired
    private RouterFunction routerFunction;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private AuthorHandler authorHandler;
    @MockBean
    private GenreHandler genreHandler;
    @MockBean
    private CommentHandler commentHandler;

    private WebTestClient webTestClient;
    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
        author = new Author("1", "Николай Гоголь", LocalDate.of(1809, 3, 19));
        genre = new Genre("2", "Роман", "Литературный жанр");
        book = new Book("3", "Мертвые души", "Авантюра Чичикова",
                List.of(author), List.of(genre));
    }

    @DisplayName("начитать книгу по переданному идентификатору")
    @Test
    void shouldGetExpectedBookById() {
        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));

        webTestClient.get()
                .uri("/api/book/{id}", book.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo(book.getId())
                .jsonPath("$.title").isEqualTo(book.getTitle())
                .jsonPath("$.description").isEqualTo(book.getDescription())
                .jsonPath("$.authors").isEqualTo(author.getName())
                .jsonPath("$.genres").isEqualTo(genre.getName());

        verify(bookRepository, times(1)).findById(book.getId());
    }

    @DisplayName("начитать список книг")
    @Test
    void shouldGetExpectedListBooks() {
        when(bookRepository.findAll()).thenReturn(Flux.just(book));

        webTestClient.get()
                .uri("/api/book")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(book.getId())
                .jsonPath("$[0].title").isEqualTo(book.getTitle())
                .jsonPath("$[0].description").isEqualTo(book.getDescription())
                .jsonPath("$[0].authors").isEqualTo(author.getName())
                .jsonPath("$[0].genres").isEqualTo(genre.getName());

        verify(bookRepository, times(1)).findAll();
    }

    @DisplayName("сохранять переданную книгу")
    @Test
    void shouldSaveBook() {
        when(authorRepository.findByName(author.getName())).thenReturn(Mono.just(author));
        when(authorRepository.save(author)).thenReturn(Mono.just(author));
        when(genreRepository.findByName(genre.getName())).thenReturn(Mono.just(genre));
        when(genreRepository.save(genre)).thenReturn(Mono.just(genre));
        when(bookRepository.save(book)).thenReturn(Mono.just(book));

        webTestClient.post()
                .uri("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(LibraryBook.toDto(book))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo(book.getId())
                .jsonPath("$.title").isEqualTo(book.getTitle())
                .jsonPath("$.description").isEqualTo(book.getDescription())
                .jsonPath("$.authors").isEqualTo(author.getName())
                .jsonPath("$.genres").isEqualTo(genre.getName());

        verify(authorRepository, times(1)).findByName(author.getName());
        verify(authorRepository, times(1)).save(author);
        verify(genreRepository, times(1)).findByName(genre.getName());
        verify(genreRepository, times(1)).save(genre);
        verify(bookRepository, times(1)).save(book);
    }

    @DisplayName("удалять книгу по переданному идентификатору")
    @Test
    void shouldDeleteBookById() {
        when(bookRepository.findById(book.getId())).thenReturn(Mono.just(book));
        when(commentRepository.deleteAllByBook(book)).thenReturn(Mono.when(Mono.empty()));
        when(bookRepository.delete(book)).thenReturn(Mono.when(Mono.empty()));

        webTestClient.delete()
                .uri("/api/book/{id}", book.getId())
                .exchange()
                .expectStatus().isOk();

        verify(bookRepository, times(1)).findById(book.getId());
        verify(commentRepository, times(1)).deleteAllByBook(book);
        verify(bookRepository, times(1)).delete(book);

    }

}