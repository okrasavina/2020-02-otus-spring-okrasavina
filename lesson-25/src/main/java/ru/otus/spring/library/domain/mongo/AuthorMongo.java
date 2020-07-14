package ru.otus.spring.library.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "author")
public class AuthorMongo {
    @Id
    private String id;
    private String name;
    private LocalDate birthDay;

    public AuthorMongo(String name, LocalDate birthDay) {
        this.name = name;
        this.birthDay = birthDay;
    }
}
