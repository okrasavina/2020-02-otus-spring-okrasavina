package ru.otus.spring.repositories;

import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Book;

public interface CommentRepository {
    Comment save(Comment comment);

    void deleteCommentsByBook(Book book);
}
