package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Book {
    @Transient
    public static final String SEQUENCE_NAME = "book_sequence";

    @Id
    private long id;

    private String title;

    private List<Author> authors;

    private List<Genre> genres;

    public Book(String title, List<Author> authors, List<Genre> genres) {
        this.title = title;
        this.authors = authors;
        this.genres = genres;
    }

    @Override
    public String toString() {
        return String.format("LibraryBook %d{ bookTitle = '%s'; authorNames = %s; genreNames = %s}",
                id, title, authors.stream().map(Author::getName).collect(Collectors.joining(", ")),
                        genres.stream().map(Genre::getName).collect(Collectors.joining(", ")));
    }
}
