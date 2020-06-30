package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Book {
    @Id
    private String id;

    private String title;
    private String description;
    private List<Author> authors;
    private List<Genre> genres;

    public Book(String title, String description, List<Author> authors, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.genres = genres;
    }

}
