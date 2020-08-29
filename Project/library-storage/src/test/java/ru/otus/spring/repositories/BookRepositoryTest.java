package ru.otus.spring.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами должен")
@DataJpaTest
class BookRepositoryTest {

  /*  private Book book;
    private Author author;
    private Genre genre;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository repository;

    @BeforeEach
    void setUp() {
        author = entityManager.persist(new Author("Николай Гоголь", LocalDate.of(1809, 03, 19)));
        genre = entityManager.persist(new Genre("Роман", "Литературный жанр"));
        book = entityManager.persist(new Book(0L, "Мертвые души", "Авантюра Чичикова",
                List.of(author), List.of(genre)));
        entityManager.flush();
    }

    @DisplayName("сохранять книгу в базе данных")
    @Test
    void shouldSaveBook() {
        Book bookAdded = getNewBook();

        Optional<Book> bookFound = repository.findById(bookAdded.getId());

        assertThat(bookFound).isNotNull();
        assertThat(bookFound.get()).isEqualTo(bookAdded);
    }

    @DisplayName("возвращать пустое значение, если книги не существует по заданному идентификатору")
    @Test
    void shouldReturnNullIfBookIsNotExistsById() {
        Optional<Book> errorBook = repository.findById(10L);

        assertThat(errorBook.isPresent()).isEqualTo(false);
    }

    @DisplayName("удалять книгу по переданному идентификатору")
    @Test
    void shouldDeleteBookById() {
        Optional<Book> bookBeforeDelete = repository.findById(book.getId());

        repository.deleteById(book.getId());

        Optional<Book> bookAfterDelete = repository.findById(book.getId());

        assertThat(bookBeforeDelete).isNotEmpty();
        assertThat(bookBeforeDelete.get()).isEqualTo(book);
        assertThat(bookAfterDelete).isEmpty();
    }

    @DisplayName("возвращать весь список книг из базы данных")
    @Test
    void shouldReturnExpectedListOfBooks() {
        Book bookAdded = getNewBook();

        List<Book> booksActual = repository.findAll();

        assertThat(booksActual).isNotEmpty().hasSize(2)
                .contains(book, bookAdded);
    }

    private Book getNewBook() {
        Book book = new Book(0L, "Вечера на Хуторе близ Диканьки", "Фантастические истории",
                List.of(author), List.of(genre));
        book = entityManager.persist(book);
        entityManager.flush();

        return book;
    }
*/
}