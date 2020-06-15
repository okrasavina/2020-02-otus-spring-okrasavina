package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.service.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/author")
    public ResponseEntity<List<LibraryAuthor>> getListAuthor() {
        return ResponseEntity.ok(authorService.getListAuthor());
    }

    @GetMapping("/api/author/{number}")
    public ResponseEntity<LibraryAuthor> getAuthor(@PathVariable Long number) {
        return ResponseEntity.ok(authorService.getAuthorById(number));
    }

    @PostMapping("/api/author/save")
    public ResponseEntity<Void> saveAuthor(@RequestBody LibraryAuthor author) {
        authorService.saveAuthor(author);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/api/author/delete")
    public ResponseEntity<Void> deleteAuthor(@RequestBody LibraryAuthor author) throws EntityNotAllowedDeleteException {
        authorService.deleteAuthor(author);
        return ResponseEntity.ok(null);
    }

}
