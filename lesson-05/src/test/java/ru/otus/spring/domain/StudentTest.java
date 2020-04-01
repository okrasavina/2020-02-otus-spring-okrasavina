package ru.otus.spring.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("класс Student")
class StudentTest {

    @DisplayName("должен корректно создавать новый экземпляр")
    @Test
    void shouldCorrectConstructor() {
        Student maxim = new Student("Maxim", "Ivanov");

        assertThat(maxim.getFirstName()).isEqualTo("Maxim");
        assertThat(maxim.getLastName()).isEqualTo("Ivanov");
    }
}