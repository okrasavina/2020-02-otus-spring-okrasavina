package ru.otus.spring.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@DisplayName("Хэндлер для работы с авторами должен")
@SpringBootTest(classes = {AuthorHandler.class, LibraryRouter.class})
class AuthorHandlerTest {

    @Autowired
    private RouterFunction routerFunction;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private GenreHandler genreHandler;
    @MockBean
    private BookHandler bookHandler;
    @MockBean
    private CommentHandler commentHandler;

    private WebTestClient webTestClient;
    private Author author;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
        author = new Author("1", "Николай Гоголь", LocalDate.of(1809, 3, 19));
    }

    @DisplayName("начитать автора по переданному идентификатору")
    @Test
    void shouldGetExpectedAuthorById() throws JsonProcessingException {
        when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));

        webTestClient.get()
                .uri("/api/author/{id}", author.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(getAuthorContent());

        verify(authorRepository, times(1)).findById(author.getId());
    }

    @DisplayName("начитать список авторов")
    @Test
    void shouldGetExpectedListAuthors() throws JsonProcessingException {
        when(authorRepository.findAll()).thenReturn(Flux.just(author));

        webTestClient.get()
                .uri("/api/author")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0]", getAuthorContent());

        verify(authorRepository, times(1)).findAll();
    }

    @DisplayName("сохранять переданного автора")
    @Test
    void shouldSaveAuthor() throws JsonProcessingException {
        when(authorRepository.save(author)).thenReturn(Mono.just(author));

        webTestClient.post()
                .uri("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LibraryAuthor(author))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(getAuthorContent());

        verify(authorRepository, times(1)).save(author);
    }

    @DisplayName("удалять автора по переданному идентификатору")
    @Test
    void shouldDeleteAuthorById() {
        Author authorDeleting = new Author("2", "Илья Ильф", LocalDate.of(1897, 10, 15));
        when(authorRepository.findById(authorDeleting.getId())).thenReturn(Mono.just(authorDeleting));
        when(bookRepository.findAllByAuthorsContaining(authorDeleting)).thenReturn(Flux.empty());
        when(authorRepository.delete(authorDeleting)).thenReturn(Mono.when(Mono.empty()));

        webTestClient.delete()
                .uri("/api/author/{id}", authorDeleting.getId())
                .exchange()
                .expectStatus().isOk();

        verify(authorRepository, times(1)).findById(authorDeleting.getId());
        verify(bookRepository, times(1)).findAllByAuthorsContaining(authorDeleting);
        verify(authorRepository, times(1)).delete(authorDeleting);
    }

    @DisplayName("выдать ошибку при удалении автора с книгами")
    @Test
    void shouldReturnExceptionByDeletingAuthor() {
        when(authorRepository.findById(author.getId())).thenReturn(Mono.just(author));
        when(bookRepository.findAllByAuthorsContaining(author)).thenReturn(Flux.just(new Book()));

        webTestClient.delete()
                .uri("/api/author/{id}", author.getId())
                .exchange()
                .expectStatus().isNotFound();

        verify(authorRepository, times(1)).findById(author.getId());
        verify(bookRepository, times(1)).findAllByAuthorsContaining(author);
        verify(authorRepository, times(0)).delete(author);
    }

    private String getAuthorContent() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(author);
    }
}