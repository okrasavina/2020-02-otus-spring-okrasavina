package ru.otus.spring.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class GenreHandler {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> getGenre(ServerRequest request) {
        return genreRepository.findById(request.pathVariable("id"))
                .map(LibraryGenre::new)
                .flatMap(libraryGenre -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromValue(libraryGenre)))
                .onErrorResume(ex -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getListGenre() {
        Flux<LibraryGenre> genreFlux = genreRepository.findAll()
                .map(LibraryGenre::new);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(genreFlux, LibraryGenre.class);
    }

    public Mono<ServerResponse> saveGenre(ServerRequest request) {
        Mono<LibraryGenre> libraryGenreMono = request.bodyToMono(LibraryGenre.class)
                .map(this::toDomain)
                .flatMap(genreRepository::save)
                .map(LibraryGenre::new);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(libraryGenreMono, LibraryGenre.class);
    }

    public Mono<ServerResponse> deleteGenre(ServerRequest request) {
        return genreRepository.findById(request.pathVariable("id"))
                .flatMap(genre -> bookRepository.findAllByGenresContaining(genre)
                        .hasElements()
                        .flatMap(notEmpty -> notEmpty ? Mono.error(new EntityNotAllowedDeleteException("жанр"))
                                : genreRepository.delete(genre)))
                .then(ServerResponse.ok().build())
                .onErrorResume(ex -> ServerResponse.notFound().build());
    }

    private Genre toDomain(LibraryGenre genre) {
        return new Genre(genre.getId(), genre.getName(), genre.getDescription());
    }
}
