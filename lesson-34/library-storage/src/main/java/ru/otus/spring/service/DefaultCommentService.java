package ru.otus.spring.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultCommentService implements CommentService {

    private final CommentRepository commentRepository;
    private final BookService bookService;

    @HystrixCommand(fallbackMethod = "getDefaultListComment")
    @Override
    public List<LibraryComment> getListCommentsByBook(LibraryBook libraryBook) {
        Book book = bookService.toDomain(libraryBook);
        List<Comment> comments = commentRepository.findAllByBook(book);
        return comments.stream().map(this::toDto).collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "saveCommentDefault")
    @Transactional
    @Override
    public LibraryComment saveComment(LibraryComment libraryComment) {
        Comment comment = toDomain(libraryComment);
        comment = commentRepository.save(comment);
        return toDto(comment);
    }

    @HystrixCommand(fallbackMethod = "getDefaultCommentById",
            ignoreExceptions = {EntityNotFoundException.class})
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

    public List<LibraryComment> getDefaultListComment(LibraryBook libraryBook) {
        LibraryComment libraryComment = getDefaultCommentById(0L);
        libraryComment.setLibraryBook(libraryBook);
        return List.of(libraryComment);
    }

    public LibraryComment getDefaultCommentById(Long commentNumber) {
        return new LibraryComment(commentNumber, "Пустой комментарий", null);
    }

    public LibraryComment saveCommentDefault(LibraryComment libraryComment) {
        libraryComment.setNumber(0L);
        return libraryComment;
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
