package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.domain.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами книг должен")
@DataJpaTest
class AuthorRepositoryJpaTest {

    public static final String FIRST_AUTHOR_NAME = "Илья Ильф";
    public static final long FIRST_AUTHOR_ID = 1L;
    public static final long SECOND_AUTHOR_ID = 2L;
    public static final String SECOND_AUTHOR_NAME = "Евгений Петров";
    public static final long THIRD_AUTHOR_ID = 3L;
    public static final String THIRD_AUTHOR_NAME = "Сергей Есенин";
    public static final String ERROR_AUTHOR_NAME = "Валентин Пикуль";

    @Autowired
    private AuthorRepository repo;

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnListAllAuthors() {
        List<Author> listExpected = new ArrayList<>();
        listExpected.add(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME));
        listExpected.add(new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        listExpected.add(new Author(THIRD_AUTHOR_ID, THIRD_AUTHOR_NAME));
        List<Author> listActual = repo.findAll();

        assertThat(listActual).hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected);
    }

    @DisplayName("возвращать автора по его имени")
    @Test
    void shouldReturnExpectedAuthorByName() {
        Author authorExpected = new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME);
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

    @DisplayName("удалять авторов без книг")
    @Test
    void shouldDeleteAuthorWithoutBooks() {
        List<Author> beforeDelete = repo.findAll();
        repo.deleteAuthorWithoutBooks();
        List<Author> afterDelete = repo.findAll();

        assertThat(afterDelete).isNotEmpty().hasSizeLessThan(beforeDelete.size())
                .doesNotContain(beforeDelete.get(2));
    }

    @DisplayName("возвращать список авторов по переданному списку имен")
    @Test
    void shouldReturnListExpectedAuthorsByListName() {
        List<Author> listExpected = List.of(new Author(FIRST_AUTHOR_ID, FIRST_AUTHOR_NAME),
                new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Author> listActual = repo.findByNameIn(List.of(FIRST_AUTHOR_NAME, SECOND_AUTHOR_NAME));

        assertThat(listActual).isNotEmpty()
                .hasSameSizeAs(listExpected)
                .hasSameElementsAs(listExpected);
    }

}