package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с пользователями должен")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @DisplayName("сохранять пользователя в базу данных")
    @Test
    void shouldSaveUser() {
        User userAdded = new User("testUser", "test@mail.ru", "123456");
        userAdded = entityManager.persist(userAdded);
        entityManager.flush();

        Optional<User> userFound = repository.findById(userAdded.getId());

        assertThat(userFound).isNotNull();
        assertThat(userFound.get()).isEqualTo(userAdded);
    }

    @DisplayName("возвращать пользователя по его имени")
    @Test
    void shouldReturnExpectedUserByUserName() {
        User userAdded = new User("testUser", "test@mail.ru", "123456");
        userAdded = entityManager.persist(userAdded);
        entityManager.flush();

        Optional<User> userFound = repository.findByUserName(userAdded.getUserName());

        assertThat(userFound).isNotNull();
        assertThat(userFound.get()).isEqualTo(userAdded);
    }

}