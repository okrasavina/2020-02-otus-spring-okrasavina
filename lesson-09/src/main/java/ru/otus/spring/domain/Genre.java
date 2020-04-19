package ru.otus.spring.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Override
    public String toString() {
        return String.format("Genre {id = %d, name = '%s'}", id, name);
    }
}
