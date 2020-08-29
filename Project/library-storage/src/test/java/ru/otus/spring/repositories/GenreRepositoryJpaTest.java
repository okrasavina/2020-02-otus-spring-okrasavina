package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами книг должен")
@DataJpaTest
class GenreRepositoryJpaTest {

  /*  @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GenreRepository repository;

    @DisplayName("сохранять жанр в базе данных")
    @Test
    void shouldSaveGenre() {
        Genre genreAdded = new Genre("Комедия", "Литературный жанр");
        genreAdded = entityManager.persist(genreAdded);
        entityManager.flush();

        Optional<Genre> genreFound = repository.findById(genreAdded.getId());

        assertThat(genreFound).isNotNull();
        assertThat(genreFound.get()).isEqualTo(genreAdded);
    }

    @DisplayName("возвращать список всех жанров")
    @Test
    void shouldReturnListAllGenre() {
        Genre genreAdded = new Genre("Трагедия", "Еще один жанр");
        genreAdded = entityManager.persist(genreAdded);
        entityManager.flush();

        List<Genre> genres = repository.findAll();

        assertThat(genres).isNotEmpty()
                .hasSize(1)
                .contains(genreAdded);
    }

    @DisplayName("возвращать жанр по его имени")
    @Test
    void shouldReturnExpectedGenreByName() {
        Genre genreAdded = new Genre("Роман", "Еще один жанр");
        genreAdded = entityManager.persist(genreAdded);
        entityManager.flush();

        Optional<Genre> genreFound = repository.findByName(genreAdded.getName());

        assertThat(genreFound).isNotNull();
        assertThat(genreFound.get()).isEqualTo(genreAdded);
    }

    @DisplayName("возвращать пустое значение, если жанра не существует по заданному имени")
    @Test
    void shouldReturnNullIfGenreIsNotExistsByName() {
        Optional<Genre> errorGenre = repository.findByName("Сатира");

        assertThat(errorGenre.isPresent()).isEqualTo(false);
    }

    @DisplayName("удалять жанр из базы данных по идентификатору")
    @Test
    void shouldDeleteGenreById() {
        Genre genreBeforeDelete = new Genre("Исторический роман", "Литературный жанр");
        genreBeforeDelete = entityManager.persist(genreBeforeDelete);
        entityManager.flush();

        repository.deleteById(genreBeforeDelete.getId());

        Optional<Genre> genreAfterDelete = repository.findById(genreBeforeDelete.getId());

        assertThat(genreAfterDelete).isEmpty();
    }*/
}