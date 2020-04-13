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
public class GenreRepositoryJpaImpl implements GenreRepositoryJpa {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> getByName(String genreName) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", genreName);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteGenreWithoutBooks() {
        Query query = em.createQuery("delete from Genre g where not exists (select " +
                "b from LibraryBook b where g member of b.genres)");
        query.executeUpdate();
    }

    @Override
    public List<Genre> findListByListName(List<String> names) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name in :names", Genre.class);
        query.setParameter("names", names);
        return query.getResultList();
    }
}
