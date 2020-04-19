package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Book;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.repositories.GenreRepository;
import ru.otus.spring.repositories.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с Book должен")
@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {

    public static final String INSERTED_BOOK_NAME = "Мертвые души";
    public static final long INSERTED_BOOK_ID = 2L;
    public static final String INSERTED_AUTHOR_NAME = "Гоголь";
    public static final String INSERTED_GENRE_NAME = "Роман";
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
    public static final String DEFAULT_COMMENT_TEXT = "Это очень интересная книга.";

    @Configuration
    @ComponentScan("ru.otus.spring.service")
    private static class Config {
    }

    @MockBean
    private BookRepository bookRepo;
    @MockBean
    private AuthorRepository authorRepo;
    @MockBean
    private GenreRepository genreRepo;
    @MockBean
    private CommentRepository commentRepo;

    private BookServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BookServiceImpl(bookRepo, authorRepo, genreRepo, commentRepo);
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    void shouldCreateNewBook() {
        List<Author> authors = List.of(new Author(0, INSERTED_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(0, INSERTED_GENRE_NAME));

        Book libraryBook = new Book(INSERTED_BOOK_ID, INSERTED_BOOK_NAME, authors, genres, List.of());

        given(bookRepo.save(libraryBook)).willReturn(libraryBook);
        service.createBook(libraryBook);

        verify(bookRepo, times(1)).save(libraryBook);
    }

    @DisplayName("возвращать список всех книг в БД")
    @Test
    void shouldReturnListAllBooks() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<Book> expected = List.of(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of()));

        given(bookRepo.getAll()).willReturn(expected);

        List<Book> actual = service.getListBook();

        assertThat(actual).hasSameElementsAs(expected);
        verify(bookRepo, times(1)).getAll();
    }

    @DisplayName("вызывать необходимые методы Dao при удалении книги по номеру")
    @Test
    void shouldDeleteBookById() {
        service.deleteBookById(DEFAULT_BOOK_ID);

        verify(bookRepo, times(1)).deleteById(DEFAULT_BOOK_ID);
    }

    @DisplayName("возвращать книгу по ее id")
    @Test
    void shouldReturnExpectedBookById() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        Book book = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of());

        given(bookRepo.getById(DEFAULT_BOOK_ID)).willReturn(Optional.of(book));

        Book actual = service.getBookByNumber(DEFAULT_BOOK_ID);

        assertThat(actual.toString()).isEqualTo(book.toString());
        verify(bookRepo, times(1)).getById(DEFAULT_BOOK_ID);
    }

    @DisplayName("возвращать список авторов в БД")
    @Test
    void shouldReturnExpectedListAuthors() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<String> expected = authors.stream().map(Author::toString).collect(Collectors.toList());

        given(authorRepo.getAll()).willReturn(authors);
        List<String> listAuthor = service.getListAuthor();

        assertThat(listAuthor).hasSameSizeAs(expected)
                .hasSameElementsAs(expected);
        verify(authorRepo, times(1)).getAll();
    }

    @DisplayName("возвращать список книг по имени автора")
    @Test
    void shouldReturnExpectedListBookByAuthorName() {
        Author author = new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME);
        List<Author> authors = List.of(author, new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<Book> expected = List.of(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME,
                authors, genres, List.of()));

        given(authorRepo.getByName(FIRST_AUTHOR_NAME)).willReturn(Optional.of(author));
        given(bookRepo.getAllByAuthor(author)).willReturn(expected);

        List<Book> actual = service.getListBookByAuthorName(FIRST_AUTHOR_NAME);

        verify(authorRepo, times(1)).getByName(FIRST_AUTHOR_NAME);
        verify(bookRepo, times(1)).getAllByAuthor(author);
        assertThat(actual).hasSameElementsAs(expected);
    }

    @DisplayName("возвращать список жанров в БД")
    @Test
    void shouldReturnExpectedListGenres() {
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<String> expected = genres.stream().map(Genre::toString).collect(Collectors.toList());

        given(genreRepo.getAll()).willReturn(genres);
        List<String> listGenre = service.getListGenres();

        assertThat(listGenre).hasSameElementsAs(expected);
        verify(genreRepo, times(1)).getAll();
    }

    @DisplayName("возвращать список книг по названию жанра")
    @Test
    void shouldReturnExpectedListBooksByGenreName() {
        Genre genre = new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME);
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(genre);
        List<Book> expected = List.of(new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of()));

        given(genreRepo.getByName(DEFAULT_GENRE_NAME)).willReturn(Optional.of(genre));
        given(bookRepo.getAllByGenre(genre)).willReturn(expected);

        List<Book> actual = service.getListBookByGenreName(DEFAULT_GENRE_NAME);

        verify(genreRepo, times(1)).getByName(DEFAULT_GENRE_NAME);
        verify(bookRepo, times(1)).getAllByGenre(genre);
        assertThat(actual).hasSameElementsAs(expected);
    }

    @DisplayName("возвращать ошибку, если нет автора по переданному имени")
    @Test
    void shouldReturnAuthorNotFoundException() {
        given(authorRepo.getByName(ERROR_AUTHOR_NAME)).willReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException(Author.class, "name", ERROR_AUTHOR_NAME);

        assertThatThrownBy(() -> service.getListBookByAuthorName(ERROR_AUTHOR_NAME)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());
    }

    @DisplayName("возвращать ошибку, если нет жанра по переданному имени")
    @Test
    void shouldReturnGenreNotFoundException() {
        given(genreRepo.getByName(ERROR_GENRE_NAME)).willReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException(Genre.class, "name", ERROR_GENRE_NAME);

        assertThatThrownBy(() -> service.getListBookByGenreName(ERROR_GENRE_NAME)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());
    }

    @DisplayName("добавлять комментарий к книге")
    @Test
    void shouldAddNewCommentToTheBook() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        Book book = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of());

        Comment comment = new Comment(0, DEFAULT_COMMENT_TEXT, book);

        given(commentRepo.save(comment)).willReturn(comment);
        service.addCommentToTheBook(book, DEFAULT_COMMENT_TEXT);

        verify(commentRepo, times(1)).save(comment);
    }

    @DisplayName("удалять комментарии по книге")
    @Test
    void shouldDeleteCommentsByBook() {
        Book book = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, List.of(), List.of(), List.of());
        service.clearCommentsOnTheBook(book);

        verify(commentRepo, times(1)).deleteCommentsByBook(book);
    }
}