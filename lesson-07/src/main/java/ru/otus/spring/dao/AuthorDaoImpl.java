package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<Author> getByName(String authorName) {
        return jdbcOperations.query("select * from author where name = :name",
                Map.of("name", authorName), new AuthorMapper()).stream().findFirst();
    }

    @Override
    public Author insert(String authorName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", authorName);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcOperations.update("insert into author(name) values(:name)",
                params, kh);
        return new Author(kh.getKey().longValue(), authorName);
    }

    @Override
    public List<Author> getListByBookId(long bookId) {
        return jdbcOperations.query("select a.* from book_author_link l join author a on a.id = l.author_id where l.book_id = :book_id",
                Map.of("book_id", bookId), new AuthorMapper());
    }

    @Override
    public List<Author> getAll() {
        return jdbcOperations.query("select * from author", new AuthorMapper());
    }

    @Override
    public void deleteAuthorsWithoutBooks() {
        jdbcOperations.update("delete from author where not exists(select 1 from book_author_link l where l.author_id = author.id)",
                Map.of());
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Author(id, name);
        }
    }
}
