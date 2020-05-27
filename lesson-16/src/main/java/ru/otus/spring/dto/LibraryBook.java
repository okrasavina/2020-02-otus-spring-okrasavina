package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LibraryBook {
    private Long number;
    private String title;
    private String description;
    private List<String> authors;
    private List<String> genres;

    public LibraryBook() {
        this.number = 0L;
    }

}
