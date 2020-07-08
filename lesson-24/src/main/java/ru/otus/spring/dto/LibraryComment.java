package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibraryComment {
    private Long number;
    private String textComment;
    private LibraryBook libraryBook;

    public LibraryComment() {
        this.number = 0L;
    }

    public LibraryComment(LibraryBook libraryBook) {
        this();
        this.libraryBook = libraryBook;
    }

}
