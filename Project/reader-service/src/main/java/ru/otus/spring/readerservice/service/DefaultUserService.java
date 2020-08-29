package ru.otus.spring.readerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.readerservice.domain.User;
import ru.otus.spring.readerservice.dto.UserAlreadyExistsException;
import ru.otus.spring.readerservice.dto.UserDTO;
import ru.otus.spring.readerservice.repository.UserRepository;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultUserService implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public UserDTO getUserByDetails(UserDTO userDTO) {
        return UserDTO.toDTO(repository.findByUsernameAndPassword(userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()))
                .orElseThrow(() -> new UserPrincipalNotFoundException("Неверное имя пользователя или пароль.")));

    }

    @Override
    @Transactional
    public UserDTO registerNewUser(UserDTO userDTO) {
        repository.findByUsername(userDTO.getUsername()).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });
        return UserDTO.toDTO(repository.save(new User(userDTO.getUsername(),
                userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()))));
    }
}
