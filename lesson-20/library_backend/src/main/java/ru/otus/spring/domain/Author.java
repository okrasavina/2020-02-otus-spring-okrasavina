package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Author {
    @Id
    private String id;

    private String name;
    private LocalDate birthDay;

    public Author(String name, LocalDate birthDay) {
        this.name = name;
        this.birthDay = birthDay;
    }

}
