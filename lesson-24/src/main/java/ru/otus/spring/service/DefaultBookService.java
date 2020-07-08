package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultBookService implements BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<LibraryBook> getListBooksByGenreName(String genreName) {
        Genre genreRepo = genreRepository.findByName(genreName)
                .orElseThrow(() -> new EntityNotFoundException("Жанр", ""));
        List<Book> books = bookRepository.findAllByGenresContaining(genreRepo);
        return books.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<LibraryBook> getListBooksByAuthorName(String authorName) {
        Author author = authorRepository.findByName(authorName)
                .orElseThrow(() -> new EntityNotFoundException("Автор", ""));
        List<Book> books = bookRepository.findAllByAuthorsContaining(author);
        return books.stream().map(this::toDto).collect(Collectors.toList());

    }

    @Override
    public List<LibraryBook> getListBooksByTitle(String title) {
        List<Book> books = bookRepository.findAllByTitleContaining(title);
        return books.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteBook(LibraryBook libraryBook) {
        bookRepository.deleteById(libraryBook.getNumber());
        commentRepository.deleteAllByBook(toDomain(libraryBook));
    }

    @Override
    public List<LibraryBook> getListBook() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public LibraryBook getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Книга", "а"));
        return toDto(book);
    }

    @Transactional
    @Override
    public LibraryBook saveBook(LibraryBook libraryBook) {
        Book book = toDomain(libraryBook);
        book = bookRepository.save(book);
        return toDto(book);
    }

    @Override
    public Book toDomain(LibraryBook libraryBook) {
        List<Author> authors = getAuthorsByLibraryBook(libraryBook);
        List<Genre> genres = getGenresByLibraryBook(libraryBook);
        return new Book(libraryBook.getNumber(), libraryBook.getTitle(), libraryBook.getDescription(),
                authors, genres);
    }

    @Override
    public LibraryBook toDto(Book book) {
        return new LibraryBook(book.getId(), book.getTitle(), book.getDescription(),
                book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()),
                book.getGenres().stream().map(Genre::getName).collect(Collectors.toList()));
    }

    private List<Genre> getGenresByLibraryBook(LibraryBook libraryBook) {
        List<Genre> genres = libraryBook.getGenres().stream().map(genreName -> genreRepository.findByName(genreName)
                .orElseGet(() -> new Genre(genreName, "")))
                .map(genreRepository::save).collect(Collectors.toList());
        return genres;
    }

    private List<Author> getAuthorsByLibraryBook(LibraryBook libraryBook) {
        List<Author> authors = libraryBook.getAuthors().stream().map(authorName -> authorRepository.findByName(authorName)
                .orElseGet(() -> new Author(authorName, LocalDate.now())))
                .map(authorRepository::save).collect(Collectors.toList());
        return authors;
    }

}
