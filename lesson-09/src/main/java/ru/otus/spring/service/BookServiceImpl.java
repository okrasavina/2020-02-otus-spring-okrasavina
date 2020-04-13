package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.LibraryBook;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.repositories.AuthorRepositoryJpa;
import ru.otus.spring.repositories.CommentRepositoryJpa;
import ru.otus.spring.repositories.GenreRepositoryJpa;
import ru.otus.spring.repositories.LibraryBookRepositoryJpa;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final LibraryBookRepositoryJpa bookRepo;
    private final AuthorRepositoryJpa authorRepo;
    private final GenreRepositoryJpa genreRepo;
    private final CommentRepositoryJpa commentRepo;

    @Override
    public void createBook(LibraryBook libraryBook) {
        List<Author> authors = getListAuthorsWithActualId(libraryBook);
        List<Genre> genres = getListGenresWithActualId(libraryBook);
        libraryBook.setAuthors(List.of());
        libraryBook.setGenres(List.of());
        bookRepo.save(libraryBook);

        libraryBook.setAuthors(authors);
        libraryBook.setGenres(genres);
        bookRepo.save(libraryBook);
    }

    private List<Genre> getListGenresWithActualId(LibraryBook book) {
        List<Genre> genres = book.getGenres();
        List<Genre> genresExisting = genreRepo.findListByListName(genres.stream().map(Genre::getName).collect(Collectors.toList()));
        genres.forEach(g -> {
            val genreExist = genresExisting.stream().filter(b -> b.getName().equals(g.getName())).findFirst();
            if (genreExist.isPresent()) {
                g.setId(genreExist.get().getId());
            }
        });

        return genres;
    }

    @Override
    public List<LibraryBook> getListBook() {
        return bookRepo.getAll();
    }

    @Override
    public void deleteBookById(long bookId) {
       bookRepo.deleteById(bookId);
       authorRepo.deleteAuthorWithoutBooks();
       genreRepo.deleteGenreWithoutBooks();
    }

    @Override
    public LibraryBook getBookByNumber(long bookId) {
        return bookRepo.getById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(LibraryBook.class, "number", Long.toString(bookId)));
    }

    @Override
    public List<String> getListAuthor() {
        List<Author> authors = authorRepo.getAll();
        return authors.stream().map(Author::toString).collect(Collectors.toList());
    }

    @Override
    public List<LibraryBook> getListBookByAuthorName(String authorName) {
        Author author = authorRepo.getByName(authorName)
                .orElseThrow(() -> new EntityNotFoundException(Author.class, "name", authorName));
        return bookRepo.getAllByAuthor(author);
    }

    @Override
    public List<String> getListGenres() {
        List<Genre> genres = genreRepo.getAll();
        return genres.stream().map(Genre::toString).collect(Collectors.toList());
    }

    @Override
    public List<LibraryBook> getListBookByGenreName(String genreName) {
        Genre genre = genreRepo.getByName(genreName)
                .orElseThrow(() -> new EntityNotFoundException(Genre.class, "name", genreName));
        return bookRepo.getAllByGenre(genre);
    }

    @Override
    public long addCommentToTheBook(LibraryBook book, String textComment) {
        Comment commentAdded = new Comment(0, textComment, book);
        commentRepo.save(commentAdded);
        return book.getId();
    }

    @Override
    public void clearCommentsOnTheBook(LibraryBook book) {
        commentRepo.deleteCommentsByBook(book);
    }

    private List<Author> getListAuthorsWithActualId(LibraryBook book) {
        List<Author> authors = book.getAuthors();
        List<Author> authorsExisting = authorRepo.findListByListName(authors.stream().map(Author::getName).collect(Collectors.toList()));
        authors.forEach(a -> {
            val authorExist = authorsExisting.stream().filter(b -> b.getName().equals(a.getName())).findFirst();
            if (authorExist.isPresent()) {
                a.setId(authorExist.get().getId());
            }
        });

        return authors;
    }

}
