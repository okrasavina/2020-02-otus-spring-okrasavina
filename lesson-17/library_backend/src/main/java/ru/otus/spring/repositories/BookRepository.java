package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;

@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {
}
