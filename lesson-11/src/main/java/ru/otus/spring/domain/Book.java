package ru.otus.spring.domain;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
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
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinTable(name = "book_genre", joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "book_id")
    private List<Comment> comments;

    public Book(long id, String title) {
        this.id = id;
        this.title = title;
        authors = List.of();
        genres = List.of();
        comments = List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                title.equals(book.title) &&
                authors.equals(book.authors) &&
                genres.equals(book.genres) &&
                comments.equals(book.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authors, genres, comments);
    }

    @Override
    public String toString() {
        String commentText = comments.isEmpty() ? "-" : comments.stream().map(Comment::getTextComment).collect(Collectors.joining(", "));
        return String.format("LibraryBook %d { bookTitle = '%s'; authorNames = %s; genreNames = %s; comments: %s}",
                id, title, authors.stream().map(Author::getName).collect(Collectors.joining(", ")),
                        genres.stream().map(Genre::getName).collect(Collectors.joining(", ")), commentText);
    }
}
