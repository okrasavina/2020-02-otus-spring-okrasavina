package ru.otus.spring.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "book_genre", joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "book_id")
    private List<Comment> comments;

    @Override
    public String toString() {
        String commentText = comments.isEmpty() ? "-" : comments.stream().map(Comment::getTextComment).collect(Collectors.joining(", "));
        return String.format("LibraryBook %d { bookTitle = '%s'; authorNames = %s; genreNames = %s; comments: %s}",
                id, title, authors.stream().map(Author::getName).collect(Collectors.joining(", ")),
                        genres.stream().map(Genre::getName).collect(Collectors.joining(", ")), commentText);
    }
}
