package ru.otus.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.service.BookService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с книгами должен")
@WebMvcTest
@ContextConfiguration(classes = BookController.class)
class BookControllerTest {

    private LibraryBook libraryBook;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        libraryBook = new LibraryBook(1L, "Мертвые души", "Авантюра Чичикова",
                "Николай Гоголь", "Роман");
    }

    @SneakyThrows
    @DisplayName("показывать весь список книг")
    @Test
    void shouldShowListBooks() {
        when(bookService.getListBook()).thenReturn(List.of(libraryBook));

        mvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].number", is(libraryBook.getNumber().intValue())))
                .andExpect(jsonPath("$[0].title", is(libraryBook.getTitle())))
                .andExpect(jsonPath("$[0].authors", is(libraryBook.getAuthors())))
                .andExpect(jsonPath("$[0].genres", is(libraryBook.getGenres())))
                .andExpect(jsonPath("$[0].description", is(libraryBook.getDescription())));

        verify(bookService, times(1)).getListBook();
    }

    @SneakyThrows
    @DisplayName("возвращать данные по книге по ее идентификатору")
    @Test
    void shouldReturnExpectedBookByNumber() {
        when(bookService.getBookById(libraryBook.getNumber())).thenReturn(libraryBook);

        mvc.perform(get("/api/book/{number}", libraryBook.getNumber()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.number", is(libraryBook.getNumber().intValue())))
                .andExpect(jsonPath("$.title", is(libraryBook.getTitle())))
                .andExpect(jsonPath("$.authors", is(libraryBook.getAuthors())))
                .andExpect(jsonPath("$.genres", is(libraryBook.getGenres())))
                .andExpect(jsonPath("$.description", is(libraryBook.getDescription())));

        verify(bookService, times(1)).getBookById(libraryBook.getNumber());
    }

    @SneakyThrows
    @DisplayName("сохранять изменения по книге")
    @Test
    void shouldSaveBook() {
        when(bookService.saveBook(new LibraryBook())).thenReturn(libraryBook);

        mvc.perform(post("/api/book/")
                .content(new ObjectMapper().writeValueAsString(libraryBook))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookService, times(1)).saveBook(libraryBook);
    }

    @SneakyThrows
    @DisplayName("удалять существующую книгу")
    @Test
    void shouldDeleteBook() {
        mvc.perform(delete("/api/book/")
                .content(new ObjectMapper().writeValueAsString(libraryBook))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteBook(libraryBook);
    }

}