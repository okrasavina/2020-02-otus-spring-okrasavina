package ru.otus.spring.service;

import java.io.IOException;

public interface ConsoleReader {

    String getNextLine() throws IOException;
}
