package ru.otus.spring.rest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.service.BookService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с книгами должен")
@WebMvcTest
@WithMockUser
@ContextConfiguration(classes = BookController.class)
class BookControllerTest {

    private LibraryBook libraryBook;
    private LibraryGenre libraryGenre;
    private LibraryAuthor libraryAuthor;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        libraryAuthor = new LibraryAuthor(1L, "Николай Гоголь", LocalDate.of(1809, 03, 19), false);
        libraryGenre = new LibraryGenre(1L, "Роман", "Литературный жанр", false);
        libraryBook = new LibraryBook(1L, "Мертвые души", "Авантюра Чичикова",
                List.of(libraryAuthor.getName()), List.of(libraryGenre.getName()));
    }

    @SneakyThrows
    @DisplayName("показывать страницу приветствия")
    @Test
    void shouldShowWelcomePage() {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @SneakyThrows
    @DisplayName("показывать страницу поиска книг по названию жанра")
    @Test
    void shouldShowPageForSearchBooksByGenreName() {
        mvc.perform(get("/search/genre"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-by-genre"))
                .andExpect(model().attribute("genre", new LibraryGenre()));
    }

    @SneakyThrows
    @DisplayName("показывать список книг по названию жанра")
    @Test
    void shouldShowListBooksByGenreName() {
        when(bookService.getListBooksByGenreName(libraryGenre.getName())).thenReturn(List.of(libraryBook));

        mvc.perform(get("/search/genre/book")
                .param("name", libraryGenre.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attribute("books", List.of(libraryBook)))
                .andExpect(model().attribute("header", String.format("Список книг по жанру '%s'", libraryGenre.getName())))
                .andExpect(model().attribute("isNew", false));

        verify(bookService, times(1)).getListBooksByGenreName(libraryGenre.getName());
    }

    @SneakyThrows
    @DisplayName("показывать страницу поиска книг по имени автора")
    @Test
    void shouldShowPageForSearchBooksByAuthorName() {
        mvc.perform(get("/search/author"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-by-author"))
                .andExpect(model().attribute("author", new LibraryAuthor()));
    }

    @SneakyThrows
    @DisplayName("показывать список книг по имени автора")
    @Test
    void shouldShowListBooksByAuthorName() {
        when(bookService.getListBooksByAuthorName(libraryAuthor.getName())).thenReturn(List.of(libraryBook));

        mvc.perform(get("/search/author/book")
                .param("name", libraryAuthor.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attribute("books", List.of(libraryBook)))
                .andExpect(model().attribute("header", String.format("Список книг по автору '%s'", libraryAuthor.getName())))
                .andExpect(model().attribute("isNew", false));

        verify(bookService, times(1)).getListBooksByAuthorName(libraryAuthor.getName());
    }

    @SneakyThrows
    @DisplayName("показывать страницу поиска книг по наименованию книги")
    @Test
    void shouldShowPageForSearchBooksByTitle() {
        mvc.perform(get("/search/title"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-by-title"))
                .andExpect(model().attribute("book", new LibraryBook()));
    }

    @SneakyThrows
    @DisplayName("показывать список книг по наименованию")
    @Test
    void shouldShowListBooksByTitle() {
        when(bookService.getListBooksByTitle("души")).thenReturn(List.of(libraryBook));

        mvc.perform(get("/search/title/book")
                .param("title", "души"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attribute("books", List.of(libraryBook)))
                .andExpect(model().attribute("header", "Список книг по названию 'души'"))
                .andExpect(model().attribute("isNew", false));

        verify(bookService, times(1)).getListBooksByTitle("души");
    }

    @SneakyThrows
    @DisplayName("показывать весь список книг")
    @Test
    void shouldShowListBooks() {
        when(bookService.getListBook()).thenReturn(List.of(libraryBook));

        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-list"))
                .andExpect(model().attribute("books", List.of(libraryBook)))
                .andExpect(model().attribute("header", "Список книг"))
                .andExpect(model().attribute("isNew", true));

        verify(bookService, times(1)).getListBook();
    }

    @SneakyThrows
    @DisplayName("открывать форму для добавления новой книги")
    @Test
    void shouldShowFormForAddingNewBook() {
        mvc.perform(get("/book/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-edit"))
                .andExpect(model().attribute("book", new LibraryBook()));
    }

    @SneakyThrows
    @DisplayName("открывать форму для редактирования существуюшей книги")
    @Test
    void shouldShowFormForEditExistingBook() {
        when(bookService.getBookById(libraryBook.getNumber())).thenReturn(libraryBook);

        mvc.perform(get("/book/edit")
                .param("number", Long.toString(libraryBook.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("book-edit"))
                .andExpect(model().attribute("book", libraryBook));

        verify(bookService, times(1)).getBookById(libraryBook.getNumber());
    }

    @SneakyThrows
    @DisplayName("сохранять изменения по книге и перенаправлять на список книг")
    @Test
    void shouldSaveBookAndRedirect() {
        when(bookService.saveBook(new LibraryBook())).thenReturn(libraryBook);

        mvc.perform(post("/book/edit")
                .param("save", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book"));

        verify(bookService, times(1)).saveBook(new LibraryBook());
    }

    @SneakyThrows
    @DisplayName("отменять изменения по книге и перенаправлять на список книг")
    @Test
    void shouldCancelChangingBookAndRedirect() {
        mvc.perform(post("/book/edit")
                .param("cancel", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book"));
    }

    @SneakyThrows
    @DisplayName("открывать форму для удаления существуюшей книги")
    @Test
    void shouldShowFormForDeleteExistingBook() {
        when(bookService.getBookById(libraryBook.getNumber())).thenReturn(libraryBook);

        mvc.perform(get("/book/delete")
                .param("number", Long.toString(libraryBook.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("book-delete"))
                .andExpect(model().attribute("book", libraryBook));

        verify(bookService, times(1)).getBookById(libraryBook.getNumber());
    }

    @SneakyThrows
    @DisplayName("удалять существующую книгу и перенаправлять на список книг")
    @Test
    void shouldDeleteBookAndRedirect() {
        mvc.perform(post("/book/delete")
                .param("delete", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book"));

        verify(bookService, times(1)).deleteBook(new LibraryBook());
    }

    @SneakyThrows
    @DisplayName("отменять удаление книги и перенаправлять на список книг")
    @Test
    void shouldCancelDeletingBookAndRedirect() {
        mvc.perform(post("/book/delete")
                .param("cancel", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/book"));
    }
}