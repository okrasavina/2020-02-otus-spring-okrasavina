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

    public static LibraryComment toDto(Comment comment) {
        return new LibraryComment(comment.getId(), comment.getTextComment(),
                LibraryBook.toDto(comment.getBook()));
    }

}
