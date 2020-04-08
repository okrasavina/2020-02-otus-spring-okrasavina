package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с Author должно")
@JdbcTest
@Import(AuthorDaoImpl.class)
class AuthorDaoImplTest {

    public static final String DEFAULT_AUTHOR_NAME = "Илья Ильф";
    public static final long DEFAULT_AUTHOR_ID = 1L;
    public static final String INSERTED_AUTHOR_NAME = "Агния Барто";
    public static final long INSERTED_AUTHOR_ID = 3L;
    public static final String ERROR_AUTHOR_NAME = "Валентин Пикуль";
    public static final long SECOND_AUTHOR_ID = 2L;
    public static final String SECOND_AUTHOR_NAME = "Евгений Петров";
    public static final long DEFAULT_BOOK_ID = 1L;

    @Autowired
    private AuthorDaoImpl dao;

    @DisplayName("возвращать ожидаемого Author по его name")
    @Test
    void shouldReturnExpectedAuthorByName() {
        Author expectedAuthor = new Author(DEFAULT_AUTHOR_ID, DEFAULT_AUTHOR_NAME);
        Optional<Author> actualAuthor = dao.getByName(DEFAULT_AUTHOR_NAME);

        assertThat(actualAuthor.isPresent()).isEqualTo(true);
        assertThat(actualAuthor.get()).isEqualToComparingFieldByField(expectedAuthor);
    }

    @DisplayName("возвращать пустое значение, если Author не существует по заданному name")
    @Test
    void shouldReturnNullIfAuthorIsNotExistsByName() {
        Optional<Author> errorAuthor = dao.getByName(ERROR_AUTHOR_NAME);

        assertThat(errorAuthor.isPresent()).isEqualTo(false);
    }

    @DisplayName("корректно добавлять Author в БД")
    @Test
    void shouldCorrectInsertAuthor() {
        Author authorExpected = new Author(INSERTED_AUTHOR_NAME);
        dao.insert(authorExpected);

        assertThat(authorExpected.getId()).isEqualTo(INSERTED_AUTHOR_ID);
    }

    @DisplayName("возвращать список авторов по id книги")
    @Test
    void shouldReturnListAuthorsByBookId() {
        List<Author> listExpected = new ArrayList<>();
        listExpected.add(new Author(DEFAULT_AUTHOR_ID, DEFAULT_AUTHOR_NAME));
        listExpected.add(new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Author> listActual = dao.getListByBookId(DEFAULT_BOOK_ID);

        assertThat(listActual.size()).isEqualTo(listExpected.size());
        assertThat(listActual).hasSameElementsAs(listExpected);
    }

    @DisplayName("возвращать список всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        List<Author> listExpected = new ArrayList<>();
        listExpected.add(new Author(DEFAULT_AUTHOR_ID, DEFAULT_AUTHOR_NAME));
        listExpected.add(new Author(SECOND_AUTHOR_ID, SECOND_AUTHOR_NAME));
        List<Author> listActual = dao.getAll();

        assertThat(listActual.size()).isEqualTo(listExpected.size());
        assertThat(listActual).hasSameElementsAs(listExpected);
    }

    @DisplayName("удалять авторов без книг")
    @Test
    void shouldDeleteAuthorWithoutBooks() {
        List<Author> beforeInsert = dao.getAll();
        dao.insert(new Author(INSERTED_AUTHOR_NAME));
        dao.deleteAuthorsWithoutBooks();
        List<Author> afterDelete = dao.getAll();

        assertThat(afterDelete.size()).isEqualTo(beforeInsert.size());
        assertThat(afterDelete).hasSameElementsAs(beforeInsert);
    }

}