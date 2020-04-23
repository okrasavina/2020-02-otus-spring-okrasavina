package ru.otus.spring.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами должен")
@DataJpaTest
class BookRepositoryJpaTest {

    public static final String INSERTED_BOOK_NAME = "Мертвые души";
    public static final String INSERTED_AUTHOR_NAME = "Николай Гоголь";
    public static final String INSERTED_GENRE_NAME = "Роман";
    public static final String FIRST_AUTHOR_NAME = "Илья Ильф";
    public static final long FIRST_AUTHOR_ID = 1L;
    public static final long SECOND_AUTHOR_ID = 2L;
    public static final String SECOND_AUTHOR_NAME = "Евгений Петров";
    public static final long DEFAULT_GENRE_ID = 1L;
    public static final String DEFAULT_GENRE_NAME = "Комедия";
    public static final long DEFAULT_BOOK_ID = 1L;
    public static final String DEFAULT_BOOK_NAME = "12 стульев";

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private TestEntityManager em;

    @DisplayName("сохранять всю информацию о книге")
    @Test
    void shouldCorrectSaveAllBookInfo() {
        List<Author> authors = List.of(new Author(0, INSERTED_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(0, INSERTED_GENRE_NAME));
        Book book = new Book(0, INSERTED_BOOK_NAME, authors, genres, List.of());

        bookRepo.save(book);
        assertThat(book.getId()).isGreaterThan(0);

        Book actualBook = em.find(Book.class, book.getId());
        assertThat(actualBook).isNotNull().isEqualToComparingFieldByField(book);
    }

    @DisplayName("возвращать список всех книг с полной информацией по ним")
    @Test
    void shouldReturnListAllLibraryBooks() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME), new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        Book book = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of());
        val listExpected = List.of(book);

        val listActual = bookRepo.findAll();
        assertThat(listActual).isNotNull().hasSameSizeAs(listExpected)
                .allMatch(b -> b.getId() == DEFAULT_BOOK_ID)
                .allMatch(b -> b.getTitle().equals(DEFAULT_BOOK_NAME))
                .allMatch(b -> b.getAuthors().size() == authors.size())
                .allMatch(b -> b.getAuthors().containsAll(authors))
                .allMatch(b -> b.getGenres().size() == genres.size())
                .allMatch(b -> b.getGenres().containsAll(genres));
    }

    @DisplayName("возвращать список книг по автору")
    @Test
    void shouldReturnExpectedListBooksByAuthor() {
        Author author = new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME);
        List<Author> authors = List.of(author, new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));
        Book book = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of());
        val listExpected = List.of(book);

        val listActual = bookRepo.findAllByAuthorsContaining(author);
        assertThat(listActual).isNotNull().hasSameSizeAs(listExpected)
                .allMatch(b -> b.getId() == DEFAULT_BOOK_ID)
                .allMatch(b -> b.getTitle().equals(DEFAULT_BOOK_NAME))
                .allMatch(b -> b.getAuthors().size() == authors.size())
                .allMatch(b -> b.getAuthors().containsAll(authors))
                .allMatch(b -> b.getGenres().size() == genres.size())
                .allMatch(b -> b.getGenres().containsAll(genres));
    }

    @DisplayName("удалять книгу по ее идентификатору")
    @Test
    void shouldDeleteLibraryBookById() {
        Book book = em.find(Book.class, DEFAULT_BOOK_ID);
        assertThat(book).isNotNull();
        em.detach(book);

        bookRepo.deleteById(DEFAULT_BOOK_ID);
        Book bookDeleted = em.find(Book.class, DEFAULT_BOOK_ID);
        assertThat(bookDeleted).isNull();
    }

    @DisplayName("возвращать книгу по ее id")
    @Test
    void shouldReturnExpectedBookById() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME));

        Book expected = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, genres, List.of());
        Optional<Book> actual = bookRepo.findById(DEFAULT_BOOK_ID);

        assertThat(actual.isPresent()).isEqualTo(true);
        assertThat(actual.get()).matches(b -> b.getId() == expected.getId())
                .matches(b -> b.getTitle().equals(expected.getTitle()))
                .matches(b -> b.getAuthors().size() == expected.getAuthors().size())
                .matches(b -> b.getAuthors().containsAll(expected.getAuthors()))
                .matches(b -> b.getGenres().size() == expected.getGenres().size())
                .matches(b -> b.getGenres().containsAll(expected.getGenres()));
    }

    @DisplayName("возвращать список книг по жанру")
    @Test
    void shouldReturnExpectedListBooksByGenre() {
        List<Author> authors = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        Genre genre = new Genre(DEFAULT_GENRE_ID, DEFAULT_GENRE_NAME);
        List<Genre> genres = List.of(genre);
        Book book = new Book(DEFAULT_BOOK_ID, DEFAULT_BOOK_NAME, authors, List.of(genre), List.of());
        val listExpected = List.of(book);

        val listActual = bookRepo.findAllByGenresContaining(genre);
        assertThat(listActual).isNotNull().hasSameSizeAs(listExpected)
                .allMatch(b -> b.getId() == DEFAULT_BOOK_ID)
                .allMatch(b -> b.getTitle().equals(DEFAULT_BOOK_NAME))
                .allMatch(b -> b.getAuthors().size() == authors.size())
                .allMatch(b -> b.getAuthors().containsAll(authors))
                .allMatch(b -> b.getGenres().size() == genres.size())
                .allMatch(b -> b.getGenres().containsAll(genres));
    }

}