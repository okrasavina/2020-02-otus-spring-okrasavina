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
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.service.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с жанрами должен")
@WebMvcTest
@WithMockUser
@ContextConfiguration(classes = GenreController.class)
class GenreControllerTest {

    private LibraryGenre genre;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        genre = new LibraryGenre(1L, "Комедия", "Литературный жанр", false);
    }

    @SneakyThrows
    @DisplayName("выводить список жанров")
    @Test
    void shouldShowListGenre() {
        List<LibraryGenre> genreList = new ArrayList<>(List.of(genre));
        when(genreService.getListGenre()).thenReturn(genreList);

        this.mvc.perform(get("/genre"))
                .andExpect(view().name("genre-list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", genreList));

        verify(genreService, times(1)).getListGenre();

    }

    @SneakyThrows
    @DisplayName("открывать форму для добавления нового жанра")
    @Test
    void shouldShowFormForAddingNewGenre() {
        mvc.perform(get("/genre/new"))
                .andExpect(view().name("genre-edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genre", new LibraryGenre()))
                .andExpect(model().attribute("notNew", false));
    }

    @SneakyThrows
    @DisplayName("открывать форму для редактирования существующего жанра")
    @Test
    void shouldShowFormForEditExistingGenre() {
        when(genreService.getGenreById(genre.getNumber())).thenReturn(genre);

        mvc.perform(get("/genre/edit")
                .param("number", Long.toString(genre.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("genre-edit"))
                .andExpect(model().attribute("genre", genre))
                .andExpect(model().attribute("notNew", true));

        verify(genreService, times(1)).getGenreById(genre.getNumber());
    }

    @SneakyThrows
    @DisplayName("сохранять изменения по жанру")
    @Test
    void shouldSaveGenreAndRedirect() {
        when(genreService.saveGenre(new LibraryGenre())).thenReturn(genre);

        mvc.perform(post("/genre/edit")
                .param("genre", genre.toString())
                .param("save", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre"));

        verify(genreService, times(1)).saveGenre(new LibraryGenre());
    }

    @SneakyThrows
    @DisplayName("отменять изменения по жанру")
    @Test
    void shouldCancelChangingGenreAndRedirect() {
        mvc.perform(post("/genre/edit")
                .param("genre", genre.toString())
                .param("cancel", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre"));
    }

    @SneakyThrows
    @DisplayName("открывать форму для удаления существующего жанра")
    @Test
    void shouldShowFormForDeleteExistingGenre() {
        when(genreService.getGenreById(genre.getNumber())).thenReturn(genre);

        mvc.perform(get("/genre/delete")
                .param("number", Long.toString(genre.getNumber())))
                .andExpect(status().isOk())
                .andExpect(view().name("genre-delete"))
                .andExpect(model().attribute("genre", genre));

        verify(genreService, times(1)).getGenreById(genre.getNumber());
    }

    @SneakyThrows
    @DisplayName("удалять существующий жанр и перенаправлять на форму со списком жанров")
    @Test
    void shouldDeleteGenreAndRedirect() {
        mvc.perform(post("/genre/delete")
                .param("genre", genre.toString())
                .param("delete", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre"));

        verify(genreService, times(1)).deleteGenre(new LibraryGenre());
    }

    @SneakyThrows
    @DisplayName("отменять удаление жанра")
    @Test
    void shouldCancelDeletingGenreAndRedirect() {
        mvc.perform(post("/genre/delete")
                .param("genre", genre.toString())
                .param("cancel", "true")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/genre"));
    }

}