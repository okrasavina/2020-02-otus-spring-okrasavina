package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Arrays;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class ShellService {
    private final LibraryService libraryService;
    private final LocaleService localeService;

    @ShellMethod(value = "Create library book: cr <bookTitle>", key = {"cr", "create"})
    public String createLibraryBook(@ShellOption(value = "-t", help = "The title of the book") String bookTitle,
                                    @ShellOption(value = "-a", help = "Authors of the book separate by comma") String authorNames,
                                    @ShellOption(value = "-g", help = "Genres of the book separate by comma") String genreNames) {
        return libraryService.createLibraryBook(bookTitle, Arrays.asList(authorNames.split(",")),
                Arrays.asList(genreNames.split(",")));
    }

    @ShellMethod(value = "Get list book: get", key = "all")
    public List<String> getListBook() {
        return libraryService.getListBook();
    }

    @ShellMethod(value = "Get info about book by number", key = "get")
    public String getBookInfoByNumber(String bookNumber) {
        return libraryService.getBookByNumber(Long.parseLong(bookNumber));
    }

    @ShellMethod(value = "Delete book by number", key = {"delete", "del"})
    public String deleteBookByNumber(String bookNumber) {
        return libraryService.deleteBookByNumber(Long.parseLong(bookNumber));
    }

    @ShellMethod(value = "Get list authors", key = "authors")
    public List<String> getListAuthor() {
        return libraryService.getListAuthor();
    }

    @ShellMethod(value = "Get list genres", key = "genres")
    public List<String> getListGenres() {
        return libraryService.getListGenres();
    }

    @ShellMethod(value = "Get list books by author name", key = "geta")
    public List<String> getListBookByAuthorName(@ShellOption(value = "-a", help = "Author name") String authorName) {
        return libraryService.getListBookByAuthorName(authorName);
    }

    @ShellMethod(value = "Get list books by genre name", key = "getg")
    public List<String> getListBookByGenreName(@ShellOption(value = "-g", help = "Genre name") String genreName) {
        return libraryService.getListBookByGenreName(genreName);
    }

    @ShellMethod(value = "Change language: lang <localeTag>", key = "lang")
    public String changeLanguage(String localeTag) {
        return localeService.changeLocale(localeTag);
    }

}
