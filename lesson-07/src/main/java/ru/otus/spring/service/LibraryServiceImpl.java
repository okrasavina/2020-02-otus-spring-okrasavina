package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.LocaleConfig;
import ru.otus.spring.dto.LibraryBook;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final LocaleConfig localeConfig;
    private final MessageSource messageSource;
    private final BookService bookService;

    @Override
    public String createLibraryBook(String bookTitle, List<String> authorNames, List<String> genreNames) {
        bookService.createBook(new LibraryBook(bookTitle, 1L, authorNames, genreNames));
        return messageSource.getMessage("book.create", null, localeConfig.getCurrentLocale());
    }

    @Override
    public List<String> getListBook() {
        List<LibraryBook> books = bookService.getListBook();

        return books.stream().map(LibraryBook::toString).collect(Collectors.toList());
    }

    @Override
    public String deleteBookByNumber(long bookId) {
        bookService.deleteBookById(bookId);

        return messageSource.getMessage("book.delete", new String[]{String.valueOf(bookId)},
                localeConfig.getCurrentLocale());
    }

    @Override
    public String getBookByNumber(long bookNumber) {
        return bookService.getBookByNumber(bookNumber);
    }

    @Override
    public List<String> getListAuthor() {
        return bookService.getListAuthor();
    }

    @Override
    public List<String> getListBookByAuthorName(String authorName) {
        return bookService.getListBookByAuthorName(authorName)
                .stream().map(LibraryBook::toString).collect(Collectors.toList());
    }

    @Override
    public List<String> getListGenres() {
        return bookService.getListGenres();
    }

    @Override
    public List<String> getListBookByGenreName(String genreName) {
        return bookService.getListBookByGenreName(genreName)
                .stream().map(LibraryBook::toString).collect(Collectors.toList());
    }
}
