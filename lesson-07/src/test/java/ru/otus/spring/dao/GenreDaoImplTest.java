package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с Genre должно")
@JdbcTest
@Import(GenreDaoImpl.class)
class GenreDaoImplTest {

    public static final long DEFAULT_GENRE_ID = 1L;
    public static final String DEFAULT_GENRE_NAME = "Комедия";
    public static final String ERROR_GENRE_NAME = "Роман";
    public static final long INSERTED_GENRE_ID = 2L;
    public static final String INSERTED_GENRE_NAME = "Детектив";
    public static final long DEFAULT_BOOK_ID = 1L;

    @Autowired
    private GenreDaoImpl dao;

    @DisplayName("возвращать ожидаемого Genre по его name")
    @Test
    void shouldReturnExpectedGenreByName() {
        Genre expectedGenre = new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME);
        Optional<Genre> actualGenre = dao.getByName(DEFAULT_GENRE_NAME);

        assertThat(actualGenre.isPresent()).isEqualTo(true);
        assertThat(actualGenre.get()).isEqualToComparingFieldByField(expectedGenre);
    }

    @DisplayName("возвращать пустое значение, если Genre не существует по заданному name")
    @Test
    void shouldReturnNullIfGenreIsNotExistsByName() {
        Optional<Genre> errorGenre = dao.getByName(ERROR_GENRE_NAME);

        assertThat(errorGenre.isPresent()).isEqualTo(false);
    }

    @DisplayName("корректно добавлять Genre в БД")
    @Test
    void shouldCorrectInsertGenre() {
        Genre genreExpected = new Genre(INSERTED_GENRE_ID, INSERTED_GENRE_NAME);
        Genre genreInserted = dao.insert(new Genre(INSERTED_GENRE_NAME));

        assertThat(genreInserted).isEqualToComparingFieldByField(genreExpected);
    }

    @DisplayName("возвращать список жанров по id книги")
    @Test
    void shouldReturnListGenresByBookId() {
        List<Genre> listExpected = new ArrayList<>();
        listExpected.add(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<Genre> listActual = dao.getListByBookId(DEFAULT_BOOK_ID);

        assertThat(listActual.size()).isEqualTo(listExpected.size());
        assertThat(listActual).hasSameElementsAs(listExpected);
    }

    @DisplayName("возвращать список всех жанров")
    @Test
    void shouldReturnAllGenres() {
        List<Genre> listExpected = new ArrayList<>();
        listExpected.add(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<Genre> listActual = dao.getAll();

        assertThat(listActual.size()).isEqualTo(listExpected.size());
        assertThat(listActual).hasSameElementsAs(listExpected);
    }

    @DisplayName("удалять жанры без книг")
    @Test
    void shouldDeleteGenreWithoutBooks() {
        List<Genre> beforeInsert = dao.getAll();
        dao.insert(new Genre(INSERTED_GENRE_NAME));
        dao.deleteGenresWithoutBooks();
        List<Genre> afterDelete = dao.getAll();

        assertThat(afterDelete.size()).isEqualTo(beforeInsert.size());
        assertThat(afterDelete).hasSameElementsAs(beforeInsert);
    }
}