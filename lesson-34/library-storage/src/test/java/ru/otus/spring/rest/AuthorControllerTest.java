package ru.otus.spring.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.service.AuthorService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для работы с авторами должен")
@WebMvcTest
@ContextConfiguration(classes = AuthorController.class)
class AuthorControllerTest {

    private LibraryAuthor author;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        author = new LibraryAuthor(1L, "Илья Ильф", LocalDate.of(1897, 10, 15));
    }

    @SneakyThrows
    @DisplayName("выводить список авторов")
    @Test
    void shouldShowListAuthors() {
        List<LibraryAuthor> authorList = new ArrayList<>(List.of(author));
        when(authorService.getListAuthor()).thenReturn(authorList);

        mvc.perform(get("/api/author"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].number", is(author.getNumber().intValue())))
                .andExpect(jsonPath("$[0].name", is(author.getName())))
                .andExpect(jsonPath("$[0].birthDay", is(author.getBirthDay().toString())));

        verify(authorService, times(1)).getListAuthor();
    }

    @SneakyThrows
    @DisplayName("возвращать данные по автору по его идентификатору")
    @Test
    void shouldReturnExpectedAuthorByNumber() {
        when(authorService.getAuthorById(author.getNumber())).thenReturn(author);

        mvc.perform(get("/api/author/{number}", author.getNumber()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.number", is(author.getNumber().intValue())))
                .andExpect(jsonPath("$.name", is(author.getName())))
                .andExpect(jsonPath("$.birthDay", is(author.getBirthDay().toString())));

        verify(authorService, times(1)).getAuthorById(author.getNumber());
    }

    @SneakyThrows
    @DisplayName("сохранять изменения по переданному автору")
    @Test
    void shouldSaveAuthor() {
        when(authorService.saveAuthor(new LibraryAuthor())).thenReturn(author);
        System.out.println(new ObjectMapper().writeValueAsString(author));

        mvc.perform(post("/api/author/")
                .content(getAuthorContent())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authorService, times(1)).saveAuthor(author);
    }

    @SneakyThrows
    @DisplayName("удалять существующего автора")
    @Test
    void shouldDeleteAuthor() {
        mvc.perform(delete("/api/author/")
                .content(getAuthorContent())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authorService, times(1)).deleteAuthor(author);
    }

    private String getAuthorContent() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(author);
    }


}