package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BookService bookService;

    @GetMapping("/api/comment/{bookNumber}")
    public ResponseEntity<List<LibraryComment>> getListCommentsByBook(@PathVariable Long bookNumber) {
        LibraryBook book = bookService.getBookById(bookNumber);
        return ResponseEntity.ok(commentService.getListCommentsByBook(book));
    }

    @GetMapping("/api/comment/get/{number}")
    public ResponseEntity<LibraryComment> getComment(@PathVariable long number) {
        return ResponseEntity.ok(commentService.getCommentById(number));
    }

    @PostMapping("/api/comment/{bookNumber}")
    public ResponseEntity<LibraryComment> saveComment(@PathVariable long bookNumber, @RequestBody LibraryComment comment) {
        comment.setLibraryBook(bookService.getBookById(bookNumber));
        return ResponseEntity.ok(commentService.saveComment(comment));
    }

    @DeleteMapping("/api/comment/{bookNumber}")
    public ResponseEntity<Optional<LibraryComment>> deleteComment(@PathVariable long bookNumber, @RequestBody LibraryComment comment) {
        comment.setLibraryBook(bookService.getBookById(bookNumber));
        commentService.deleteComment(comment);
        return ResponseEntity.ok(Optional.empty());
    }
}
