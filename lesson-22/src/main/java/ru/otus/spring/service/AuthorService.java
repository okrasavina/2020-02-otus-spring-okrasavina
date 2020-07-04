package ru.otus.spring.service;

import ru.otus.spring.dto.LibraryAuthor;

import java.util.List;

public interface AuthorService {
    List<LibraryAuthor> getListAuthor();

    LibraryAuthor getAuthorById(Long id);

    LibraryAuthor saveAuthor(LibraryAuthor libraryAuthor);

    void deleteAuthor(LibraryAuthor libraryAuthor);
}
