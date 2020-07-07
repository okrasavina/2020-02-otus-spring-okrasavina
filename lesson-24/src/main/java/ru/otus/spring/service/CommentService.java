package ru.otus.spring.service;

import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryComment;

import java.util.List;

public interface CommentService {
    List<LibraryComment> getListCommentsByBook(LibraryBook libraryBook);

    LibraryComment saveComment(LibraryComment libraryComment);

    LibraryComment getCommentById(Long commentNumber);

    void deleteComment(LibraryComment libraryComment);
}
