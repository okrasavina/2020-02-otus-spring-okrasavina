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
import ru.otus.spring.service.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/api/book")
    public ResponseEntity<List<LibraryBook>> getListBook() {
        return ResponseEntity.ok(bookService.getListBook());
    }

    @GetMapping("/api/book/{number}")
    public ResponseEntity<LibraryBook> getBook(@PathVariable Long number) {
        return ResponseEntity.ok(bookService.getBookById(number));
    }

    @PostMapping("/api/book/save")
    public ResponseEntity<Void> saveBook(@RequestBody LibraryBook book) {
        bookService.saveBook(book);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/api/book/delete")
    public ResponseEntity<Void> deleteBook(@RequestBody LibraryBook libraryBook) {
        bookService.deleteBook(libraryBook);
        return ResponseEntity.ok(null);
    }

}
