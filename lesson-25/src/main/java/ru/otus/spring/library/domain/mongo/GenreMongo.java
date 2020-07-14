package ru.otus.spring.library.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "genre")
public class GenreMongo {
    @Id
    private String id;
    private String name;
    private String description;

    public GenreMongo(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
