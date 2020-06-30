package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с жанрами должен")
@ExtendWith(MockitoExtension.class)
class DefaultGenreServiceTest {

    private static final Long DEFAULT_GENRE_ID = 1L;
    private static final long ERROR_GENRE_ID = 10L;

    private Genre genre;
    private LibraryGenre libraryGenre;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private BookRepository bookRepository;

    private DefaultGenreService genreService;

    @BeforeEach
    void setUp() {
        genreService = new DefaultGenreService(genreRepository, bookRepository);
        genre = new Genre(1L, "Комедия", "Литературный жанр");
        libraryGenre = new LibraryGenre(1L, "Комедия", "Литературный жанр", false);
    }

    @DisplayName("возвращать ожидаемый список жанров из БД")
    @Test
    void shouldReturnExpectedListGenres() {
        libraryGenre.setCouldDelete(true);
        List<LibraryGenre> expected = new ArrayList<>(List.of(libraryGenre));

        when(genreRepository.findAll()).thenReturn(List.of(genre));
        when(bookRepository.findAllByGenresContaining(genre)).thenReturn(List.of());

        List<LibraryGenre> actual = genreService.getListGenre();

        assertThat(actual).hasSameSizeAs(expected)
                .hasSameElementsAs(expected);
        verify(genreRepository, times(1)).findAll();
        verify(bookRepository, times(1)).findAllByGenresContaining(genre);
    }

    @DisplayName("сохранять жанр в БД")
    @Test
    void shouldSaveGenre() {
        when(genreRepository.save(genre)).thenReturn(genre);

        LibraryGenre actual = genreService.saveGenre(libraryGenre);

        assertThat(actual).isEqualTo(libraryGenre);
        verify(genreRepository, times(1)).save(genre);
    }

    @DisplayName("возвращать ожидаемый жанр по его идентификатору")
    @Test
    void shouldReturnExpectedGenreById() {
        when(genreRepository.findById(DEFAULT_GENRE_ID)).thenReturn(Optional.of(genre));

        LibraryGenre actual = genreService.getGenreById(DEFAULT_GENRE_ID);

        assertThat(actual).isEqualTo(libraryGenre);
        verify(genreRepository, times(1)).findById(DEFAULT_GENRE_ID);
    }

    @DisplayName("возвращать ошибку, если нет жанра по переданному идентификатору")
    @Test
    void shouldReturnEntityNotFoundException() {
        when(genreRepository.findById(ERROR_GENRE_ID)).thenReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException("Жанр", "");

        assertThatThrownBy(() -> genreService.getGenreById(ERROR_GENRE_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());

        verify(genreRepository, times(1)).findById(ERROR_GENRE_ID);
    }

    @DisplayName("удалять передаваемый жанр")
    @Test
    void shouldDeleteGenre() {
        genreService.deleteGenre(libraryGenre);

        verify(genreRepository, times(1)).deleteById(DEFAULT_GENRE_ID);
    }

}