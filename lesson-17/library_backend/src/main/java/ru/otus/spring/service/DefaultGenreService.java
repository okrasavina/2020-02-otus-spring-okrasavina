package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultGenreService implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<LibraryGenre> getListGenre() {
        return genreRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
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

    @Transactional
    @Override
    public void deleteGenre(LibraryGenre libraryGenre) throws EntityNotAllowedDeleteException{
        try {
            genreRepository.deleteById(libraryGenre.getNumber());
        } catch (Exception e) {
            throw new EntityNotAllowedDeleteException("жанр");
        }

    }

    private Genre toDomain(LibraryGenre libraryGenre) {
        return new Genre(libraryGenre.getNumber(), libraryGenre.getName(), libraryGenre.getDescription());
    }

    private LibraryGenre toDto(Genre genre) {
        return new LibraryGenre(genre.getId(), genre.getName(), genre.getDescription());
    }

}
