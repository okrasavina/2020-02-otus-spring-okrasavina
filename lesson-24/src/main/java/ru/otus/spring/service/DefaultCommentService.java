package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultCommentService implements CommentService {

    private final CommentRepository commentRepository;
    private final BookService bookService;

    @Override
    public List<LibraryComment> getListCommentsByBook(LibraryBook libraryBook) {
        Book book = bookService.toDomain(libraryBook);
        List<Comment> comments = commentRepository.findAllByBook(book);
        return comments.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public LibraryComment saveComment(LibraryComment libraryComment) {
        Comment comment = toDomain(libraryComment);
        comment = commentRepository.save(comment);
        return toDto(comment);
    }

    @Override
    public LibraryComment getCommentById(Long commentNumber) {
        Comment comment = commentRepository.findById(commentNumber).orElseThrow(() -> new EntityNotFoundException("Комментарий по книге", ""));
        return toDto(comment);
    }

    @Transactional
    @Override
    public void deleteComment(LibraryComment libraryComment) {
        commentRepository.delete(toDomain(libraryComment));
    }

    private Comment toDomain(LibraryComment libraryComment) {
        Book book = bookService.toDomain(libraryComment.getLibraryBook());
        return new Comment(libraryComment.getNumber(), libraryComment.getTextComment(), book);
    }

    private LibraryComment toDto(Comment comment) {
        LibraryBook libraryBook = bookService.toDto(comment.getBook());
        return new LibraryComment(comment.getId(), comment.getTextComment(), libraryBook);
    }
}
