package ru.otus.spring.dto;

import lombok.*;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class LibraryBook {
    private final Book book;

    @Setter
    private List<Author> authors;

    @Setter
    private List<Genre> genres;

    @Override
    public String toString() {
        return String.format("LibraryBook %d { bookTitle = '%s'; authorNames = %s; genreNames = %s}", book.getId(),
                        book.getTitle(), authors.stream().map(Author::getName).collect(Collectors.joining(", ")),
                        genres.stream().map(Genre::getName).collect(Collectors.joining(", ")));
    }
}
