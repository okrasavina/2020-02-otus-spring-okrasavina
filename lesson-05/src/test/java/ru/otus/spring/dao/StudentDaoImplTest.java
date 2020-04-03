package ru.otus.spring.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("класс StudentDaoImpl")
class StudentDaoImplTest {

    private StudentDao dao;

    @BeforeEach
    void setUp() {
        this.dao = new StudentDaoImpl();
    }

    @DisplayName("должен добавлять нового студента в мапу")
    @Test
    void shouldCorrectAddNewStudent() {
        Student olga = new Student("Olga", "Krasavina");
        Student addStudent = dao.addNewStudent("Olga", "Krasavina");

        assertThat(addStudent).isEqualTo(olga);
    }

    @DisplayName("корректное обновление результата теста по студенту")
    @Test
    void shouldCorrectAddPointToTestResult() {
        Student olga = dao.addNewStudent("Olga", "Krasavina");
        dao.addPointToTestResult(olga);

        assertThat(dao.getTestResultByStudent(olga)).isEqualTo(1);
    }

    @DisplayName("обновление результата теста корректно выдает ошибку")
    @Test
    void shouldAddPointToTestResultCorrectThrowException() {
        Student maxim = new Student("Maxim", "Bobrov");
        dao.addNewStudent("Olga", "Krasavina");

        assertThatThrownBy(() -> dao.addPointToTestResult(maxim)).isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Student " + maxim.getFirstName() + " " + maxim.getLastName() + " not found.");

    }

}