package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Сервис для работы с локалью должен")
@SpringBootTest(classes = {LocaleServiceImpl.class})
class LocaleServiceImplTest {

    public static final String CHANGE_LOCALE_TAG = "en";
    public static final String LOCALE_CHANGE_MESSAGE = "The locale was successfully changed.";

    @Configuration
    @ComponentScan("ru.otus.spring.service")
    private static class Config {
    }

    @MockBean
    private MessageSource messageSource;

    private LocaleServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new LocaleServiceImpl(messageSource);
    }

    @DisplayName("менять локаль по переданному тегу")
    @Test
    void shouldChangeLocaleByLanguageTag() {
        given(messageSource.getMessage("locale.change", null,
                Locale.ENGLISH)).willReturn(LOCALE_CHANGE_MESSAGE);

        String messageActual = service.changeLocale(CHANGE_LOCALE_TAG);

        assertThat(messageActual).isEqualTo(LOCALE_CHANGE_MESSAGE);
        verify(messageSource, times(1))
                .getMessage("locale.change", null, Locale.ENGLISH);
    }
}