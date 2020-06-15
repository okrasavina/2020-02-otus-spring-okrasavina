package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryComment {
    private Long number;
    private String textComment;
    private LibraryBook libraryBook;

    public LibraryComment(LibraryBook libraryBook) {
        this.libraryBook = libraryBook;
    }

}
