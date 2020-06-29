package ru.otus.spring.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class BookHandler {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    public Mono<ServerResponse> getBook(ServerRequest request) {
        return bookRepository.findById(request.pathVariable("id"))
                .map(LibraryBook::new)
                .flatMap(libraryBook -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromValue(libraryBook)))
                .onErrorResume(ex -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getListBook() {
        Flux<LibraryBook> bookFlux = bookRepository.findAll()
                .map(LibraryBook::new);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(bookFlux, LibraryBook.class);
    }

    public Mono<ServerResponse> saveBook(ServerRequest request) {
        Mono<LibraryBook> libraryBookMono = request.bodyToMono(LibraryBook.class)
                .flatMap(book -> getAuthorsByLibraryBook(book).zipWith(getGenresByLibraryBook(book))
                        .map(tuple -> new Book(book.getId(), book.getTitle(), book.getDescription(),
                                tuple.getT1(), tuple.getT2())))
                .flatMap(bookRepository::save)
                .map(LibraryBook::new);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(libraryBookMono, LibraryBook.class);
    }

    public Mono<ServerResponse> deleteBook(ServerRequest request) {
        return bookRepository.findById(request.pathVariable("id"))
                .map(book -> {
                    commentRepository.deleteAllByBook(book);
                    return bookRepository.delete(book);
                })
                .then(ServerResponse.ok().build())
                .onErrorResume(ex -> ServerResponse.notFound().build());
    }

    private Mono<List<Genre>> getGenresByLibraryBook(LibraryBook libraryBook) {
        return Flux.fromArray(libraryBook.getGenres().split(","))
                .flatMap(genreName -> genreRepository.findByName(genreName.trim())
                        .switchIfEmpty(Mono.just(new Genre(genreName.trim(), ""))))
                .flatMap(genreRepository::save).collectList();
    }

    private Mono<List<Author>> getAuthorsByLibraryBook(LibraryBook libraryBook) {
        return Flux.fromArray(libraryBook.getAuthors().split(","))
                .flatMap(authorName -> authorRepository.findByName(authorName.trim())
                        .switchIfEmpty(Mono.just(new Author(authorName.trim(), LocalDate.now()))))
                .flatMap(authorRepository::save).collectList();

    }
}
