package ru.otus.spring.repositories;

import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.LibraryBook;

public interface CommentRepositoryJpa {
    Comment save(Comment comment);

    void deleteCommentsByBook(LibraryBook book);
}
