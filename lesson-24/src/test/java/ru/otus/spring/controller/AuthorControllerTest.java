package ru.otus.spring.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.service.AuthorService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с авторами должен")
@WebMvcTest
@WithMockUser
@ContextConfiguration(classes = AuthorController.class)
class AuthorControllerTest {

    private LibraryAuthor author;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        author = new LibraryAuthor(1L, "Илья Ильф", LocalDate.of(1897, 10, 15), false);
    }

    @SneakyThrows
    @DisplayName("выводить список авторов")
    @Test
    void shouldShowListAuthors() {
        List<LibraryAuthor> authorList = new ArrayList<>(List.of(author));
        when(authorService.getListAuthor()).thenReturn(authorList);

        mvc.perform(get("/author"))
                .andExpect(status().isOk())
                .andExpect(view().name("author-list"))
                .andExpect(model().attribute("authors", authorList));

        verify(authorService, times(1)).getListAuthor();
    }

    @SneakyThrows
    @DisplayName("открывать форму для добавления нового автора")
    @Test
    void shouldShowFormForAddingNewAuthor() {
        mvc.perform(get("/author/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("author-edit"))
                .andExpect(model().attribute("author", new LibraryAuthor()))
                .andExpect(model().attribute("notNew", false));
    }

    @SneakyThrows
    @DisplayName("открывать форму для редактирования существуюшего автора")
    @Test
    void shouldShowFormForEditExistingAuthor() {
        when(authorService.getAuthorById(author.getNumber())).thenReturn(author);

        mvc.perform(get("/author/edit")
                .param("number", Long.toString(author.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("author-edit"))
                .andExpect(model().attribute("author", author))
                .andExpect(model().attribute("notNew", true));

        verify(authorService, times(1)).getAuthorById(author.getNumber());
    }

    @SneakyThrows
    @DisplayName("сохранять изменения по автору и перенаправлять на список авторов")
    @Test
    void shouldSaveAuthorAndRedirect() {
        when(authorService.saveAuthor(new LibraryAuthor())).thenReturn(author);

        mvc.perform(post("/author/edit")
                .param("author", author.toString())
                .param("save", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author"));

        verify(authorService, times(1)).saveAuthor(new LibraryAuthor());
    }

    @SneakyThrows
    @DisplayName("отменять изменения по автору и перенаправлять на список авторов")
    @Test
    void shouldCancelChangingAuthorAndRedirect() {
        mvc.perform(post("/author/edit")
                .param("author", author.toString())
                .param("cancel", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author"));
    }

    @SneakyThrows
    @DisplayName("открывать форму для удаления существуюшего автора")
    @Test
    void shouldShowFormForDeleteExistingAuthor() {
        when(authorService.getAuthorById(author.getNumber())).thenReturn(author);

        mvc.perform(get("/author/delete")
                .param("number", Long.toString(author.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("author-delete"))
                .andExpect(model().attribute("author", author));

        verify(authorService, times(1)).getAuthorById(author.getNumber());
    }

    @SneakyThrows
    @DisplayName("удалять существующего автора и перенаправлять на список авторов")
    @Test
    void shouldDeleteAuthorAndRedirect() {
        mvc.perform(post("/author/delete")
                .param("author", author.toString())
                .param("delete", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author"));

        verify(authorService, times(1)).deleteAuthor(new LibraryAuthor());
    }

    @SneakyThrows
    @DisplayName("отменять удаление автора и перенаправлять на список авторов")
    @Test
    void shouldCancelDeletingAuthorAndRedirect() {
        mvc.perform(post("/author/delete")
                .param("author", author.toString())
                .param("cancel", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/author"));
    }

}