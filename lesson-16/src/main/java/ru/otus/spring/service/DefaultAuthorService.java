package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultAuthorService implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public List<LibraryAuthor> getListAuthor() {
        List<LibraryAuthor> authors = new ArrayList<>();
        authorRepository.findAll().forEach(author -> {
            LibraryAuthor libraryAuthor = toDto(author);
            libraryAuthor.setCouldDelete(bookRepository.findAllByAuthorsContaining(author).isEmpty());
            authors.add(libraryAuthor);
        });
        return authors;
    }

    @Override
    public LibraryAuthor getAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Автор", ""));
        return toDto(author);
    }

    @Override
    public LibraryAuthor saveAuthor(LibraryAuthor libraryAuthor) {
        Author author = toDomain(libraryAuthor);
        author = authorRepository.save(author);
        return toDto(author);
    }

    @Override
    public void deleteAuthor(LibraryAuthor libraryAuthor) {
        authorRepository.deleteById(libraryAuthor.getNumber());
    }

    private Author toDomain(LibraryAuthor libraryAuthor) {
        return new Author(libraryAuthor.getNumber(),
                libraryAuthor.getName(),
                libraryAuthor.getBirthDay());
    }

    private LibraryAuthor toDto(Author author) {
        return new LibraryAuthor(author.getId(), author.getName(), author.getBirthDay(), false);
    }
}
