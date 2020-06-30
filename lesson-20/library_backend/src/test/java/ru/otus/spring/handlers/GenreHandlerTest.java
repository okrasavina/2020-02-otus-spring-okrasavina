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
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;

import static org.mockito.Mockito.*;

@DisplayName("Хэндлер для работы с жанрами должен")
@SpringBootTest(classes = {GenreHandler.class, LibraryRouter.class})
class GenreHandlerTest {

    @Autowired
    private RouterFunction routerFunction;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorHandler authorHandler;
    @MockBean
    private BookHandler bookHandler;
    @MockBean
    private CommentHandler commentHandler;

    private WebTestClient webTestClient;
    private Genre genre;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
        genre = new Genre("1", "Роман", "Литературный жанр");
    }

    @DisplayName("начитать жанр по переданному идентификатору")
    @Test
    void shouldGetExpectedGenreById() {
        when(genreRepository.findById(genre.getId())).thenReturn(Mono.just(genre));

        webTestClient.get()
                .uri("/api/genre/{id}", genre.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo(genre.getId())
                .jsonPath("$.name").isEqualTo(genre.getName())
                .jsonPath("$.description").isEqualTo(genre.getDescription());

        verify(genreRepository, times(1)).findById(genre.getId());
    }

    @DisplayName("начитать список жанров")
    @Test
    void shouldGetExpectedListGenres() {
        when(genreRepository.findAll()).thenReturn(Flux.just(genre));

        webTestClient.get()
                .uri("/api/genre")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(genre.getId())
                .jsonPath("$[0].name").isEqualTo(genre.getName())
                .jsonPath("$[0].description").isEqualTo(genre.getDescription());

        verify(genreRepository, times(1)).findAll();
    }

    @DisplayName("сохранять переданный жанр")
    @Test
    void shouldSaveGenre() {
        when(genreRepository.save(genre)).thenReturn(Mono.just(genre));

        webTestClient.post()
                .uri("/api/genre")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(LibraryGenre.toDto(genre))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo(genre.getId())
                .jsonPath("$.name").isEqualTo(genre.getName())
                .jsonPath("$.description").isEqualTo(genre.getDescription());

        verify(genreRepository, times(1)).save(genre);
    }

    @DisplayName("удалять жанр по переданному идентификатору")
    @Test
    void shouldDeleteGenreById() {
        Genre genreDeleting = new Genre("2", "Трагедия", "Еще один жанр");
        when(genreRepository.findById(genreDeleting.getId())).thenReturn(Mono.just(genreDeleting));
        when(bookRepository.findAllByGenresContaining(genreDeleting)).thenReturn(Flux.empty());
        when(genreRepository.delete(genreDeleting)).thenReturn(Mono.when(Mono.empty()));

        webTestClient.delete()
                .uri("/api/genre/{id}", genreDeleting.getId())
                .exchange()
                .expectStatus().isOk();

        verify(genreRepository, times(1)).findById(genreDeleting.getId());
        verify(bookRepository, times(1)).findAllByGenresContaining(genreDeleting);
        verify(genreRepository, times(1)).delete(genreDeleting);
    }

    @DisplayName("выдать ошибку при удалении жанра с книгами")
    @Test
    void shouldReturnExceptionByDeletingGenre() {
        when(genreRepository.findById(genre.getId())).thenReturn(Mono.just(genre));
        when(bookRepository.findAllByGenresContaining(genre)).thenReturn(Flux.just(new Book()));

        webTestClient.delete()
                .uri("/api/genre/{id}", genre.getId())
                .exchange()
                .expectStatus().isNotFound();

        verify(genreRepository, times(1)).findById(genre.getId());
        verify(bookRepository, times(1)).findAllByGenresContaining(genre);
        verify(genreRepository, times(0)).delete(genre);
    }
}