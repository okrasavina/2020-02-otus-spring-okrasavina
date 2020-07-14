package ru.otus.spring.library.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "book")
public class BookMongo {
    @Id
    private String id;
    private long jpaId;
    private String title;
    private String description;
    private List<AuthorMongo> authors;
    private List<GenreMongo> genres;

    public BookMongo(long jpaId, String title, String description, List<AuthorMongo> authors,
                     List<GenreMongo> genres) {
        this.jpaId = jpaId;
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.genres = genres;
    }
}
