package ru.otus.spring.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.spring.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class BookCountHealthIndicator implements HealthIndicator {
    private final BookRepository bookRepository;

    @Override
    public Health health() {
        long bookCount = bookRepository.count();
        if (bookCount == 0) {
            return Health.status(Status.DOWN).withDetail("message", "Библиотека пуста!").build();
        } else {
            return Health.status(Status.UP).withDetail("message", "В библиотеке есть книги.").build();
        }
    }
}
