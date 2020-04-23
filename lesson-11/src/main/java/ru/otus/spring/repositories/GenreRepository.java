package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Transactional(readOnly = true)
    Optional<Genre> findByName(String genreName);

    @Modifying
    @Transactional
    @Query("delete from Genre g where not exists (select b from Book b where g member of b.genres)")
    void deleteGenreWithoutBooks();

    @Transactional(readOnly = true)
    List<Genre> findByNameIn(List<String> names);
}
