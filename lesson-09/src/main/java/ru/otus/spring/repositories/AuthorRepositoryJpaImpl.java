package ru.otus.spring.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class AuthorRepositoryJpaImpl implements AuthorRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> getByName(String authorName) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name =  :name", Author.class);
        query.setParameter("name", authorName);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteAuthorWithoutBooks() {
        Query query = em.createQuery("delete from Author a where not exists (select " +
                "b from LibraryBook b where a member of b.authors)");
        query.executeUpdate();
    }

    @Override
    public List<Author> findListByListName(List<String> names) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name in :names", Author.class);
        query.setParameter("names", names);
        return query.getResultList();
    }
}
