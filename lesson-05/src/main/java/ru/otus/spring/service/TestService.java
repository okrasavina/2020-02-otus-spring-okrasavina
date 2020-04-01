package ru.otus.spring.service;

import ru.otus.spring.domain.Student;

public interface TestService {

    void run() throws Exception;

    String welcome(String firstName, String lastName);

    String getResult();

    Student getStudent();

}
