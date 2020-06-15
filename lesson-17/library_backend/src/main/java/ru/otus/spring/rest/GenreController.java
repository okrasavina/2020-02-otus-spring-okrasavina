package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/api/genre")
    public ResponseEntity<List<LibraryGenre>> getListGenre() {
        return ResponseEntity.ok(genreService.getListGenre());
    }

    @GetMapping("/api/genre/{number}")
    public ResponseEntity<LibraryGenre> getGenre(@PathVariable Long number) {
        return ResponseEntity.ok(genreService.getGenreById(number));
    }

    @PostMapping("/api/genre/save")
    public ResponseEntity<Void> saveGenre(@RequestBody LibraryGenre genre) {
        genreService.saveGenre(genre);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/api/genre/delete/")
    public ResponseEntity<Void> deleteGenre(@RequestBody LibraryGenre genre) throws EntityNotAllowedDeleteException {
        genreService.deleteGenre(genre);
        return ResponseEntity.ok(null);
    }

}
