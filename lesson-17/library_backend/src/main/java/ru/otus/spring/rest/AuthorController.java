package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.service.AuthorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/author")
    public ResponseEntity<List<LibraryAuthor>> getListAuthor() {
        return ResponseEntity.ok(authorService.getListAuthor());
    }

    @GetMapping("/api/author/{number}")
    public ResponseEntity<LibraryAuthor> getAuthor(@PathVariable long number) {
        return ResponseEntity.ok(authorService.getAuthorById(number));
    }

    @PostMapping("/api/author")
    public ResponseEntity<LibraryAuthor> saveAuthor(@RequestBody LibraryAuthor author) {
        return ResponseEntity.ok(authorService.saveAuthor(author));
    }

    @DeleteMapping("/api/author")
    public ResponseEntity<Optional<LibraryAuthor>> deleteAuthor(@RequestBody LibraryAuthor author) throws EntityNotAllowedDeleteException {
        authorService.deleteAuthor(author);
        return ResponseEntity.ok(Optional.empty());
    }

}
