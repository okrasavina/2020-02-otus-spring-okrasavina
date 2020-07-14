package ru.otus.spring.library.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comment")
public class CommentMongo {
    @Id
    private String id;
    private long jpaId;
    private String textComment;
    private BookMongo book;

    public CommentMongo(long jpaId, String textComment, BookMongo book) {
        this.jpaId = jpaId;
        this.textComment = textComment;
        this.book = book;
    }
}
