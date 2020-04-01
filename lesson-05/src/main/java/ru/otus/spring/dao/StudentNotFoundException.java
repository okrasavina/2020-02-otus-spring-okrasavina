package ru.otus.spring.dao;

import ru.otus.spring.domain.Student;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Student student) {
        super("Student " + student.getFirstName() + " " + student.getLastName() + " not found.");
    }
}
