package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.service.GenreService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/api/genre")
    public ResponseEntity<List<LibraryGenre>> getListGenre() {
        return ResponseEntity.ok(genreService.getListGenre());
    }

    @GetMapping("/api/genre/{number}")
    public ResponseEntity<LibraryGenre> getGenre(@PathVariable long number) {
        return ResponseEntity.ok(genreService.getGenreById(number));
    }

    @PostMapping("/api/genre")
    public ResponseEntity<LibraryGenre> saveGenre(@RequestBody LibraryGenre genre) {
        return ResponseEntity.ok(genreService.saveGenre(genre));
    }

    @DeleteMapping("/api/genre")
    public ResponseEntity<Optional<LibraryGenre>> deleteGenre(@RequestBody LibraryGenre genre) throws EntityNotAllowedDeleteException {
        genreService.deleteGenre(genre);
        return ResponseEntity.ok(Optional.empty());
    }

}
