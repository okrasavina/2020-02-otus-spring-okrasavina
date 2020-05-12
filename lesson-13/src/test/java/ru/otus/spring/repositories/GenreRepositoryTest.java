package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.spring.config", "ru.otus.spring.repositories"})
@DisplayName("Репозиторий на основе Jpa для работы с жанрами книг должен")
class GenreRepositoryTest {

    public static final String DEFAULT_GENRE_NAME = "Комедия";
    public static final String ERROR_GENRE_NAME = "Роман";

    @Autowired
    private GenreRepository repo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("возвращать список всех жанров")
    @Test
    void shouldReturnListAllGenre() {
        List<Genre> listExpected = mongoTemplate.findAll(Genre.class);
        List<Genre> listActual = repo.findAll();

        assertThat(listActual).hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected);
    }

    @DisplayName("возвращать жанр по его имени")
    @Test
    void shouldReturnExpectedGenreByName() {
        Genre genreExpected = mongoTemplate.findOne(query(where("name").is(DEFAULT_GENRE_NAME)), Genre.class);
        Optional<Genre> genreActual = repo.findByName(DEFAULT_GENRE_NAME);

        assertThat(genreActual).isPresent()
                .get().isEqualTo(genreExpected);
    }

    @DisplayName("возвращать пустое значение, если Genre не существует по заданному имени")
    @Test
    void shouldReturnNullIfGenreIsNotExistsByName() {
        Optional<Genre> errorGenre = repo.findByName(ERROR_GENRE_NAME);

        assertThat(errorGenre.isPresent()).isEqualTo(false);
    }

}