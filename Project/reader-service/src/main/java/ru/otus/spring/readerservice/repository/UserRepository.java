package ru.otus.spring.readerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.readerservice.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
