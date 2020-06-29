package ru.otus.spring.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class AuthorHandler {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> getAuthor(ServerRequest request) {
        return authorRepository.findById(request.pathVariable("id"))
                .map(LibraryAuthor::new)
                .flatMap(libraryAuthor -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromValue(libraryAuthor)))
                .onErrorResume(ex -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getListAuthor() {
        Flux<LibraryAuthor> authorFlux = authorRepository.findAll()
                .map(LibraryAuthor::new);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(authorFlux, LibraryAuthor.class);
    }

    public Mono<ServerResponse> saveAuthor(ServerRequest request) {
        Mono<LibraryAuthor> libraryAuthorMono = request.bodyToMono(LibraryAuthor.class)
                .map(this::toDomain)
                .flatMap(authorRepository::save)
                .map(LibraryAuthor::new);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(libraryAuthorMono, LibraryAuthor.class);
    }

    public Mono<ServerResponse> deleteAuthor(ServerRequest request) {
        return authorRepository.findById(request.pathVariable("id"))
                .flatMap(author -> bookRepository.findAllByAuthorsContaining(author)
                        .hasElements()
                        .flatMap(notEmpty -> notEmpty ? Mono.error(new EntityNotAllowedDeleteException("автор"))
                                : authorRepository.delete(author)))
                .then(ServerResponse.ok().build())
                .onErrorResume(ex -> ServerResponse.notFound().build());
    }

    private Author toDomain(LibraryAuthor author) {
        return new Author(author.getId(), author.getName(), author.getBirthDay());
    }

}
