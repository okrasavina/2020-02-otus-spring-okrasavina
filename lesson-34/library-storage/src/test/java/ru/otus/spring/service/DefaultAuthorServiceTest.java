package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с авторами должен")
@ExtendWith(MockitoExtension.class)
class DefaultAuthorServiceTest {

    private static final long DEFAULT_AUTHOR_ID = 1L;
    private static final long ERROR_AUTHOR_ID = 10L;

    @Mock
    private AuthorRepository authorRepository;

    private DefaultAuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new DefaultAuthorService(authorRepository);
    }

    @DisplayName("возвращать ожидаемый список авторов из БД")
    @Test
    void shouldReturnExpectedListAuthors() {
        LibraryAuthor libraryAuthor = getDefaultLibraryAuthor();
        List<LibraryAuthor> expected = new ArrayList<>(List.of(libraryAuthor));
        Author author = getDefaultAuthor();
        Book book = new Book(1L, "12 стульев", "Приключения Остапа Бендера", List.of(author),
                List.of(new Genre(1L, "Комедия", "Жанр")));

        when(authorRepository.findAll()).thenReturn(List.of(author));

        List<LibraryAuthor> actual = authorService.getListAuthor();

        assertThat(actual).hasSameSizeAs(expected)
                .hasSameElementsAs(expected);
        verify(authorRepository, times(1)).findAll();
    }

    @DisplayName("сохранять автора в БД")
    @Test
    void shouldSaveAuthor() {
        Author author = getDefaultAuthor();
        LibraryAuthor libraryAuthor = getDefaultLibraryAuthor();
        when(authorRepository.save(author)).thenReturn(author);

        LibraryAuthor actual = authorService.saveAuthor(libraryAuthor);

        assertThat(actual).isEqualTo(libraryAuthor);
        verify(authorRepository, times(1)).save(author);
    }

    @DisplayName("возвращать ожидаемого автора по его идентификатору")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author author = getDefaultAuthor();
        LibraryAuthor libraryAuthor = getDefaultLibraryAuthor();
        when(authorRepository.findById(DEFAULT_AUTHOR_ID)).thenReturn(Optional.of(author));

        LibraryAuthor actual = authorService.getAuthorById(DEFAULT_AUTHOR_ID);

        assertThat(actual).isEqualTo(libraryAuthor);
        verify(authorRepository, times(1)).findById(DEFAULT_AUTHOR_ID);
    }

    @DisplayName("возвращать ошибку, если нет автора по переданному идентификатору")
    @Test
    void shouldReturnEntityNotFoundException() {
        when(authorRepository.findById(ERROR_AUTHOR_ID)).thenReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException("Автор", "");

        assertThatThrownBy(() -> authorService.getAuthorById(ERROR_AUTHOR_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());

        verify(authorRepository, times(1)).findById(ERROR_AUTHOR_ID);
    }

    @DisplayName("удалять передаваемого автора")
    @Test
    void shouldDeleteAuthor() throws EntityNotAllowedDeleteException {
        LibraryAuthor libraryAuthor = getDefaultLibraryAuthor();
        authorService.deleteAuthor(libraryAuthor);

        verify(authorRepository, times(1)).deleteById(DEFAULT_AUTHOR_ID);
    }

    private Author getDefaultAuthor() {
        return new Author(1L, "Илья Ильф", LocalDate.of(1897, 10, 15));
    }

    private LibraryAuthor getDefaultLibraryAuthor() {
        return new LibraryAuthor(1L, "Илья Ильф", LocalDate.of(1897, 10, 15));
    }
}