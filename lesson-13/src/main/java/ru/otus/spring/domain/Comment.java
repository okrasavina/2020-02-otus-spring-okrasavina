package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Comment {
    @Id
    private String id;

    private String textComment;

    private Book book;

    public Comment(String textComment, Book book) {
        this.textComment = textComment;
        this.book = book;
    }

    @Override
    public String toString() {
        return String.format("Comment {text = '%s', book title = '%s'}", textComment, book.getTitle());
    }
}
