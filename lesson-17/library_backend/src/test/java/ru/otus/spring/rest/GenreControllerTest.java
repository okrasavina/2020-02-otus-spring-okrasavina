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
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.service.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с жанрами должен")
@WebMvcTest
@ContextConfiguration(classes = GenreController.class)
class GenreControllerTest {

    private LibraryGenre genre;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        genre = new LibraryGenre(1L, "Комедия", "Литературный жанр");
    }

    @SneakyThrows
    @DisplayName("выводить список жанров")
    @Test
    void shouldShowListGenre() {
        List<LibraryGenre> genreList = new ArrayList<>(List.of(genre));
        when(genreService.getListGenre()).thenReturn(genreList);

        this.mvc.perform(get("/api/genre"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].number", is(genre.getNumber().intValue())))
                .andExpect(jsonPath("$[0].name", is(genre.getName())))
                .andExpect(jsonPath("$[0].description", is(genre.getDescription())));

        verify(genreService, times(1)).getListGenre();

    }

    @SneakyThrows
    @DisplayName("возвращать данные по жанру по его идентификатору")
    @Test
    void shouldReturnExpectedGenreByNumber() {
        when(genreService.getGenreById(genre.getNumber())).thenReturn(genre);

        mvc.perform(get("/api/genre/{number}", genre.getNumber()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.number", is(genre.getNumber().intValue())))
                .andExpect(jsonPath("$.name", is(genre.getName())))
                .andExpect(jsonPath("$.description", is(genre.getDescription())));

        verify(genreService, times(1)).getGenreById(genre.getNumber());
    }

    @SneakyThrows
    @DisplayName("сохранять изменения по жанру")
    @Test
    void shouldSaveGenre() {
        when(genreService.saveGenre(new LibraryGenre())).thenReturn(genre);

        mvc.perform(post("/api/genre/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(genre)))
                .andExpect(status().isOk());

        verify(genreService, times(1)).saveGenre(genre);
    }

    @SneakyThrows
    @DisplayName("удалять существующий жанр")
    @Test
    void shouldDeleteGenre() {
        mvc.perform(delete("/api/genre/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(genre)))
                .andExpect(status().isOk());

        verify(genreService, times(1)).deleteGenre(genre);
    }

}