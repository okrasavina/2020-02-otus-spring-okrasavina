package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.spring.config", "ru.otus.spring.repositories"})
@DisplayName("Репозиторий на основе Jpa для работы с авторами книг должен")
class AuthorRepositoryTest {

    public static final String FIRST_AUTHOR_NAME = "Илья Ильф";
    public static final String SECOND_AUTHOR_NAME = "Евгений Петров";
    public static final String ERROR_AUTHOR_NAME = "Валентин Пикуль";

    @Autowired
    private AuthorRepository repo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnListAllAuthors() {
        List<Author> listExpected = mongoTemplate.findAll(Author.class);
        List<Author> listActual = repo.findAll();

        assertThat(listActual).hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected);
    }

    @DisplayName("возвращать автора по его имени")
    @Test
    void shouldReturnExpectedAuthorByName() {
        Author authorExpected = mongoTemplate.findOne(query(where("name").is(FIRST_AUTHOR_NAME)), Author.class);
        Optional<Author> authorActual = repo.findByName(FIRST_AUTHOR_NAME);

        assertThat(authorActual).isPresent()
                .get().isEqualTo(authorExpected);
    }

    @DisplayName("возвращать пустое значение, если Author не существует по заданному имени")
    @Test
    void shouldReturnNullIfAuthorIsNotExistsByName() {
        Optional<Author> errorAuthor = repo.findByName(ERROR_AUTHOR_NAME);

        assertThat(errorAuthor.isPresent()).isEqualTo(false);
    }

    @DisplayName("возвращать список авторов по переданному списку имен")
    @Test
    void shouldReturnListExpectedAuthorsByListName() {
        List<String> names = List.of(FIRST_AUTHOR_NAME, SECOND_AUTHOR_NAME);
        List<Author> listActual = repo.findByNameIn(names);

        assertThat(listActual).isNotEmpty()
                .hasSameSizeAs(names)
                .allMatch(a -> a.getName().equals(names.get(listActual.indexOf(a))));
    }

}