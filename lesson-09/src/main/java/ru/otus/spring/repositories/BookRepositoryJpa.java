package ru.otus.spring.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        else {
            return em.merge(book);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllByAuthor(Author author) {
        TypedQuery<Book> query = em.createQuery("select b from Book b " +
                "where :author member of b.authors", Book.class);
        query.setParameter("author", author);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllByGenre(Genre genre) {
        TypedQuery<Book> query = em.createQuery("select b from Book b " +
                "where :genre member of b.genres", Book.class);
        query.setParameter("genre", genre);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void deleteById(long bookId) {
        Query query = em.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", bookId);
        query.executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getById(long bookId) {
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.id = :id", Book.class);
        query.setParameter("id", bookId);
        return query.getResultList().stream().findFirst();
    }

}
