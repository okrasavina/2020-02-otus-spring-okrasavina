package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Author;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryAuthor {

    private String id;
    private String name;
    private LocalDate birthDay;

    public static LibraryAuthor toDto(Author author) {
        return new LibraryAuthor(author.getId(), author.getName(), author.getBirthDay());
    }

}
