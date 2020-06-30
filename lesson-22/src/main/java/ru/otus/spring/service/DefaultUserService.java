package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.User;
import ru.otus.spring.dto.LibraryUser;
import ru.otus.spring.dto.UserAlreadyExistsException;
import ru.otus.spring.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LibraryUser saveNewUser(LibraryUser libraryUser) {
        userRepository.findByUserName(libraryUser.getUserName()).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });
        return LibraryUser.toDto(userRepository.save(new User(libraryUser.getUserName(),
                libraryUser.getEmail(), passwordEncoder.encode(libraryUser.getPassword()))));
    }
}
