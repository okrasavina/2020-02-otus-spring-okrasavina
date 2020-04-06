package ru.otus.spring.dto;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class LibraryBook {
    private final String bookTitle;

    @Setter
    private long bookNumber;

    @Setter
    private List<String> authorNames;

    @Setter
    private List<String> genreNames;

    @Override
    public String toString() {
        return String.format("LibraryBook %d { bookTitle = '%s'; authorNames = %s; genreNames = %s}", bookNumber,
                        bookTitle, authorNames.stream().collect(Collectors.joining(", ")),
                        genreNames.stream().collect(Collectors.joining(", ")));
    }
}
