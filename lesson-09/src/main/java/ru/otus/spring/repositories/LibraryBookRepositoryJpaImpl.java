package ru.otus.spring.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.LibraryBook;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class LibraryBookRepositoryJpaImpl implements LibraryBookRepositoryJpa {

    @PersistenceContext
    private EntityManager em;

    @Override
    public LibraryBook save(LibraryBook book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        else {
            return em.merge(book);
        }
    }

    @Override
    public List<LibraryBook> getAll() {
        TypedQuery<LibraryBook> query = em.createQuery("select b from LibraryBook b", LibraryBook.class);
        return query.getResultList();
    }

    @Override
    public List<LibraryBook> getAllByAuthor(Author author) {
        TypedQuery<LibraryBook> query = em.createQuery("select b from LibraryBook b " +
                "where :author member of b.authors", LibraryBook.class);
        query.setParameter("author", author);
        return query.getResultList();
    }

    @Override
    public List<LibraryBook> getAllByGenre(Genre genre) {
        TypedQuery<LibraryBook> query = em.createQuery("select b from LibraryBook b " +
                "where :genre member of b.genres", LibraryBook.class);
        query.setParameter("genre", genre);
        return query.getResultList();
    }

    @Override
    public void deleteById(long bookId) {
        Query query = em.createQuery("delete from LibraryBook b where b.id = :id");
        query.setParameter("id", bookId);
        query.executeUpdate();
    }

    @Override
    public Optional<LibraryBook> getById(long bookId) {
        TypedQuery<LibraryBook> query = em.createQuery("select b from LibraryBook b where b.id = :id", LibraryBook.class);
        query.setParameter("id", bookId);
        return query.getResultList().stream().findFirst();
    }

}
