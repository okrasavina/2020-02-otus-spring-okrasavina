package ru.otus.spring.service;

import org.springframework.boot.CommandLineRunner;

public interface TestService extends CommandLineRunner {

    void run(String... args) throws Exception;
}
