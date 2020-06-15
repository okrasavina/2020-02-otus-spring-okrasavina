package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;

@Controller
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
    public ResponseEntity<LibraryComment> getComment(@PathVariable Long number) {
        return ResponseEntity.ok(commentService.getCommentById(number));
    }

    @PostMapping("/api/comment/save/{bookNumber}")
    public ResponseEntity<Void> saveComment(@PathVariable Long bookNumber, @RequestBody LibraryComment comment) {
        comment.setLibraryBook(bookService.getBookById(bookNumber));
        commentService.saveComment(comment);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/api/comment/delete/{bookNumber}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long bookNumber, @RequestBody LibraryComment comment) {
        comment.setLibraryBook(bookService.getBookById(bookNumber));
        commentService.deleteComment(comment);
        return ResponseEntity.ok(null);
    }
}
