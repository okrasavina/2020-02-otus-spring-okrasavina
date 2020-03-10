package ru.otus.spring.dao;

import ru.otus.spring.domain.Student;

public interface StudentDao {

    Student addNewStudent(String firstName, String lastName);

    void addPointToTestResult(Student student) throws StudentNotFoundException;

    Integer getTestResultByStudent(Student student) throws StudentNotFoundException;

}
