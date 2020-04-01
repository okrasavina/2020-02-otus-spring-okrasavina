package ru.otus.spring.service;

import au.com.bytecode.opencsv.CSVReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.ApplicationSettings;
import ru.otus.spring.dao.StudentDao;
import ru.otus.spring.domain.Student;

import java.io.*;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private static final int COUNT_QUESTIONS = 5;
    private final StudentDao studentDao;
    private final MessageSource messageSource;
    private final ConsoleReader consoleReader;
    private final ApplicationSettings applicationSettings;

    @Getter
    private Student student;

    @Override
    public void run() throws Exception{
        for (int i = 0; i < COUNT_QUESTIONS; i++) {
            getNextQuestion(i);
            String answer = consoleReader.getNextLine();
            checkStudentAnswer(student, answer, i);
        }

    }

    @Override
    public String welcome(String firstName, String lastName) {
        this.student = studentDao.addNewStudent(firstName, lastName);
        return messageSource.getMessage("test.welcome", new String[]{firstName, lastName}, applicationSettings.getLocale());
    }

    @Override
    public String getResult() {
        return messageSource.getMessage("student.result", new String[]{studentDao.getFullName(student), studentDao.getTestResultByStudent(student).toString()}, applicationSettings.getLocale());
    }

    private void getNextQuestion(int countLine) throws IOException {
        String questionResourceFileName = messageSource.getMessage("file.question", null, applicationSettings.getLocale());

        try (CSVReader readerTest = new CSVReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(questionResourceFileName)), ';', '"', countLine)) {
            String[] nextLine = readerTest.readNext();
            if (nextLine != null) {
                for (String line : nextLine
                ) {
                    System.out.println(line);
                }
            }
        }
    }

    private void checkStudentAnswer(Student student, String answer, int countLine) throws IOException {

        try (CSVReader readerAnswer = new CSVReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(applicationSettings.getAnswerFileName())), ';', '"', countLine)) {
            String[] nextLine = readerAnswer.readNext();
            if (nextLine != null) {
                if (nextLine[0].equals(answer)) {
                    studentDao.addPointToTestResult(student);
                }
            }
        }
    }

}
