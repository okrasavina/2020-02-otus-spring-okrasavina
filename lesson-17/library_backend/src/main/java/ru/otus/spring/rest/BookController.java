package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.service.BookService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/api/book")
    public ResponseEntity<List<LibraryBook>> getListBook() {
        return ResponseEntity.ok(bookService.getListBook());
    }

    @GetMapping("/api/book/{number}")
    public ResponseEntity<LibraryBook> getBook(@PathVariable long number) {
        return ResponseEntity.ok(bookService.getBookById(number));
    }

    @PostMapping("/api/book")
    public ResponseEntity<LibraryBook> saveBook(@RequestBody LibraryBook book) {
        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @DeleteMapping("/api/book")
    public ResponseEntity<Optional<LibraryBook>> deleteBook(@RequestBody LibraryBook libraryBook) {
        bookService.deleteBook(libraryBook);
        return ResponseEntity.ok(Optional.empty());
    }

}
