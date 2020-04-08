package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryBook;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с Book должен")
@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {

    public static final String INSERTED_BOOK_NAME = "Мертвые души";
    public static final long INSERTED_BOOK_ID = 2L;
    public static final String INSERTED_AUTHOR_NAME = "Гоголь";
    public static final String INSERTED_GENRE_NAME = "Комедия";
    public static final long DEFAULT_BOOK_ID = 1L;
    public static final String DEFAULT_BOOK_NAME = "12 стульев";
    public static final long FIRST_AUTHOR_ID = 1L;
    public static final String FIRST_AUTHOR_NAME = "Илья Ильф";
    public static final long SECOND_AUTHOR_ID = 2L;
    public static final String SECOND_AUTHOR_NAME = "Евгений Петров";
    public static final long DEFAULT_GENRE_ID = 1L;
    public static final String DEFAULT_GENRE_NAME = "Комедия";
    public static final String ERROR_AUTHOR_NAME = "Агния Барто";
    public static final String ERROR_GENRE_NAME = "Драма";

    @Configuration
    @ComponentScan("ru.otus.spring.service")
    private static class Config {
    }

    @MockBean
    private BookDao bookDao;
    @MockBean
    private AuthorDao authorDao;
    @MockBean
    private GenreDao genreDao;

    private BookServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BookServiceImpl(bookDao, authorDao, genreDao);
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    void shouldCreateNewBook() {
        Book bookExpected = new Book(INSERTED_BOOK_ID, INSERTED_BOOK_NAME);
        List<Author> authors = List.of(new Author(INSERTED_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(INSERTED_GENRE_NAME));

        LibraryBook libraryBook = new LibraryBook(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME), authors, genres);

        given(bookDao.insert(any(Book.class), anyList(), anyList())).willReturn(bookExpected);
        service.createBook(libraryBook, authors.stream().map(Author::getName).collect(Collectors.toList()),
                List.of(INSERTED_GENRE_NAME));

        verify(bookDao, times(1)).insert(any(Book.class), anyList(), anyList());
    }

    @DisplayName("возвращать список всех книг в БД")
    @Test
    void shouldReturnListAllBooks() {
        List<Book> books = List.of(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME));
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<LibraryBook> expected = List.of(new LibraryBook(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME),
                authors, genres));

        given(bookDao.getAll()).willReturn(books);
        given(authorDao.getListByBookId(DEFAULT_BOOK_ID)).willReturn(authors);
        given(genreDao.getListByBookId(DEFAULT_BOOK_ID)).willReturn(genres);

        List<LibraryBook> actual = service.getListBook();

        assertThat(actual).hasSameElementsAs(expected);
        verify(bookDao, times(1)).getAll();
        verify(authorDao, times(1)).getListByBookId(DEFAULT_BOOK_ID);
        verify(genreDao, times(1)).getListByBookId(DEFAULT_BOOK_ID);
    }

    @DisplayName("вызывать необходимые методы Dao при удалении книги по номеру")
    @Test
    void shouldDeleteBookById() {
        service.deleteBookById(DEFAULT_BOOK_ID);

        verify(bookDao, times(1)).deleteById(DEFAULT_BOOK_ID);
        verify(authorDao, times(1)).deleteAuthorsWithoutBooks();
        verify(genreDao, times(1)).deleteGenresWithoutBooks();
    }

    @DisplayName("возвращать книгу по ее id")
    @Test
    void shouldReturnExpectedBookById() {
        Book book = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME);
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        String expected = new LibraryBook(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME),
                authors, genres).toString();

        given(bookDao.getById(DEFAULT_BOOK_ID)).willReturn(Optional.of(book));
        given(authorDao.getListByBookId(DEFAULT_BOOK_ID)).willReturn(authors);
        given(genreDao.getListByBookId(DEFAULT_BOOK_ID)).willReturn(genres);

        String actual = service.getBookByNumber(DEFAULT_BOOK_ID);

        assertThat(actual).isEqualTo(expected);
        verify(bookDao, times(1)).getById(DEFAULT_BOOK_ID);
        verify(authorDao, times(1)).getListByBookId(DEFAULT_BOOK_ID);
        verify(genreDao, times(1)).getListByBookId(DEFAULT_BOOK_ID);
    }

    @DisplayName("возвращать список авторов в БД")
    @Test
    void shouldReturnExpectedListAuthors() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<String> expected = authors.stream().map(Author::toString).collect(Collectors.toList());

        given(authorDao.getAll()).willReturn(authors);
        List<String> listAuthor = service.getListAuthor();

        assertThat(listAuthor).hasSameElementsAs(expected);
        verify(authorDao, times(1)).getAll();
    }

    @DisplayName("возвращать список книг по имени автора")
    @Test
    void shouldReturnExpectedListBookByAuthorName() {
        List<Book> books = List.of(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME));
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<LibraryBook> expected = List.of(new LibraryBook(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME),
                authors, genres));

        given(authorDao.getByName(FIRST_AUTHOR_NAME)).willReturn(Optional.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME)));
        given(bookDao.getAllByAuthorId(FIRST_AUTHOR_ID)).willReturn(books);
        given(authorDao.getListByBookId(DEFAULT_BOOK_ID)).willReturn(authors);
        given(genreDao.getListByBookId(DEFAULT_BOOK_ID)).willReturn(genres);

        List<LibraryBook> actual = service.getListBookByAuthorName(FIRST_AUTHOR_NAME);

        assertThat(actual).hasSameElementsAs(expected);
        verify(bookDao, times(1)).getAllByAuthorId(FIRST_AUTHOR_ID);
        verify(authorDao, times(1)).getListByBookId(DEFAULT_BOOK_ID);
        verify(genreDao, times(1)).getListByBookId(DEFAULT_BOOK_ID);
    }

    @DisplayName("возвращать список жанров в БД")
    @Test
    void shouldReturnExpectedListGenres() {
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<String> expected = genres.stream().map(Genre::toString).collect(Collectors.toList());

        given(genreDao.getAll()).willReturn(genres);
        List<String> listGenre = service.getListGenres();

        assertThat(listGenre).hasSameElementsAs(expected);
        verify(genreDao, times(1)).getAll();
    }

    @DisplayName("возвращать список книг по названию жанра")
    @Test
    void shouldReturnExpectedListBooksByGenreName() {
        List<Book> books = List.of(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME));
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<LibraryBook> expected = List.of(new LibraryBook(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME),
                authors, genres));

        given(genreDao.getByName(DEFAULT_GENRE_NAME)).willReturn(Optional.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME)));
        given(bookDao.getAllByGenreId(DEFAULT_GENRE_ID)).willReturn(books);
        given(authorDao.getListByBookId(DEFAULT_BOOK_ID)).willReturn(authors);
        given(genreDao.getListByBookId(DEFAULT_BOOK_ID)).willReturn(genres);

        List<LibraryBook> actual = service.getListBookByGenreName(DEFAULT_GENRE_NAME);

        assertThat(actual).hasSameElementsAs(expected);
        verify(bookDao, times(1)).getAllByGenreId(DEFAULT_GENRE_ID);
        verify(authorDao, times(1)).getListByBookId(DEFAULT_BOOK_ID);
        verify(genreDao, times(1)).getListByBookId(DEFAULT_BOOK_ID);
    }

    @DisplayName("возвращать ошибку, если нет автора по переданному имени")
    @Test
    void shouldReturnAuthorNotFoundException() {
        given(authorDao.getByName(ERROR_AUTHOR_NAME)).willReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException(Author.class, "name", ERROR_AUTHOR_NAME);

        assertThatThrownBy(() -> service.getListBookByAuthorName(ERROR_AUTHOR_NAME)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());
    }

    @DisplayName("возвращать ошибку, если нет жанра по переданному имени")
    @Test
    void shouldReturnGenreNotFoundException() {
        given(genreDao.getByName(ERROR_GENRE_NAME)).willReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException(Genre.class, "name", ERROR_GENRE_NAME);

        assertThatThrownBy(() -> service.getListBookByGenreName(ERROR_GENRE_NAME)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());
    }
}