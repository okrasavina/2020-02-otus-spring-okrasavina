package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Сервис по работе с библиотекой должен")
@SpringBootTest(classes = LibraryServiceImpl.class)
class LibraryServiceImplTest {

    public static final String BOOK_CREATE_MESSAGE = "The book was successfully added to the library.";
    private static final String BOOK_DELETE_MESSAGE = "The book number 1 was successfully deleted from the library.";
    public static final String INSERTED_BOOK_NAME = "Мертвые души";
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
    private static final String BOOK_RETURN_MESSAGE = "The book number 1 was returned back to the library.";
    public static final String DEFAULT_COMMENT_TEXT = "Это очень интересная книга.";
    public static final String BOOK_COMMENT_MESSAGE = "The comment was added to the book by number 1.";
    private static final String BOOK_COMMENT_CLEAR_MESSAGE = "The comments on the book number 1 was deleted.";

    @Configuration
    @ComponentScan("ru.otus.spring.service")
    private static class Config {
    }

    @MockBean
    private MessageSource messageSource;
    @MockBean
    private LocaleService localeService;
    @MockBean
    private BookService bookService;

    private LibraryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new LibraryServiceImpl(localeService, messageSource, bookService);
        given(localeService.getCurrentLocale()).willReturn(Locale.ENGLISH);
    }

    @DisplayName("добавлять книгу в библотеку")
    @Test
    void shouldCreateLibraryBook() {
        given(messageSource.getMessage("book.create", null, Locale.ENGLISH)).willReturn(BOOK_CREATE_MESSAGE);
        String messageExcepted = service.createLibraryBook(INSERTED_BOOK_NAME,
                List.of(INSERTED_AUTHOR_NAME), List.of(INSERTED_GENRE_NAME));

        assertThat(messageExcepted).isEqualTo(BOOK_CREATE_MESSAGE);
        verify(messageSource, times(1)).getMessage("book.create", null, Locale.ENGLISH);
    }

    @DisplayName("возвращать список всех книг")
    @Test
    void shouldReturnExceptedListAllBooks() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        Book libraryBook = new Book(DEFAULT_BOOK_ID, DEFAULT_GENRE_NAME,
                authors, List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME)), List.of());
        given(bookService.getListBook()).willReturn(List.of(libraryBook));

        List<String> listBook = service.getListBook();

        assertThat(listBook).hasSameElementsAs(List.of(libraryBook.toString()));
        verify(bookService, times(1)).getListBook();
    }

    @DisplayName("удалять книгу по номеру")
    @Test
    void shouldDeleteBookByNumber() {
        given(messageSource.getMessage("book.delete", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH))
                .willReturn(BOOK_DELETE_MESSAGE);
        String messageExcepted = service.deleteBookByNumber(DEFAULT_BOOK_ID);

        assertThat(messageExcepted).isEqualTo(BOOK_DELETE_MESSAGE);
        verify(messageSource, times(1))
                .getMessage("book.delete", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH);
    }

    @DisplayName("возвращать книгу по номеру")
    @Test
    void shouldReturnExceptedBookByNumber() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        Book libraryBook = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME,
                authors, List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME)), List.of());
        given(bookService.getBookByNumber(DEFAULT_BOOK_ID)).willReturn(libraryBook);

        String bookActual = service.getBookByNumber(DEFAULT_BOOK_ID);

        assertThat(bookActual.toString()).isEqualTo(libraryBook.toString());
        verify(bookService, times(1)).getBookByNumber(DEFAULT_BOOK_ID);
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnListAllAuthors() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        given(bookService.getListAuthor()).willReturn(authors.stream().map(Author::toString).collect(Collectors.toList()));

        List<String> listAuthor = service.getListAuthor();

        assertThat(listAuthor).hasSameElementsAs(authors.stream().map(Author::toString).collect(Collectors.toList()));
        verify(bookService, times(1)).getListAuthor();
    }

    @DisplayName("возвращать список книг по имени автора")
    @Test
    void shouldReturnExceptedListBooksByAuthorName() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        Book libraryBook = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME,
                authors, List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME)), List.of());
        given(bookService.getListBookByAuthorName(FIRST_AUTHOR_NAME)).willReturn(List.of(libraryBook));

        List<String> listActual = service.getListBookByAuthorName(FIRST_AUTHOR_NAME);

        assertThat(listActual).hasSameElementsAs(List.of(libraryBook.toString()));
        verify(bookService, times(1)).getListBookByAuthorName(FIRST_AUTHOR_NAME);
    }

    @DisplayName("возвращать список всех жанров в БД")
    @Test
    void shouldReturnListAllGenres() {
        Genre genre = new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME);
        given(bookService.getListGenres()).willReturn(List.of(genre.toString()));

        List<String> listGenre = service.getListGenres();

        assertThat(listGenre).hasSameElementsAs(List.of(genre.toString()));
        verify(bookService, times(1)).getListGenres();
    }

    @DisplayName("возвращать список книг по имени жанра")
    @Test
    void shouldReturnExceptedListBooksByGenreName() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        Book libraryBook = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME,
                authors, List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME)), List.of());
        given(bookService.getListBookByGenreName(DEFAULT_GENRE_NAME)).willReturn(List.of(libraryBook));

        List<String> listActual = service.getListBookByGenreName(DEFAULT_GENRE_NAME);

        assertThat(listActual).hasSameElementsAs(List.of(libraryBook.toString()));
        verify(bookService, times(1)).getListBookByGenreName(DEFAULT_GENRE_NAME);
    }

    @DisplayName("возвращать книгу в библиотеку")
    @Test
    void shouldReturnBookToTheLibrary() {
        Book libraryBook = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME,
                List.of(), List.of(), List.of());

        given(bookService.getBookByNumber(DEFAULT_BOOK_ID)).willReturn(libraryBook);
        service.getBookByNumber(DEFAULT_BOOK_ID);

        given(messageSource.getMessage("book.return", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH))
                .willReturn(BOOK_RETURN_MESSAGE);
        String resultMessage = service.returnTheBook();

        assertThat(resultMessage).isEqualTo(BOOK_RETURN_MESSAGE);
        assertThat(service.getBook()).isNull();
        verify(messageSource, times(1)).getMessage("book.return",
                new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH);
    }

    @DisplayName("добавлять новый комментарий к книге")
    @Test
    void shouldAddNewCommentToTheBook() {
        given(bookService.addCommentToTheBook(service.getBook(), DEFAULT_COMMENT_TEXT)).willReturn(DEFAULT_BOOK_ID);
        given(messageSource.getMessage("book.comment", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH))
                .willReturn(BOOK_COMMENT_MESSAGE);

        String resultMessage = service.addCommentToTheBook(DEFAULT_COMMENT_TEXT);

        assertThat(resultMessage).isEqualTo(BOOK_COMMENT_MESSAGE);
        verify(bookService, times(1)).addCommentToTheBook(service.getBook(), DEFAULT_COMMENT_TEXT);
        verify(messageSource, times(1)).getMessage("book.comment",
                new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH);
    }

    @DisplayName("удалять комментарии у текущей книги")
    @Test
    void shouldDeleteCommentsOnTheBook() {
        Book libraryBook = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME,
                List.of(), List.of(), List.of());

        given(bookService.getBookByNumber(DEFAULT_BOOK_ID)).willReturn(libraryBook);
        service.getBookByNumber(DEFAULT_BOOK_ID);

        given(messageSource.getMessage("book.comments.clear", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH))
                .willReturn(BOOK_COMMENT_CLEAR_MESSAGE);

        String resultMessage = service.clearCommentsOnTheBook();

        assertThat(resultMessage).isEqualTo(BOOK_COMMENT_CLEAR_MESSAGE);
        verify(bookService, times(1)).clearCommentsOnTheBook(service.getBook());
        verify(messageSource, times(1)).getMessage("book.comments.clear",
                new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH);
    }
}