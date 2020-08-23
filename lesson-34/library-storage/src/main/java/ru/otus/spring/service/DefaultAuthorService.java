package ru.otus.spring.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.repositories.AuthorRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultAuthorService implements AuthorService {
    private final AuthorRepository authorRepository;

    @HystrixCommand(fallbackMethod = "getDefaultListAuthor")
    @Override
    public List<LibraryAuthor> getListAuthor() {
        return authorRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getDefaultAuthorById",
            ignoreExceptions = {EntityNotFoundException.class})
    @Override
    public LibraryAuthor getAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Автор", ""));
        return toDto(author);
    }

    @HystrixCommand(fallbackMethod = "saveAuthorDefault")
    @Transactional
    @Override
    public LibraryAuthor saveAuthor(LibraryAuthor libraryAuthor) {
        Author author = toDomain(libraryAuthor);
        author = authorRepository.save(author);
        return toDto(author);
    }
    
    @Transactional
    @Override
    public void deleteAuthor(LibraryAuthor libraryAuthor) throws EntityNotAllowedDeleteException {
        try {
            authorRepository.deleteById(libraryAuthor.getNumber());
        } catch (Exception e) {
            throw new EntityNotAllowedDeleteException("автор");
        }
    }

    public List<LibraryAuthor> getDefaultListAuthor() {
        return List.of(getDefaultAuthorById(1L));
    }

    public LibraryAuthor getDefaultAuthorById(Long id) {
        return new LibraryAuthor(id, "Александр Пушкин", LocalDate.of(1799, 6, 6));
    }

    public LibraryAuthor saveAuthorDefault(LibraryAuthor libraryAuthor) {
        libraryAuthor.setNumber(0L);
        return libraryAuthor;
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
