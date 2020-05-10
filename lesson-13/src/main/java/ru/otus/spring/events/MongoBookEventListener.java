package ru.otus.spring.events;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Sequence;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.repositories.GenreRepository;
import ru.otus.spring.service.SequenceGeneratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MongoBookEventListener extends AbstractMongoEventListener<Book> {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final SequenceGeneratorService generatorService;
    private final MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {
        super.onBeforeConvert(event);
        Book book = event.getSource();

        if (!(book.getAuthors().isEmpty())) {
            book.getAuthors().stream().filter(a -> Objects.isNull(a.getId())).forEach(authorRepository::save);
        }

        if (!(book.getGenres().isEmpty())) {
            book.getGenres().stream().filter(g -> Objects.isNull(g.getId())).forEach(genreRepository::save);
        }

        if (book.getId() < 1) {
            book.setId(generatorService.generateSequence(Book.SEQUENCE_NAME));
        }
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        super.onBeforeDelete(event);
        Document source = event.getSource();
        Book book = mongoOperations.findById(source.get("_id"), Book.class);
        commentRepository.deleteAllByBook(book);

        deleteAuthorsWithoutBooks(book);

        deleteGenresWithoutBooks(book);
    }

    private void deleteAuthorsWithoutBooks(Book book) {
        List<Author> authorsForDelete = new ArrayList<>();
        for (Author author: book.getAuthors()
        ) {
            List<Book> bookAuthors = bookRepository.findAllByAuthorsContaining(author);
            bookAuthors.remove(book);
            if (bookAuthors.isEmpty()) {
                authorsForDelete.add(author);
            }
        }

        authorRepository.deleteAll(authorsForDelete);
    }

    private void deleteGenresWithoutBooks(Book book) {
        List<Genre> genresForDelete = new ArrayList<>();
        for (Genre genre: book.getGenres()
        ) {
            List<Book> bookGenres = bookRepository.findAllByGenresContaining(genre);
            bookGenres.remove(book);
            if (bookGenres.isEmpty()) {
                genresForDelete.add(genre);
            }
        }

        genreRepository.deleteAll(genresForDelete);
    }

}
