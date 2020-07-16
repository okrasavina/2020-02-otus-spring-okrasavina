package ru.otus.spring.library.service;

import ru.otus.spring.library.domain.jpa.Author;
import ru.otus.spring.library.domain.jpa.Book;
import ru.otus.spring.library.domain.jpa.Comment;
import ru.otus.spring.library.domain.jpa.Genre;
import ru.otus.spring.library.domain.mongo.AuthorMongo;
import ru.otus.spring.library.domain.mongo.BookMongo;
import ru.otus.spring.library.domain.mongo.CommentMongo;
import ru.otus.spring.library.domain.mongo.GenreMongo;

public interface JpaToMongoTransformService {
    AuthorMongo transformAuthorToAuthorMongo(Author author);

    GenreMongo transformGenreToGenreMongo(Genre genre);

    BookMongo transformBookToBookMongo(Book book);

    CommentMongo transformCommentToCommentMongo(Comment comment);
}
