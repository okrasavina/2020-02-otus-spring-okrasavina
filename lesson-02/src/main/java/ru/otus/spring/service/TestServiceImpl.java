package ru.otus.spring.service;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.StudentDao;
import ru.otus.spring.domain.Student;

import java.io.*;
import java.util.Locale;

@Service
public class TestServiceImpl implements TestService {

    private static final int COUNT_QUESTIONS = 5;
    private static final Locale CURRENT_LOCALE = Locale.getDefault();
    private final StudentDao studentDao;
    private final String questionResourceFileName;
    private final String answerResourceFileName;
    private final MessageSource messageSource;
    private final ConsoleReader consoleReader;

    public TestServiceImpl(StudentDao studentDao, @Value("${file.answer}") String answerResourceFile, MessageSource messageSource, ConsoleReader consoleReader) {
        this.studentDao = studentDao;
        this.answerResourceFileName = answerResourceFile;
        this.messageSource = messageSource;
        this.questionResourceFileName = messageSource.getMessage("file.question", null, CURRENT_LOCALE);
        this.consoleReader = consoleReader;
    }

    @Override
    public void run() throws IOException {
        System.out.println(messageSource.getMessage("student.first.name", null, CURRENT_LOCALE));
        String firstName = consoleReader.getNextLine();
        System.out.println(messageSource.getMessage("student.last.name", null, CURRENT_LOCALE));
        String lastName = consoleReader.getNextLine();

        Student student = studentDao.addNewStudent(firstName, lastName);

        for (int i = 0; i < COUNT_QUESTIONS; i++) {
            getNextQuestion(i);
            String answer = consoleReader.getNextLine();
            checkStudentAnswer(student, answer, i);
        }

        consoleReader.close();
        System.out.println(messageSource.getMessage("student.result", new String[]{studentDao.getFullName(student), studentDao.getTestResultByStudent(student).toString()}, CURRENT_LOCALE));
    }

    private void getNextQuestion(int countLine) throws IOException {

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

        try (CSVReader readerAnswer = new CSVReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(answerResourceFileName)), ';', '"', countLine)) {
            String[] nextLine = readerAnswer.readNext();
            if (nextLine != null) {
                if (nextLine[0].equals(answer)) {
                    studentDao.addPointToTestResult(student);
                }
            }
        }
    }

}
