package ru.otus.spring.controller;

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
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с комментариями должен")
@WebMvcTest
@WithMockUser
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
                List.of("Николай Гоголь"), List.of("Роман"));
        comment = new LibraryComment(1L, "Школьная программа", book);
    }

    @SneakyThrows
    @DisplayName("выводить список комментариев по книге")
    @Test
    void shouldShowListCommentByBook() {
        when(bookService.getBookById(book.getNumber())).thenReturn(book);
        when(commentService.getListCommentsByBook(book)).thenReturn(List.of(comment));

        mvc.perform(get("/comment")
                .param("number", Long.toString(book.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-list"))
                .andExpect(model().attribute("comments", List.of(comment)));

        verify(bookService, times(1)).getBookById(book.getNumber());
        verify(commentService, times(1)).getListCommentsByBook(book);
    }

    @SneakyThrows
    @DisplayName("открывать форму для добавления нового комментария")
    @Test
    void shouldShowFormForAddingNewComment() {
        when(bookService.getBookById(book.getNumber())).thenReturn(book);

        mvc.perform(get("/comment/new")
                .param("number", Long.toString(book.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-edit"))
                .andExpect(model().attribute("comment", new LibraryComment(book)));

        verify(bookService, times(1)).getBookById(book.getNumber());
    }

    @SneakyThrows
    @DisplayName("открывать форму для редактирования существуюшего комментария")
    @Test
    void shouldShowFormForEditExistingComment() {
        when(commentService.getCommentById(comment.getNumber())).thenReturn(comment);

        mvc.perform(get("/comment/edit")
                .param("number", Long.toString(comment.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-edit"))
                .andExpect(model().attribute("comment", comment));

        verify(commentService, times(1)).getCommentById(comment.getNumber());
    }

    @SneakyThrows
    @DisplayName("сохранять изменения по комментарию и перенаправлять на список комментариев")
    @Test
    void shouldSaveCommentAndRedirect() {
        when(bookService.getBookById(book.getNumber())).thenReturn(book);
        when(commentService.saveComment(new LibraryComment(book))).thenReturn(comment);

        mvc.perform(post("/comment/edit")
                .param("bookNumber", Long.toString(book.getNumber()))
                .param("comment", comment.toString())
                .param("save", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/comment?number=%d", book.getNumber())));

        verify(bookService, times(1)).getBookById(book.getNumber());
        verify(commentService, times(1)).saveComment(new LibraryComment(book));
    }

    @SneakyThrows
    @DisplayName("отменять изменения по комментарию и перенаправлять на список комментариев по книге")
    @Test
    void shouldCancelChangingCommentAndRedirect() {
        mvc.perform(post("/comment/edit")
                .param("bookNumber", Long.toString(book.getNumber()))
                .param("cancel", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/comment?number=%d", book.getNumber())));
    }

    @SneakyThrows
    @DisplayName("открывать форму для удаления существуюшего комментария")
    @Test
    void shouldShowFormForDeleteExistingComment() {
        when(commentService.getCommentById(comment.getNumber())).thenReturn(comment);

        mvc.perform(get("/comment/delete")
                .param("number", Long.toString(comment.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-delete"))
                .andExpect(model().attribute("comment", comment));

        verify(commentService, times(1)).getCommentById(comment.getNumber());
    }

    @SneakyThrows
    @DisplayName("удалять существующий комментарий и перенаправлять на список комментариев")
    @Test
    void shouldDeleteCommentAndRedirect() {
        when(bookService.getBookById(book.getNumber())).thenReturn(book);

        mvc.perform(post("/comment/delete")
                .param("bookNumber", Long.toString(book.getNumber()))
                .param("comment", comment.toString())
                .param("delete", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/comment?number=%d", book.getNumber())));

        verify(bookService, times(1)).getBookById(book.getNumber());
        verify(commentService, times(1)).deleteComment(new LibraryComment(book));
    }

    @SneakyThrows
    @DisplayName("отменять удаление комментария и перенаправлять на список комментариев")
    @Test
    void shouldCancelDeletingCommentAndRedirect() {
        mvc.perform(post("/comment/delete")
                .param("bookNumber", Long.toString(book.getNumber()))
                .param("cancel", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/comment?number=%d", book.getNumber())));
    }

}