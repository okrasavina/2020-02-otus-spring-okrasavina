package ru.otus.spring.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final LocaleService localeService;
    private final MessageSource messageSource;
    private final BookService bookService;
    @Getter
    private Book book;

    @Override
    public String createLibraryBook(String bookTitle, List<String> authorNames, List<String> genreNames) {
        bookService.createBook(new Book(0, bookTitle,
                authorNames.stream().map(s -> new Author(0, s)).collect(Collectors.toList()),
                genreNames.stream().map(s -> new Genre(0, s)).collect(Collectors.toList()), List.of()));
        return messageSource.getMessage("book.create", null, localeService.getCurrentLocale());
    }

    @Override
    public List<String> getListBook() {
        List<Book> books = bookService.getListBook();

        return books.stream().map(Book::toString).collect(Collectors.toList());
    }

    @Override
    public String deleteBookByNumber(long bookId) {
        if ((book != null) && (book.getId() == bookId)) {
            return messageSource.getMessage("book.delete.error", new String[]{String.valueOf(bookId)},
                    localeService.getCurrentLocale());
        } else {
            bookService.deleteBookById(bookId);

            return messageSource.getMessage("book.delete", new String[]{String.valueOf(bookId)},
                    localeService.getCurrentLocale());
        }
    }

    @Override
    public String getBookByNumber(long bookNumber) {
        book = bookService.getBookByNumber(bookNumber);
        return book.toString();
    }

    @Override
    public List<String> getListAuthor() {
        return bookService.getListAuthor();
    }

    @Override
    public List<String> getListBookByAuthorName(String authorName) {
        return bookService.getListBookByAuthorName(authorName)
                .stream().map(Book::toString).collect(Collectors.toList());
    }

    @Override
    public List<String> getListGenres() {
        return bookService.getListGenres();
    }

    @Override
    public List<String> getListBookByGenreName(String genreName) {
        return bookService.getListBookByGenreName(genreName)
                .stream().map(Book::toString).collect(Collectors.toList());
    }

    @Override
    public String addCommentToTheBook(String textComment) {
        Long bookId = bookService.addCommentToTheBook(book, textComment);
        return messageSource.getMessage("book.comment", new String[]{String.valueOf(bookId)},
                localeService.getCurrentLocale());
    }

    @Override
    public String returnTheBook() {
        String result = messageSource.getMessage("book.return", new String[]{String.valueOf(book.getId())},
                localeService.getCurrentLocale());
        book = null;
        return result;
    }

    @Override
    public String clearCommentsOnTheBook() {
        bookService.clearCommentsOnTheBook(book);
        return messageSource.getMessage("book.comments.clear", new String[]{String.valueOf(book.getId())},
                localeService.getCurrentLocale());
    }
}
