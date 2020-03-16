package ru.otus.spring.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("класс Student")
class StudentTest {

    @DisplayName("должен корректно создавать новый экземпляр")
    @Test
    void shouldCorrectConstructor() {
        Student maxim = new Student("Maxim", "Ivanov");

        Assertions.assertThat(maxim.getFirstName()).isEqualTo("Maxim");
        Assertions.assertThat(maxim.getLastName()).isEqualTo("Ivanov");
    }
}