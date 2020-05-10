package ru.otus.spring.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.spring.config", "ru.otus.spring.repositories"})
@DisplayName("Репозиторий на основе Jpa для работы с книгами должен")
class BookRepositoryTest {

    public static final String INSERTED_BOOK_NAME = "Мертвые души";
    public static final String INSERTED_AUTHOR_NAME = "Николай Гоголь";
    public static final String INSERTED_GENRE_NAME = "Роман";
    public static final String FIRST_AUTHOR_NAME = "Илья Ильф";
    public static final String DEFAULT_GENRE_NAME = "Комедия";
    public static final long DEFAULT_BOOK_ID = 1L;
    public static final long SEARCH_BOOK_ID = 3L;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("сохранять всю информацию о книге")
    @Test
    void shouldCorrectSaveAllBookInfo() {
        List<Author> authors = List.of(new Author(INSERTED_AUTHOR_NAME));
        List<Genre> genres = List.of(new Genre(INSERTED_GENRE_NAME));
        Book book = new Book(INSERTED_BOOK_NAME, authors, genres);

        book = bookRepo.save(book);

        Book actualBook = mongoTemplate.findById(book.getId(), Book.class);
        assertThat(actualBook).isNotNull().isEqualToComparingFieldByField(book);
    }

    @DisplayName("возвращать список всех книг с полной информацией по ним")
    @Test
    void shouldReturnListAllLibraryBooks() {
        val listExpected = mongoTemplate.findAll(Book.class);

        val listActual = bookRepo.findAll();
        assertThat(listActual).isNotNull().hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected);
    }

    @DisplayName("возвращать список книг по автору")
    @Test
    void shouldReturnExpectedListBooksByAuthor() {
        Author author = mongoTemplate.findOne(query(where("name").is(FIRST_AUTHOR_NAME)), Author.class);

        val listExpected = mongoTemplate.find(query(where("authors").all(author)), Book.class);

        val listActual = bookRepo.findAllByAuthorsContaining(author);
        assertThat(listActual).isNotNull().hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected);
    }

    @DisplayName("удалять книгу по ее идентификатору")
    @Test
    void shouldDeleteLibraryBookById() {
        Book bookBeforeDelete = mongoTemplate.findById(DEFAULT_BOOK_ID, Book.class);
        assertThat(bookBeforeDelete).isNotNull();

        bookRepo.deleteById(DEFAULT_BOOK_ID);
        Book bookAfterDelete = mongoTemplate.findById(DEFAULT_BOOK_ID, Book.class);
        assertThat(bookAfterDelete).isNull();
    }

    @DisplayName("возвращать книгу по ее id")
    @Test
    void shouldReturnExpectedBookById() {
        Book expected = mongoTemplate.findById(SEARCH_BOOK_ID, Book.class);
        Optional<Book> actual = bookRepo.findById(SEARCH_BOOK_ID);

        assertThat(actual.isPresent()).isEqualTo(true);
        assertThat(actual.get()).isEqualTo(expected);
    }

    @DisplayName("возвращать список книг по жанру")
    @Test
    void shouldReturnExpectedListBooksByGenre() {
        Genre genre = mongoTemplate.findOne(query(where("name").is(DEFAULT_GENRE_NAME)), Genre.class);

        val listExpected = mongoTemplate.find(query(where("genres").all(genre)), Book.class);

        val listActual = bookRepo.findAllByGenresContaining(genre);
        assertThat(listActual).isNotNull().hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected);
    }

}