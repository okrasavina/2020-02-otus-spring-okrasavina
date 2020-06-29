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

    public LibraryAuthor(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.birthDay = author.getBirthDay();
    }

}
