package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBook(Book book);

    @Modifying
    void deleteAllByBook(Book book);
}
