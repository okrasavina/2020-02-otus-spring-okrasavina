package ru.otus.spring.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.LibraryUser;
import ru.otus.spring.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер для регистрации пользователей должен")
@WebMvcTest
@WithMockUser
@ContextConfiguration(classes = RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @SneakyThrows
    @DisplayName("открывать форму регистрации")
    @Test
    void shouldShowRegistrationForm() {
        this.mvc.perform(get("/registration"))
                .andExpect(view().name("registration"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", new LibraryUser()));

    }

    @SneakyThrows
    @DisplayName("сохранять нового пользователя и открывать форму логина")
    @Test
    void shouldRegisterNewUserAndShowLoginForm() {
        LibraryUser libraryUser = new LibraryUser("testUser", "test@mail.ru", "123456");

        when(userService.saveNewUser(new LibraryUser())).thenReturn(libraryUser);

        this.mvc.perform(post("/registration")
                .param("libraryUser", libraryUser.toString())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(userService, times(1)).saveNewUser(new LibraryUser());

    }
}