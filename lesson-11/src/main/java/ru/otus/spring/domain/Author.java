package ru.otus.spring.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Author(String name) {
        this.id = 0;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Author {id = %d, name = '%s'}", id, name);
    }
}
