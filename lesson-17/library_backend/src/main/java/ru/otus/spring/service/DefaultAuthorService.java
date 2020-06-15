package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.repositories.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAuthorService implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<LibraryAuthor> getListAuthor() {
        return authorRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
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
    public void deleteAuthor(LibraryAuthor libraryAuthor) throws EntityNotAllowedDeleteException {
        try {
            authorRepository.deleteById(libraryAuthor.getNumber());
        } catch (Exception e) {
            throw new EntityNotAllowedDeleteException("автор");
        }
    }

    private Author toDomain(LibraryAuthor libraryAuthor) {
        return new Author(libraryAuthor.getNumber(),
                libraryAuthor.getName(),
                libraryAuthor.getBirthDay());
    }

    private LibraryAuthor toDto(Author author) {
        return new LibraryAuthor(author.getId(), author.getName(), author.getBirthDay());
    }
}
