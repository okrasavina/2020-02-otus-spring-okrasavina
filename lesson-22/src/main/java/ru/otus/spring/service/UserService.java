package ru.otus.spring.service;

import ru.otus.spring.dto.LibraryUser;

public interface UserService {
    LibraryUser saveNewUser(LibraryUser libraryUser);
}
