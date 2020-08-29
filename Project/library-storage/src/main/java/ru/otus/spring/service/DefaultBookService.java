package ru.otus.spring.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
import java.util.Arrays;
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

    @Transactional
    @Override
    public void deleteBook(LibraryBook libraryBook) {
        bookRepository.deleteById(libraryBook.getNumber());
        commentRepository.deleteAllByBook(toDomain(libraryBook));
    }

    @HystrixCommand(fallbackMethod = "getDefaultListBook")
    @Override
    public List<LibraryBook> getListBook() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(this::toDto).collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getDefaultBookById",
            ignoreExceptions = {EntityNotFoundException.class})
    @Override
    public LibraryBook getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Книга", "а"));
        return toDto(book);
    }

    @HystrixCommand(fallbackMethod = "saveBookDefault")
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
                book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", ")),
                book.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", ")));
    }

    public LibraryBook getDefaultBookById(Long id) {
        return new LibraryBook(id, "Руслан и Людмила", "История любви",
                "Пушкин", "Поэма");
    }

    public List<LibraryBook> getDefaultListBook() {
        return List.of(getDefaultBookById(1L));
    }

    public LibraryBook saveBookDefault(LibraryBook libraryBook) {
        libraryBook.setNumber(0L);
        return libraryBook;
    }

    private List<Genre> getGenresByLibraryBook(LibraryBook libraryBook) {
        List<Genre> genres = Arrays.stream(libraryBook.getGenres().split(","))
                .map(genreName -> genreRepository.findByName(genreName.trim())
                        .orElseGet(() -> new Genre(genreName.trim(), "")))
                .map(genreRepository::save).collect(Collectors.toList());
        return genres;
    }

    private List<Author> getAuthorsByLibraryBook(LibraryBook libraryBook) {
        List<Author> authors = Arrays.stream(libraryBook.getAuthors().split(","))
                .map(authorName -> authorRepository.findByName(authorName.trim())
                        .orElseGet(() -> new Author(authorName.trim(), LocalDate.now())))
                .map(authorRepository::save).collect(Collectors.toList());
        return authors;
    }

}
