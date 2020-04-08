package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryBook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public void createBook(LibraryBook libraryBook, List<String> authorNames, List<String> genreNames) {
        libraryBook.setAuthors(getListAuthorByListName(authorNames));
        libraryBook.setGenres(getListGenreByListName(genreNames));

        bookDao.insert(libraryBook.getBook(), libraryBook.getAuthors(), libraryBook.getGenres());
    }

    @Override
    public List<LibraryBook> getListBook() {
        List<Book> books = bookDao.getAll();
        return getListLibraryBookByBooks(books);
    }

    @Override
    public void deleteBookById(long bookId) {
        bookDao.deleteById(bookId);
        authorDao.deleteAuthorsWithoutBooks();
        genreDao.deleteGenresWithoutBooks();
    }

    @Override
    public String getBookByNumber(long bookId) {
        Book book = bookDao.getById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(Book.class, "number", Long.toString(bookId)));
        return getListLibraryBookByBooks(List.of(book)).get(0).toString();
    }

    @Override
    public List<String> getListAuthor() {
        List<Author> authors = authorDao.getAll();
        return authors.stream().map(Author::toString).collect(Collectors.toList());
    }

    @Override
    public List<LibraryBook> getListBookByAuthorName(String authorName) {
        Author author = authorDao.getByName(authorName)
                .orElseThrow(() -> new EntityNotFoundException(Author.class, "name", authorName));
        List<Book> books = bookDao.getAllByAuthorId(author.getId());
        return getListLibraryBookByBooks(books);
    }

    @Override
    public List<String> getListGenres() {
        List<Genre> genres = genreDao.getAll();
        return genres.stream().map(Genre::toString).collect(Collectors.toList());
    }

    @Override
    public List<LibraryBook> getListBookByGenreName(String genreName) {
        Genre genre = genreDao.getByName(genreName)
                .orElseThrow(() -> new EntityNotFoundException(Genre.class, "name", genreName));
        List<Book> books = bookDao.getAllByGenreId(genre.getId());
        return getListLibraryBookByBooks(books);
    }

    private List<Author> getListAuthorByListName(List<String> authorNames) {
        List<Author> authorsResult = new ArrayList<>();
        for (String authorName : authorNames) {
            Author author = authorDao.getByName(authorName.trim())
                    .orElseGet(() -> authorDao.insert(new Author(authorName.trim())));
            authorsResult.add(author);
        }

        return authorsResult;
    }

    private List<Genre> getListGenreByListName(List<String> genreNames) {
        List<Genre> genresResult = new ArrayList<>();
        for (String genreName : genreNames) {
            Genre genre = genreDao.getByName(genreName.trim())
                    .orElseGet(() -> genreDao.insert(new Genre(genreName.trim())));
            genresResult.add(genre);
        }
        return genresResult;
    }

    private List<LibraryBook> getListLibraryBookByBooks(List<Book> books) {
        List<LibraryBook> listResult = new ArrayList<>();
        for (Book book : books) {
            List<Author> authors = authorDao.getListByBookId(book.getId());
            List<Genre> genres = genreDao.getListByBookId(book.getId());
            listResult.add(new LibraryBook(book, authors, genres));
        }
        return listResult;
    }
}
