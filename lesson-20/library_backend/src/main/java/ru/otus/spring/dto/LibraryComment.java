package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Comment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryComment {
    private String id;
    private String textComment;
    private LibraryBook libraryBook;

    public LibraryComment(Comment comment) {
        this.id = comment.getId();
        this.textComment = comment.getTextComment();
        this.libraryBook = new LibraryBook(comment.getBook());
    }

    public LibraryComment setLibraryBook(LibraryBook book) {
        this.libraryBook = book;
        return this;
    }

}
