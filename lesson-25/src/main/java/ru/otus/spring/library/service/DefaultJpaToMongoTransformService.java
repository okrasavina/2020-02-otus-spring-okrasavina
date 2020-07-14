package ru.otus.spring.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.domain.jpa.Author;
import ru.otus.spring.library.domain.jpa.Book;
import ru.otus.spring.library.domain.jpa.Comment;
import ru.otus.spring.library.domain.jpa.Genre;
import ru.otus.spring.library.domain.mongo.AuthorMongo;
import ru.otus.spring.library.domain.mongo.BookMongo;
import ru.otus.spring.library.domain.mongo.CommentMongo;
import ru.otus.spring.library.domain.mongo.GenreMongo;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
@Service
public class DefaultJpaToMongoTransformService implements JpaToMongoTransformService {
    private final MongoTemplate mongoTemplate;

    @Override
    public AuthorMongo transformAuthorToAuthorMongo(Author author) {
        return Optional.ofNullable(mongoTemplate
                .findOne(query(where("name").is(author.getName())), AuthorMongo.class))
                .orElse(new AuthorMongo(author.getName(), author.getBirthDay()));
    }

    @Override
    public GenreMongo transformGenreToGenreMongo(Genre genre) {
        return Optional.ofNullable(mongoTemplate
                .findOne(query(where("name").is(genre.getName())), GenreMongo.class))
                .orElse(new GenreMongo(genre.getName(), genre.getDescription()));
    }

    @Override
    public BookMongo transformBookToBookMongo(Book book) {
        return Optional.ofNullable(mongoTemplate
                .findOne(query(where("jpaId").is(book.getId())), BookMongo.class))
                .orElse(new BookMongo(book.getId(), book.getTitle(), book.getDescription(),
                        book.getAuthors().stream().map(this::transformAuthorToAuthorMongo).collect(Collectors.toList()),
                        book.getGenres().stream().map(this::transformGenreToGenreMongo).collect(Collectors.toList())));

    }

    @Override
    public CommentMongo transformCommentToCommentMongo(Comment comment) {
        return Optional.ofNullable(mongoTemplate
                .findOne(query(where("jpaId").is(comment.getId())), CommentMongo.class))
                .orElse(new CommentMongo(comment.getId(), comment.getTextComment(),
                        transformBookToBookMongo(comment.getBook())));
    }
}
