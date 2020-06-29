package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.handlers.AuthorHandler;
import ru.otus.spring.handlers.BookHandler;
import ru.otus.spring.handlers.CommentHandler;
import ru.otus.spring.handlers.GenreHandler;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.repositories.GenreRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LibraryRouter {

    @Bean
    public RouterFunction<ServerResponse> libraryRoutes(AuthorHandler authorHandler,
                                                        GenreHandler genreHandler,
                                                        BookHandler bookHandler,
                                                        CommentHandler commentHandler) {
        return route()
                .GET("/api/author/{id}", accept(APPLICATION_JSON), authorHandler::getAuthor)
                .GET("/api/author", accept(APPLICATION_JSON), request -> authorHandler.getListAuthor())
                .POST("/api/author", contentType(APPLICATION_JSON), authorHandler::saveAuthor)
                .DELETE("/api/author/{id}", accept(APPLICATION_JSON), authorHandler::deleteAuthor)
                .GET("/api/genre/{id}", accept(APPLICATION_JSON), genreHandler::getGenre)
                .GET("/api/genre", accept(APPLICATION_JSON), request -> genreHandler.getListGenre())
                .POST("/api/genre", contentType(APPLICATION_JSON), genreHandler::saveGenre)
                .DELETE("/api/genre/{id}", accept(APPLICATION_JSON), genreHandler::deleteGenre)
                .GET("/api/book/{id}", accept(APPLICATION_JSON), bookHandler::getBook)
                .GET("/api/book", accept(APPLICATION_JSON), request -> bookHandler.getListBook())
                .POST("/api/book", contentType(APPLICATION_JSON), bookHandler::saveBook)
                .DELETE("/api/book/{id}", accept(APPLICATION_JSON), bookHandler::deleteBook)
                .GET("/api/comment/list/{bookId}", accept(APPLICATION_JSON), commentHandler::getListComment)
                .GET("/api/comment/{id}", accept(APPLICATION_JSON), commentHandler::getComment)
                .POST("/api/comment/{bookId}", contentType(APPLICATION_JSON), commentHandler::saveComment)
                .DELETE("/api/comment/{id}", accept(APPLICATION_JSON), commentHandler::deleteComment)
                .build();
    }
}
