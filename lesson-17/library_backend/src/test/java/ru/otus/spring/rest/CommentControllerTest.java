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
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с комментариями должен")
@WebMvcTest
@ContextConfiguration(classes = CommentController.class)
class CommentControllerTest {

    private LibraryBook book;
    private LibraryComment comment;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void setUp() {
        book = new LibraryBook(1L, "Мертвые души", "История дела Чичикова",
                "Николай Гоголь", "Роман");
        comment = new LibraryComment(1L, "Школьная программа", book);
    }

    @SneakyThrows
    @DisplayName("выводить список комментариев по книге")
    @Test
    void shouldShowListCommentByBook() {
        when(bookService.getBookById(book.getNumber())).thenReturn(book);
        when(commentService.getListCommentsByBook(book)).thenReturn(List.of(comment));

        mvc.perform(get("/api/comment/{bookNumber}", book.getNumber()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].number", is(comment.getNumber().intValue())))
                .andExpect(jsonPath("$[0].textComment", is(comment.getTextComment())));

        verify(bookService, times(1)).getBookById(book.getNumber());
        verify(commentService, times(1)).getListCommentsByBook(book);
    }

    @SneakyThrows
    @DisplayName("возвращать данные по комментарию по его идентификатору")
    @Test
    void shouldReturnExpectedCommentByNumber() {
        when(commentService.getCommentById(comment.getNumber())).thenReturn(comment);

        mvc.perform(get("/api/comment/get/{number}", comment.getNumber()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.number", is(comment.getNumber().intValue())))
                .andExpect(jsonPath("$.textComment", is(comment.getTextComment())));

        verify(commentService, times(1)).getCommentById(comment.getNumber());
    }

    @SneakyThrows
    @DisplayName("сохранять изменения по комментарию")
    @Test
    void shouldSaveCommentAndRedirect() {
        when(bookService.getBookById(book.getNumber())).thenReturn(book);
        when(commentService.saveComment(new LibraryComment(book))).thenReturn(comment);

        mvc.perform(post("/api/comment/{bookNumber}", book.getNumber())
                .content(new ObjectMapper().writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookService, times(1)).getBookById(book.getNumber());
        verify(commentService, times(1)).saveComment(comment);
    }

    @SneakyThrows
    @DisplayName("удалять существующий комментарий")
    @Test
    void shouldDeleteComment() {
        when(bookService.getBookById(book.getNumber())).thenReturn(book);

        mvc.perform(delete("/api/comment/{bookNumber}", book.getNumber())
                .content(new ObjectMapper().writeValueAsString(comment))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookService, times(1)).getBookById(book.getNumber());
        verify(commentService, times(1)).deleteComment(comment);
    }

}