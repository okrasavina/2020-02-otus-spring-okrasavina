package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<Genre> getByName(String genreName) {
        return jdbcOperations.query("select * from genre where name = :name",
                Map.of("name", genreName), new GenreMapper()).stream().findFirst();
    }

    @Override
    public Genre insert(String genreName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", genreName);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcOperations.update("insert into genre(name) values(:name)", params, kh);
        return new Genre(kh.getKey().longValue(), genreName);
    }

    @Override
    public List<Genre> getListByBookId(long bookId) {
        return jdbcOperations.query("select g.* from book_genre_link l join genre g on g.id = l.genre_id where l.book_id = :book_id",
                Map.of("book_id", bookId), new GenreMapper());
    }

    @Override
    public void deleteGenresWithoutBooks() {
        jdbcOperations.update("delete from genre where not exists(select 1 from book_genre_link l where l.genre_id = genre.id)",
                Map.of());
    }

    @Override
    public List<Genre> getAll() {
        return jdbcOperations.query("select * from genre", new GenreMapper());
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
