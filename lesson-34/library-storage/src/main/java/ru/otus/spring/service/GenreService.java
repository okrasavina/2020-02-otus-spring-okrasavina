package ru.otus.spring.service;

import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.LibraryGenre;

import java.util.List;

public interface GenreService {
    List<LibraryGenre> getListGenre();

    LibraryGenre saveGenre(LibraryGenre libraryGenre);

    LibraryGenre getGenreById(Long id);

    void deleteGenre(LibraryGenre libraryGenre) throws EntityNotAllowedDeleteException;
}
