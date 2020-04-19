package ru.otus.spring.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Genre> getByName(String genreName) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", genreName);
        return query.getResultList().stream().findFirst();
    }

    @Override
    @Transactional
    public void deleteGenreWithoutBooks() {
        Query query = em.createQuery("delete from Genre g where not exists (select " +
                "b from Book b where g member of b.genres)");
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> findListByListName(List<String> names) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name in :names", Genre.class);
        query.setParameter("names", names);
        return query.getResultList();
    }
}
