package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.domain.Author;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами книг должен")
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository repository;

    @DisplayName("сохранять автора в базе данных")
    @Test
    void shouldSaveAuthor() {
        Author authorAdded = new Author("Илья Ильф", LocalDate.of(1897, 10, 15));
        authorAdded = entityManager.persist(authorAdded);
        entityManager.flush();

        Optional<Author> authorFound = repository.findById(authorAdded.getId());

        assertThat(authorFound).isNotNull();
        assertThat(authorFound.get()).isEqualTo(authorAdded);
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnListAllAuthor() {
        Author authorAdded = new Author("Валентин Пикуль", LocalDate.of(1928, 07, 13));
        authorAdded = entityManager.persist(authorAdded);
        entityManager.flush();

        List<Author> authors = repository.findAll();

        assertThat(authors).isNotEmpty()
                .hasSize(1)
                .contains(authorAdded);
    }

    @DisplayName("возвращать автора по его имени")
    @Test
    void shouldReturnExpectedAuthorByName() {
        Author authorAdded = new Author("Александр Пушкин", LocalDate.of(1799, 06, 06));
        authorAdded = entityManager.persist(authorAdded);
        entityManager.flush();

        Optional<Author> authorFound = repository.findByName(authorAdded.getName());

        assertThat(authorFound).isNotNull();
        assertThat(authorFound.get()).isEqualTo(authorAdded);
    }

    @DisplayName("возвращать пустое значение, если автора не существует по заданному имени")
    @Test
    void shouldReturnNullIfGenreIsNotExistsByName() {
        Optional<Author> errorAuthor = repository.findByName("Александр Блок");

        assertThat(errorAuthor.isPresent()).isEqualTo(false);
    }

    @DisplayName("удалять автора из базы данных по идентификатору")
    @Test
    void shouldDeleteGenreById() {
        Author authorBeforeDelete = new Author("Виктор Гюго", LocalDate.of(1802, 02, 26));
        authorBeforeDelete = entityManager.persist(authorBeforeDelete);
        entityManager.flush();

        repository.deleteById(authorBeforeDelete.getId());

        Optional<Author> authorAfterDelete = repository.findById(authorBeforeDelete.getId());

        assertThat(authorAfterDelete).isEmpty();
    }

}