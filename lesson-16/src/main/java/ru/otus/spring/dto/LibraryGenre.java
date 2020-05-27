package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibraryGenre {

    private Long number;
    private String name;
    private String description;
    private boolean couldDelete;

    public LibraryGenre() {
        this.number = 0L;
        this.couldDelete = true;
    }

}
