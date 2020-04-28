package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с Book должен")
@ExtendWith(MockitoExtension.class)
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
    public static final String INSERTED_COMMENT_TEXT = "Это очень интересная книга.";
    public static final String DEFAULT_COMMENT_TEXT = "Нужно сдать до праздников.";
    public static final long DEFAULT_COMMENT_ID = 1L;

    @Mock
    private BookRepository bookRepo;
    @Mock
    private AuthorRepository authorRepo;
    @Mock
    private GenreRepository genreRepo;
    @Mock
    private CommentRepository commentRepo;

    private BookServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BookServiceImpl(bookRepo, authorRepo, genreRepo, commentRepo);
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    void shouldCreateNewBook() {
        List<Author> authors = List.of(new Author(INSERTED_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(INSERTED_GENRE_NAME));

        Book libraryBook = new Book(INSERTED_BOOK_ID, INSERTED_BOOK_NAME, authors, genres, List.of());

        when(bookRepo.save(libraryBook)).thenReturn(libraryBook);
        service.createBook(libraryBook);

        verify(bookRepo, times(2)).save(libraryBook);
    }

    @DisplayName("возвращать список всех книг в БД")
    @Test
    void shouldReturnListAllBooks() {
        List<Book> listBook = List.of(getDefaultBook());

        when(bookRepo.findAll()).thenReturn(listBook);

        List<String> actual = service.getListBookInfo();

        assertThat(actual).hasSameSizeAs(listBook)
                .hasSameElementsAs(listBook.stream().map(Book::toString).collect(Collectors.toList()));
        verify(bookRepo, times(1)).findAll();
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
        Book book = getDefaultBook();

        when(bookRepo.findById(DEFAULT_BOOK_ID)).thenReturn(Optional.of(book));

        Book bookActual = service.getBookByNumber(DEFAULT_BOOK_ID);

        assertThat(bookActual).isEqualTo(book);
        verify(bookRepo, times(1)).findById(DEFAULT_BOOK_ID);
    }

    @DisplayName("возвращать список авторов в БД")
    @Test
    void shouldReturnExpectedListAuthors() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<String> expected = authors.stream().map(Author::toString).collect(Collectors.toList());

        when(authorRepo.findAll()).thenReturn(authors);
        List<String> listAuthor = service.getListAuthor();

        assertThat(listAuthor).hasSameSizeAs(expected)
                .hasSameElementsAs(expected);
        verify(authorRepo, times(1)).findAll();
    }

    @DisplayName("возвращать список книг по имени автора")
    @Test
    void shouldReturnExpectedListBookByAuthorName() {
        Author author = new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME);
        List<Book> expected = List.of(getDefaultBook());

        when(authorRepo.findByName(FIRST_AUTHOR_NAME)).thenReturn(Optional.of(author));
        when(bookRepo.findAllByAuthorsContaining(author)).thenReturn(expected);

        List<String> actualList = service.getListBookInfoByAuthorName(FIRST_AUTHOR_NAME);

        verify(authorRepo, times(1)).findByName(FIRST_AUTHOR_NAME);
        verify(bookRepo, times(1)).findAllByAuthorsContaining(author);
        assertThat(actualList).hasSameSizeAs(expected)
                .hasSameElementsAs(expected.stream().map(Book::toString).collect(Collectors.toList()));
    }

    @DisplayName("возвращать список жанров в БД")
    @Test
    void shouldReturnExpectedListGenres() {
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        List<String> expected = genres.stream().map(Genre::toString).collect(Collectors.toList());

        when(genreRepo.findAll()).thenReturn(genres);
        List<String> listGenre = service.getListGenres();

        assertThat(listGenre).hasSameElementsAs(expected);
        verify(genreRepo, times(1)).findAll();
    }

    @DisplayName("возвращать список книг по названию жанра")
    @Test
    void shouldReturnExpectedListBooksByGenreName() {
        Genre genre = new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME);
        List<Book> expected = List.of(getDefaultBook());

        when(genreRepo.findByName(DEFAULT_GENRE_NAME)).thenReturn(Optional.of(genre));
        when(bookRepo.findAllByGenresContaining(genre)).thenReturn(expected);

        List<String> actualList = service.getListBookInfoByGenreName(DEFAULT_GENRE_NAME);

        verify(genreRepo, times(1)).findByName(DEFAULT_GENRE_NAME);
        verify(bookRepo, times(1)).findAllByGenresContaining(genre);
        assertThat(actualList).hasSameSizeAs(expected)
                .hasSameElementsAs(expected.stream().map(Book::toString).collect(Collectors.toList()));
    }

    @DisplayName("возвращать ошибку, если нет автора по переданному имени")
    @Test
    void shouldReturnAuthorNotFoundException() {
        when(authorRepo.findByName(ERROR_AUTHOR_NAME)).thenReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException(Author.class, "name", ERROR_AUTHOR_NAME);

        assertThatThrownBy(() -> service.getListBookInfoByAuthorName(ERROR_AUTHOR_NAME)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());
    }

    @DisplayName("возвращать ошибку, если нет жанра по переданному имени")
    @Test
    void shouldReturnGenreNotFoundException() {
        when(genreRepo.findByName(ERROR_GENRE_NAME)).thenReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException(Genre.class, "name", ERROR_GENRE_NAME);

        assertThatThrownBy(() -> service.getListBookInfoByGenreName(ERROR_GENRE_NAME)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());
    }

    @DisplayName("добавлять комментарий к книге")
    @Test
    void shouldAddNewCommentToTheBook() {
        Book book = getDefaultBook();
        Comment comment = new Comment(0L, INSERTED_COMMENT_TEXT, book);
        when(commentRepo.save(comment)).thenReturn(comment);

        long bookId = service.addCommentToTheBook(book, INSERTED_COMMENT_TEXT);

        assertThat(bookId).isEqualTo(book.getId());
        verify(commentRepo, times(1)).save(comment);
    }

    @DisplayName("удалять комментарии по книге")
    @Test
    void shouldDeleteCommentsByBook() {
        Book book = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME);
        service.clearCommentsOnTheBook(book);

        verify(commentRepo, times(1)).deleteAllByBook(book);
    }

    @DisplayName("возвращать список комментариев по книге")
    @Test
    void shouldReturnExpectedListCommentsByBook() {
        Book book = getDefaultBook();
        List<Comment> listExpected = List.of(new Comment(DEFAULT_COMMENT_ID, DEFAULT_COMMENT_TEXT, book));
        book.setComments(listExpected);
        when(bookRepo.findById(DEFAULT_BOOK_ID)).thenReturn(Optional.of(book));

        List<String> listActual = service.getListCommentsByBookId(DEFAULT_BOOK_ID);

        assertThat(listActual).isNotEmpty()
                .hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected.stream().map(Comment::toString).collect(Collectors.toList()));
        verify(bookRepo, times(1)).findById(DEFAULT_BOOK_ID);
    }

    private Book getDefaultBook() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        return new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of());
    }

}