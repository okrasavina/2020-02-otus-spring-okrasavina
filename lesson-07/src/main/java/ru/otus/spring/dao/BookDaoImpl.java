package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Book insert(String titleBook, List<Author> authors, List<Genre> genres) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", titleBook);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcOperations.update("insert into book(title) values(:title)",
                params, kh);

        insertBookAuthorLink(kh.getKey().longValue(), authors);
        insertBookGenreLink(kh.getKey().longValue(), genres);

        return new Book(kh.getKey().longValue(), titleBook);
    }

    @Override
    public List<Book> getAll() {
        return jdbcOperations.query("select * from book", new BookMapper());
    }

    @Override
    public void deleteById(long id) {
        jdbcOperations.update("delete from book where id = :id", Map.of("id", id));
        deleteBookAuthorLinkByBookId(id);
        deleteBookGenreLinkByBookId(id);
    }

    @Override
    public Optional<Book> getById(long id) {
        return jdbcOperations.query("select * from book where id = :id", Map.of("id", id),
                new BookMapper()).stream().findFirst();
    }

    @Override
    public List<Book> getAllByAuthorId(long authorId) {
        return jdbcOperations.query("select b.* from book_author_link l join book b on b.id = l.book_id where author_id = :id",
                Map.of("id", authorId), new BookMapper());
    }

    @Override
    public List<Book> getAllByGenreId(long genreId) {
        return jdbcOperations.query("select b.* from book_genre_link l join book b on b.id = l.book_id where genre_id = :id",
                Map.of("id", genreId), new BookMapper());
    }

    private void deleteBookGenreLinkByBookId(long bookId) {
        jdbcOperations.update("delete from book_genre_link where book_id = :id", Map.of("id", bookId));
    }

    private void deleteBookAuthorLinkByBookId(long bookId) {
        jdbcOperations.update("delete from book_author_link where book_id = :id", Map.of("id", bookId));
    }

    private void insertBookGenreLink(long bookId, List<Genre> genres) {
        Map<String, Object>[] params = new Map[genres.size()];
        for (Genre genre : genres) {
            params[genres.indexOf(genre)] = Map.of("book_id", bookId, "genre_id", genre.getId());
        }
        jdbcOperations.batchUpdate("insert into book_genre_link(book_id, genre_id) values(:book_id, :genre_id)",
                params);
    }

    private void insertBookAuthorLink(long bookId, List<Author> authors) {
        Map<String, Object>[] params = new Map[authors.size()];
        for (Author author : authors) {
            params[authors.indexOf(author)] = Map.of("book_id", bookId, "author_id", author.getId());
        }
        jdbcOperations.batchUpdate("insert into book_author_link(book_id, author_id) values(:book_id, :author_id)",
                params);
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            return new Book(id, title);
        }
    }
}
