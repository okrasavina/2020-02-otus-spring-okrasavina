package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами книг должен")
@DataJpaTest
class GenreRepositoryJpaTest {

    public static final long DEFAULT_GENRE_ID = 1L;
    public static final String DEFAULT_GENRE_NAME = "Комедия";
    public static final String ERROR_GENRE_NAME = "Роман";
    public static final long SECOND_GENRE_ID = 2L;
    public static final String SECOND_GENRE_NAME = "Приключения";

    @Autowired
    private GenreRepository repo;

    @DisplayName("возвращать список всех жанров")
    @Test
    void shouldReturnListAllGenre() {
        List<Genre> listExpected = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME),
                new Genre(SECOND_GENRE_ID, SECOND_GENRE_NAME));
        List<Genre> listActual = repo.findAll();

        assertThat(listActual).hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected);
    }

    @DisplayName("возвращать жанр по его имени")
    @Test
    void shouldReturnExpectedGenreByName() {
        Genre genreExpected = new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME);
        Optional<Genre> genreActual = repo.findByName(DEFAULT_GENRE_NAME);

        assertThat(genreActual).isPresent()
                .get().isEqualTo(genreExpected);
    }

    @DisplayName("возвращать пустое значение, если Genre не существует по заданному имени")
    @Test
    void shouldReturnNullIfGenreIsNotExistsByName() {
        Optional<Genre> errorGenre = repo.findByName(ERROR_GENRE_NAME);

        assertThat(errorGenre.isPresent()).isEqualTo(false);
    }

    @DisplayName("удалять жанры без книг")
    @Test
    void shouldDeleteGenreWithoutBooks() {
        List<Genre> beforeDelete = repo.findAll();
        repo.deleteGenreWithoutBooks();
        List<Genre> afterDelete = repo.findAll();

        assertThat(afterDelete).isNotEmpty().hasSizeLessThan(beforeDelete.size())
                .doesNotContain(beforeDelete.get(1));
    }

}