package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    @Override
    public List<LibraryGenre> getListGenre() {
        List<LibraryGenre> genres = new ArrayList<>();
        genreRepository.findAll().forEach(genre -> {
            LibraryGenre libraryGenre = toDto(genre);
            libraryGenre.setCouldDelete(bookRepository.findAllByGenresContaining(genre).isEmpty());
            genres.add(libraryGenre);
        });
        return genres;
    }

    @Override
    public LibraryGenre saveGenre(LibraryGenre libraryGenre) {
        Genre genre = toDomain(libraryGenre);
        genre = genreRepository.save(genre);
        return toDto(genre);
    }

    @Override
    public LibraryGenre getGenreById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Жанр", ""));
        return toDto(genre);
    }

    @Override
    public void deleteGenre(LibraryGenre libraryGenre) {
        genreRepository.deleteById(libraryGenre.getNumber());
    }

    private Genre toDomain(LibraryGenre libraryGenre) {
        return new Genre(libraryGenre.getNumber(), libraryGenre.getName(), libraryGenre.getDescription());
    }

    private LibraryGenre toDto(Genre genre) {
        return new LibraryGenre(genre.getId(), genre.getName(), genre.getDescription(), false);
    }

}
