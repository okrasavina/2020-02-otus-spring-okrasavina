package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с библиотекой должен")
@ExtendWith(MockitoExtension.class)
class LibraryServiceImplTest {

    public static final String BOOK_CREATE_MESSAGE = "The book was successfully added to the library.";
    private static final String BOOK_DELETE_MESSAGE = "The book number 1 was successfully deleted from the library.";
    public static final String INSERTED_BOOK_NAME = "Мертвые души";
    public static final String INSERTED_AUTHOR_NAME = "Гоголь";
    public static final String INSERTED_GENRE_NAME = "Комедия";
    public static final long DEFAULT_BOOK_ID = 1L;
    public static final String DEFAULT_BOOK_NAME = "12 стульев";
    public static final String FIRST_AUTHOR_NAME = "Илья Ильф";
    public static final String SECOND_AUTHOR_NAME = "Евгений Петров";
    public static final String DEFAULT_GENRE_NAME = "Комедия";
    private static final String BOOK_RETURN_MESSAGE = "The book number 1 was returned back to the library.";
    public static final String INSERTED_COMMENT_TEXT = "Это очень интересная книга.";
    public static final String BOOK_COMMENT_MESSAGE = "The comment was added to the book by number 1.";
    private static final String BOOK_COMMENT_CLEAR_MESSAGE = "The comments on the book number 1 was deleted.";
    public static final String DEFAULT_COMMENT_TEXT = "Это очень интересная книга";
    public static final String BOOK_TAKE_MESSAGE = "The book number 1 was taken to reading.";

    @Mock
    private MessageSource messageSource;
    @Mock
    private LocaleService localeService;
    @Mock
    private BookService bookService;

