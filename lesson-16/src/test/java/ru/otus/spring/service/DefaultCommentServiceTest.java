package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.repositories.CommentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с комментариями должен")
@ExtendWith(MockitoExtension.class)
class DefaultCommentServiceTest {

    private static final long ERROR_COMMENT_ID = 10L;
    private LibraryComment libraryComment;
    private Comment comment;
    private LibraryBook libraryBook;
    private Book book;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookService bookService;

    private DefaultCommentService service;

    @BeforeEach
    void setUp() {
        service = new DefaultCommentService(commentRepository, bookService);
        Author author = new Author(1L, "Николай Гоголь", LocalDate.of(1809, 03, 19));
        Genre genre = new Genre(1L, "Роман", "Литературный жанр");
        book = new Book(1L, "Мертвые души", "Авантюра Чичикова",
                List.of(author), List.of(genre));
        libraryBook = new LibraryBook(1L, "Мертвые души", "Авантюра Чичикова",
                List.of(author.getName()), List.of(genre.getName()));
        comment = new Comment(1L, "Книга из школьной программы", book);
        libraryComment = new LibraryComment(1L, "Книга из школьной программы", libraryBook);
    }

    @DisplayName("возвращать ожидаемый список комментариев по книге")
    @Test
    void shouldReturnExpectedListCommentsByBook() {
        when(commentRepository.findAllByBook(book)).thenReturn(List.of(comment));
        when(bookService.toDomain(libraryBook)).thenReturn(book);
        when(bookService.toDto(book)).thenReturn(libraryBook);

        List<LibraryComment> actual = service.getListCommentsByBook(libraryBook);

        assertThat(actual).isNotEmpty().hasSize(1)
                .contains(libraryComment);
        verify(bookService, times(1)).toDomain(libraryBook);
        verify(commentRepository, times(1)).findAllByBook(book);
        verify(bookService, times(1)).toDto(book);
    }

    @DisplayName("сохранять комментарий к книге")
    @Test
    void shouldSaveComment() {
        when(commentRepository.save(comment)).thenReturn(comment);
        when(bookService.toDomain(libraryBook)).thenReturn(book);
        when(bookService.toDto(book)).thenReturn(libraryBook);

        LibraryComment actual = service.saveComment(libraryComment);

        assertThat(actual).isEqualTo(libraryComment);
        verify(commentRepository, times(1)).save(comment);
        verify(bookService, times(1)).toDomain(libraryBook);
        verify(bookService, times(1)).toDto(book);
    }

    @DisplayName("возвращать комментарий по его идентификатору")
    @Test
    void shouldReturnExpectedCommentById() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(bookService.toDto(book)).thenReturn(libraryBook);

        LibraryComment actual = service.getCommentById(libraryComment.getNumber());

        assertThat(actual).isEqualTo(libraryComment);
        verify(commentRepository, times(1)).findById(comment.getId());
        verify(bookService, times(1)).toDto(book);
    }

    @DisplayName("возвращать ошибку, если комментарий не найден по переданному идентификатору")
    @Test
    void shouldReturnEntityNotFoundException() {
        when(commentRepository.findById(ERROR_COMMENT_ID)).thenReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException("Комментарий по книге", "");

        assertThatThrownBy(() -> service.getCommentById(ERROR_COMMENT_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());

        verify(commentRepository, times(1)).findById(ERROR_COMMENT_ID);
    }

    @DisplayName("удалять комментарий")
    @Test
    void shouldDeleteComment() {
        when(bookService.toDomain(libraryBook)).thenReturn(book);
        service.deleteComment(libraryComment);

        verify(commentRepository, times(1)).delete(comment);
        verify(bookService, times(1)).toDomain(libraryBook);
    }

}