package ru.otus.spring.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class CommentHandler {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public Mono<ServerResponse> getComment(ServerRequest request) {
        return commentRepository.findById(request.pathVariable("id"))
                .map(LibraryComment::toDto)
                .flatMap(libraryComment -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromValue(libraryComment)))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(ex -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public Mono<ServerResponse> getListComment(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(bookRepository.findById(request.pathVariable("bookId"))
                        .flatMapMany(commentRepository::findAllByBook)
                        .map(LibraryComment::toDto), LibraryComment.class);
    }

    public Mono<ServerResponse> saveComment(ServerRequest request) {
        Mono<LibraryComment> libraryCommentMono = bookRepository.findById(request.pathVariable("bookId"))
                .zipWith(request.bodyToMono(LibraryComment.class))
                .map(tuple -> new Comment(tuple.getT2().getId(), tuple.getT2().getTextComment(), tuple.getT1()))
                .flatMap(commentRepository::save)
                .map(LibraryComment::toDto);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(libraryCommentMono, LibraryComment.class);
    }

    public Mono<ServerResponse> deleteComment(ServerRequest request) {
        return commentRepository.deleteById(request.pathVariable("id"))
                .then(ServerResponse.ok().build())
                .onErrorResume(ex -> ServerResponse.notFound().build());
    }
}
