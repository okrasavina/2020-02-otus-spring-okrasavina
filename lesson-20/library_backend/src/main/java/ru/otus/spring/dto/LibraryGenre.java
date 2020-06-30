package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryGenre {

    private String id;
    private String name;
    private String description;

    public static LibraryGenre toDto(Genre genre) {
        return new LibraryGenre(genre.getId(), genre.getName(), genre.getDescription());
    }
}
