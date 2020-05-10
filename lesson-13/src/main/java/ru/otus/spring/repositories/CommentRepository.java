package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Book;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    void deleteAllByBook(Book book);

    List<Comment> findAllByBook(Book book);
}
