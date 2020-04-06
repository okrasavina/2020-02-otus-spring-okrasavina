package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с Book должен")
@JdbcTest
@Import(BookDaoImpl.class)
class BookDaoImplTest {

    public static final long INSERTED_BOOK_ID = 2L;
    public static final String INSERTED_BOOK_NAME = "Мертвые души";
    public static final long DEFAULT_BOOK_ID = 1L;
    public static final String DEFAULT_BOOK_NAME = "12 стульев";
    public static final long DEFAULT_AUTHOR_ID = 1L;
    public static final long DEFAULT_GENRE_ID = 1L;

    @Autowired
    private BookDaoImpl dao;

    @DisplayName("добавлять Book в БД")
    @Test
    void shouldInsertBook() {
        Book expected = new Book(INSERTED_BOOK_ID, INSERTED_BOOK_NAME);
        Book insertedBook = dao.insert(INSERTED_BOOK_NAME, new ArrayList<>(), new ArrayList<>());

        assertThat(insertedBook).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("возвращать список всех книг в БД")
    @Test
    void shouldReturnAllBooks() {
        List<Book> listExpected = new ArrayList<>();
        listExpected.add(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME));
        List<Book> listActual = dao.getAll();

        assertThat(listActual.size()).isEqualTo(listExpected.size());
        assertThat(listActual).hasSameElementsAs(listExpected);
    }

    @DisplayName("удалять книгу по id")
    @Test
    void shouldDeleteBookById() {
        dao.deleteById(DEFAULT_BOOK_ID);
        Optional<Book> book = dao.getById(DEFAULT_BOOK_ID);

        assertThat(book.isPresent()).isEqualTo(false);
    }

    @DisplayName("возвращать книгу по ее id")
    @Test
    void shouldReturnExpectedBookById() {
        Book expected = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME);
        Optional<Book> actual = dao.getById(DEFAULT_BOOK_ID);

        assertThat(actual.isPresent()).isEqualTo(true);
        assertThat(actual.get()).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("возвращать список книг по id автора")
    @Test
    void shouldReturnBookListByAuthorId() {
        List<Book> listExpected = new ArrayList<>();
        listExpected.add(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME));
        List<Book> listActual = dao.getAllByAuthorId(DEFAULT_AUTHOR_ID);

        assertThat(listActual.size()).isEqualTo(listExpected.size());
        assertThat(listActual).hasSameElementsAs(listExpected);
    }

    @DisplayName("возвращать список книг по id жанра")
    @Test
    void shouldReturnBookListByGenreId() {
        List<Book> listExpected = new ArrayList<>();
        listExpected.add(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME));
        List<Book> listActual = dao.getAllByGenreId(DEFAULT_GENRE_ID);

        assertThat(listActual.size()).isEqualTo(listExpected.size());
        assertThat(listActual).hasSameElementsAs(listExpected);
    }

}