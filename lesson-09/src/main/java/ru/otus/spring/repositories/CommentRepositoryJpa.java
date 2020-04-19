package ru.otus.spring.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Transactional
@Repository
public class CommentRepositoryJpa implements CommentRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        else {
            return em.merge(comment);
        }
    }

    @Override
    @Transactional
    public void deleteCommentsByBook(Book book) {
        Query query = em.createQuery("delete from Comment c where c.book = :book");
        query.setParameter("book", book);
        query.executeUpdate();
    }
}
