package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.spring.domain.User;
import ru.otus.spring.dto.LibraryUser;
import ru.otus.spring.dto.UserAlreadyExistsException;
import ru.otus.spring.repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с пользователями должен")
@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private DefaultUserService userService;

    @BeforeEach
    void setUp() {
        userService = new DefaultUserService(userRepository, passwordEncoder);
    }

    @DisplayName("сохранять нового пользователя в базу данных")
    @Test
    void shouldSaveNewUser() {
        LibraryUser userAdded = new LibraryUser("testUser", "test@mail.ru", "123456");
        User user = new User(userAdded.getUserName(), userAdded.getEmail(), userAdded.getPassword());

        when(userRepository.findByUserName(userAdded.getUserName())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());

        LibraryUser userActual = userService.saveNewUser(userAdded);

        assertThat(userActual).matches(u -> u.getUserName().equals(userAdded.getUserName()))
                .matches(u -> u.getEmail().equals(userAdded.getEmail()));

        verify(userRepository, times(1)).findByUserName(userAdded.getUserName());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode(userAdded.getPassword());
    }

    @DisplayName("возвращать ошибку по существующему пользователю")
    @Test
    void shouldReturnExceptionBySavingExistingUser() {
        LibraryUser userAdded = new LibraryUser("testUser", "test@mail.ru", "123456");
        User user = new User(1L, userAdded.getUserName(), userAdded.getEmail(), userAdded.getPassword());
        Throwable exception = new UserAlreadyExistsException();

        when(userRepository.findByUserName(userAdded.getUserName())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.saveNewUser(userAdded))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage(exception.getMessage());

        verify(userRepository, times(1)).findByUserName(userAdded.getUserName());
    }
}