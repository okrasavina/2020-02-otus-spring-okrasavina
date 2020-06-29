package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryBook {
    private String id;
    private String title;
    private String description;
    private String authors;
    private String genres;

    public LibraryBook(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.authors = book.getAuthors().stream().map(Author::getName)
                .collect(Collectors.joining(", "));
        this.genres = book.getGenres().stream().map(Genre::getName)
                .collect(Collectors.joining(", "));
    }
}
