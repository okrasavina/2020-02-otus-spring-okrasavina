package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByName(String authorName);

    @Transactional
    @Modifying
    @Query("delete from Author a where not exists (select b from Book b where a member of b.authors)")
    void deleteAuthorWithoutBooks();

    List<Author> findByNameIn(List<String> names);
}
