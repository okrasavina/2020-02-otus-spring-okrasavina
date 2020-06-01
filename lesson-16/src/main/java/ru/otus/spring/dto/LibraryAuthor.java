package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LibraryAuthor {

    private Long number;
    private String name;
    private LocalDate birthDay;
    private boolean couldDelete;

    public LibraryAuthor() {
        this.number = 0L;
        this.couldDelete = false;
    }

}