    private LibraryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new LibraryServiceImpl(localeService, messageSource, bookService);
    }

    @DisplayName("добавлять книгу в библотеку")
    @Test
    void shouldCreateLibraryBook() {
        when(messageSource.getMessage("book.create", null, Locale.ENGLISH))
                .thenReturn(BOOK_CREATE_MESSAGE);
        when(localeService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
        String messageExcepted = service.createLibraryBook(INSERTED_BOOK_NAME,
                List.of(INSERTED_AUTHOR_NAME), List.of(INSERTED_GENRE_NAME));

        assertThat(messageExcepted).isEqualTo(BOOK_CREATE_MESSAGE);
        verify(messageSource, times(1))
                .getMessage("book.create", null, Locale.ENGLISH);
        verify(localeService, times(1)).getCurrentLocale();
    }

    @DisplayName("возвращать список всех книг")
    @Test
    void shouldReturnExceptedListAllBooks() {
        List<String> expectedList = List.of(getDefaultBook().toString());
        when(bookService.getListBookInfo()).thenReturn(expectedList);

        List<String> actualList = service.getListBook();

        assertThat(actualList).hasSameElementsAs(expectedList);
        verify(bookService, times(1)).getListBookInfo();
    }

    @DisplayName("удалять книгу по номеру")
    @Test
    void shouldDeleteBookByNumber() {
        when(messageSource.getMessage("book.delete", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH))
                .thenReturn(BOOK_DELETE_MESSAGE);
        when(localeService.getCurrentLocale()).thenReturn(Locale.ENGLISH);

        String messageExcepted = service.deleteBookByNumber(DEFAULT_BOOK_ID);

        assertThat(messageExcepted).isEqualTo(BOOK_DELETE_MESSAGE);
        verify(messageSource, times(1))
                .getMessage("book.delete", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH);
        verify(localeService, times(1)).getCurrentLocale();
    }

    @DisplayName("возвращать сообщении о взятии книги по номеру на чтение")
    @Test
    void shouldReturnExceptedBookByNumber() {
        Book book = getDefaultBook();
        when(bookService.getBookByNumber(DEFAULT_BOOK_ID)).thenReturn(book);
        when(messageSource.getMessage("book.take", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH))
                .thenReturn(BOOK_TAKE_MESSAGE);
        when(localeService.getCurrentLocale()).thenReturn(Locale.ENGLISH);

        String messageActual = service.getBookByNumber(DEFAULT_BOOK_ID);

        assertThat(messageActual).isEqualTo(BOOK_TAKE_MESSAGE);
        verify(bookService, times(1)).getBookByNumber(DEFAULT_BOOK_ID);
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnListAllAuthors() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_NAME));
        when(bookService.getListAuthor()).thenReturn(authors.stream().map(Author::toString).collect(Collectors.toList()));

        List<String> listAuthor = service.getListAuthor();

        assertThat(listAuthor).hasSameElementsAs(authors.stream().map(Author::toString).collect(Collectors.toList()));
        verify(bookService, times(1)).getListAuthor();
    }

    @DisplayName("возвращать список книг по имени автора")
    @Test
    void shouldReturnExceptedListBooksByAuthorName() {
        String bookInfo = getDefaultBook().toString();
        when(bookService.getListBookInfoByAuthorName(FIRST_AUTHOR_NAME)).thenReturn(List.of(bookInfo));

        List<String> listActual = service.getListBookByAuthorName(FIRST_AUTHOR_NAME);

        assertThat(listActual).hasSameElementsAs(List.of(bookInfo));
        verify(bookService, times(1)).getListBookInfoByAuthorName(FIRST_AUTHOR_NAME);
    }

    @DisplayName("возвращать список всех жанров в БД")
    @Test
    void shouldReturnListAllGenres() {
        Genre genre = new Genre(DEFAULT_GENRE_NAME);
        when(bookService.getListGenres()).thenReturn(List.of(genre.toString()));

        List<String> listGenre = service.getListGenres();

        assertThat(listGenre).hasSameElementsAs(List.of(genre.toString()));
        verify(bookService, times(1)).getListGenres();
    }

    @DisplayName("возвращать список книг по имени жанра")
    @Test
    void shouldReturnExceptedListBooksByGenreName() {
        String bookInfo = getDefaultBook().toString();
        when(bookService.getListBookInfoByGenreName(DEFAULT_GENRE_NAME)).thenReturn(List.of(bookInfo));

        List<String> listActual = service.getListBookByGenreName(DEFAULT_GENRE_NAME);

        assertThat(listActual).hasSameElementsAs(List.of(bookInfo));
        verify(bookService, times(1)).getListBookInfoByGenreName(DEFAULT_GENRE_NAME);
    }

    @DisplayName("возвращать книгу в библиотеку")
    @Test
    void shouldReturnBookToTheLibrary() {
        Book book = getDefaultBook();

        when(bookService.getBookByNumber(DEFAULT_BOOK_ID)).thenReturn(book);
        service.getBookByNumber(DEFAULT_BOOK_ID);

        when(messageSource.getMessage("book.return", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH))
                .thenReturn(BOOK_RETURN_MESSAGE);
        when(localeService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
        String resultMessage = service.returnTheBook();

        assertThat(resultMessage).isEqualTo(BOOK_RETURN_MESSAGE);
        assertThat(service.getBook()).isNull();
        verify(messageSource, times(1)).getMessage("book.return",
                new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH);
        verify(localeService, times(2)).getCurrentLocale();
    }

    @DisplayName("добавлять новый комментарий к книге")
    @Test
    void shouldAddNewCommentToTheBook() {
        when(bookService.addCommentToTheBook(service.getBook(), INSERTED_COMMENT_TEXT)).thenReturn(DEFAULT_BOOK_ID);
        when(messageSource.getMessage("book.comment", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH))
                .thenReturn(BOOK_COMMENT_MESSAGE);
        when(localeService.getCurrentLocale()).thenReturn(Locale.ENGLISH);

        String resultMessage = service.addCommentToTheBook(INSERTED_COMMENT_TEXT);

        assertThat(resultMessage).isEqualTo(BOOK_COMMENT_MESSAGE);
        verify(bookService, times(1)).addCommentToTheBook(service.getBook(), INSERTED_COMMENT_TEXT);
        verify(messageSource, times(1)).getMessage("book.comment",
                new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH);
        verify(localeService, times(1)).getCurrentLocale();
    }

    @DisplayName("удалять комментарии у текущей книги")
    @Test
    void shouldDeleteCommentsOnTheBook() {
        Book book = getDefaultBook();

        when(bookService.getBookByNumber(DEFAULT_BOOK_ID)).thenReturn(book);
        service.getBookByNumber(DEFAULT_BOOK_ID);

        when(messageSource.getMessage("book.comments.clear", new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH))
                .thenReturn(BOOK_COMMENT_CLEAR_MESSAGE);
        when(localeService.getCurrentLocale()).thenReturn(Locale.ENGLISH);

        String resultMessage = service.clearCommentsOnTheBook();

        assertThat(resultMessage).isEqualTo(BOOK_COMMENT_CLEAR_MESSAGE);
        verify(bookService, times(1)).clearCommentsOnTheBook(service.getBook());
        verify(messageSource, times(1)).getMessage("book.comments.clear",
                new String[]{Long.toString(DEFAULT_BOOK_ID)}, Locale.ENGLISH);
        verify(localeService, times(2)).getCurrentLocale();
    }

    @DisplayName("возвращать список комментариев по номеру книги")
    @Test
    void shouldReturnExpectedListCommentByBookNumber() {
        Book book = getDefaultBook();
        List<String> listExpected = List.of(new Comment(DEFAULT_COMMENT_TEXT, book))
                .stream().map(Comment::toString).collect(Collectors.toList());

        when(bookService.getListCommentsByBookId(DEFAULT_BOOK_ID)).thenReturn(listExpected);

        List<String> listActual = service.getListCommentsByBook(DEFAULT_BOOK_ID);

        assertThat(listActual).isNotEmpty()
                .hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected);
        verify(bookService, times(1)).getListCommentsByBookId(DEFAULT_BOOK_ID);
    }

    private Book getDefaultBook() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_NAME));
        return new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME,
                authors, List.of(new Genre(DEFAULT_GENRE_NAME)));
    }

}