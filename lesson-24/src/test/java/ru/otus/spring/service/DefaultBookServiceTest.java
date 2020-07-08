package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Сервис по работе с книгами должен")
@ExtendWith(MockitoExtension.class)
class DefaultBookServiceTest {

    private LibraryBook libraryBook;
    private Book book;
    private Genre genre;
    private Author author;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private CommentRepository commentRepository;

    private DefaultBookService service;

    @BeforeEach
    void setUp() {
        service = new DefaultBookService(bookRepository, genreRepository, authorRepository, commentRepository);
        author = new Author(1L, "Николай Гоголь", LocalDate.of(1809, 03, 19));
        genre = new Genre(1L, "Роман", "Литературный жанр");
        book = new Book(1L, "Мертвые души", "Авантюра Чичикова",
                List.of(author), List.of(genre));
        libraryBook = new LibraryBook(1L, "Мертвые души", "Авантюра Чичикова",
                List.of(author.getName()), List.of(genre.getName()));
    }

    @DisplayName("возвращать ожидаемый список книг по названию жанра")
    @Test
    void shouldReturnExpectedListBooksByGenreName() {
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        when(bookRepository.findAllByGenresContaining(genre)).thenReturn(List.of(book));

        List<LibraryBook> actual = service.getListBooksByGenreName(genre.getName());

        assertThat(actual).isNotEmpty()
                .hasSize(1)
                .contains(libraryBook);

        verify(genreRepository, times(1)).findByName(genre.getName());
        verify(bookRepository, times(1)).findAllByGenresContaining(genre);

    }

    @DisplayName("возвращать ошибку, если передать название жанра, которого нет в базе данных")
    @Test
    void shouldReturnExceptionIfGenreNotFound() {
        when(genreRepository.findByName("Сатира")).thenReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException("Жанр", "");

        assertThatThrownBy(() -> service.getListBooksByGenreName("Сатира"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());

        verify(genreRepository, times(1)).findByName("Сатира");
    }

    @DisplayName("возвращать ожидаемый список книг по имени автора")
    @Test
    void shouldReturnExpectedListBooksByAuthorName() {
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));
        when(bookRepository.findAllByAuthorsContaining(author)).thenReturn(List.of(book));

        List<LibraryBook> actual = service.getListBooksByAuthorName(author.getName());

        assertThat(actual).isNotEmpty()
                .hasSize(1)
                .contains(libraryBook);

        verify(authorRepository, times(1)).findByName(author.getName());
        verify(bookRepository, times(1)).findAllByAuthorsContaining(author);

    }

    @DisplayName("возвращать ошибку, если передать имя автора, которого нет в базе данных")
    @Test
    void shouldReturnExceptionIfAuthorNotFound() {
        when(authorRepository.findByName("Лев Толстой")).thenReturn(Optional.empty());
        Throwable exception = new EntityNotFoundException("Автор", "");

        assertThatThrownBy(() -> service.getListBooksByAuthorName("Лев Толстой"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());

        verify(authorRepository, times(1)).findByName("Лев Толстой");
    }

    @DisplayName("возвращать ожидаемый список книг по названию")
    @Test
    void shouldReturnExpectedListBooksByTitle() {
        Book bookSecond = new Book(2L, "Вечера на хуторе близ Диканьки", "Фантастические истории",
                List.of(author), List.of(genre));
        LibraryBook libraryBookSecond = new LibraryBook(2L, "Вечера на хуторе близ Диканьки", "Фантастические истории",
                List.of(author.getName()), List.of(genre.getName()));
        when(bookRepository.findAllByTitleContaining("на Хуторе")).thenReturn(List.of(bookSecond));

        List<LibraryBook> actual = service.getListBooksByTitle("на Хуторе");

        assertThat(actual).isNotEmpty()
                .hasSize(1)
                .contains(libraryBookSecond)
                .doesNotContain(libraryBook);

        verify(bookRepository, times(1)).findAllByTitleContaining("на Хуторе");
    }

    @DisplayName("удалять переданную книгу")
    @Test
    void shouldDeleteBook() {
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(genre);
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);

        service.deleteBook(libraryBook);

        verify(genreRepository, times(1)).findByName(genre.getName());
        verify(genreRepository, times(1)).save(genre);
        verify(authorRepository, times(1)).findByName(author.getName());
        verify(authorRepository, times(1)).save(author);
        verify(bookRepository, times(1)).deleteById(libraryBook.getNumber());
        verify(commentRepository, times(1)).deleteAllByBook(book);
    }

    @DisplayName("возвращать полный список книг из базы данных")
    @Test
    void shouldReturnExpectedListBooks() {
        Book bookSecond = new Book(2L, "Вечера на хуторе близ Диканьки", "Фантастические истории",
                List.of(author), List.of(genre));
        LibraryBook libraryBookSecond = new LibraryBook(2L, "Вечера на хуторе близ Диканьки", "Фантастические истории",
                List.of(author.getName()), List.of(genre.getName()));
        when(bookRepository.findAll()).thenReturn(List.of(book, bookSecond));

        List<LibraryBook> actual = service.getListBook();

        assertThat(actual).isNotEmpty()
                .hasSize(2)
                .contains(libraryBook, libraryBookSecond);

        verify(bookRepository, times(1)).findAll();
    }

    @DisplayName("возвращать книгу по переданному идентификатору")
    @Test
    void shouldReturnExpectedBookById() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        LibraryBook actual = service.getBookById(book.getId());

        assertThat(actual).isEqualTo(libraryBook);
        verify(bookRepository, times(1)).findById(book.getId());
    }

    @DisplayName("возвращать ошибку, если передать несуществующий идентификатор")
    @Test
    void shouldReturnExceptionIfBookNotFoundById() {
        when(bookRepository.findById(10L)).thenReturn(Optional.empty());

        Throwable exception = new EntityNotFoundException("Книга", "а");

        assertThatThrownBy(() -> service.getBookById(10L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exception.getMessage());
        verify(bookRepository, times(1)).findById(10L);
    }

    @DisplayName("сохранять книгу в БД")
    @Test
    void shouldSaveBook() {
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(genre);
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(bookRepository.save(book)).thenReturn(book);

        LibraryBook actual = service.saveBook(libraryBook);

        assertThat(actual).isEqualTo(libraryBook);
        verify(genreRepository, times(1)).findByName(genre.getName());
        verify(genreRepository, times(1)).save(genre);
        verify(authorRepository, times(1)).findByName(author.getName());
        verify(authorRepository, times(1)).save(author);
        verify(bookRepository, times(1)).save(book);
    }

    @DisplayName("возвращать DTO по переданному сущности базы данных")
    @Test
    void shouldReturnExpectedDtoBookByDomainBook() {
        LibraryBook actual = service.toDto(book);

        assertThat(actual).isEqualTo(libraryBook);
    }

    @DisplayName("возвращать сущность базы данных по переданному DTO")
    @Test
    void shouldReturnExpectedDomainBookByDtoBook() {
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        when(genreRepository.save(genre)).thenReturn(genre);
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);

        Book actual = service.toDomain(libraryBook);

        assertThat(actual).isEqualTo(book);
        verify(genreRepository, times(1)).findByName(genre.getName());
        verify(genreRepository, times(1)).save(genre);
        verify(authorRepository, times(1)).findByName(author.getName());
        verify(authorRepository, times(1)).save(author);
    }
}