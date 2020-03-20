package ru.otus.spring.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ConsoleReaderImpl implements ConsoleReader {

    private final BufferedReader reader;

    public ConsoleReaderImpl() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String getNextLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}
