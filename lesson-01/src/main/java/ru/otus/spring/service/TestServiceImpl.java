package ru.otus.spring.service;

import au.com.bytecode.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.dao.StudentDao;
import ru.otus.spring.domain.Student;

import java.io.*;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final static int COUNT_QUESTIONS = 5;
    private final StudentDao studentDao;
    private final String questionResourceFile;
    private final String answerResourceFile;

    @Override
    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите, пожалуйста, свое имя:");
        String firstName = reader.readLine();
        System.out.println("Введите, пожалуйста, свою фамилию:");
        String lastName = reader.readLine();

        Student student = studentDao.addNewStudent(firstName, lastName);

        for (int i = 0; i < COUNT_QUESTIONS; i++) {
            getNextQuestion(i);
            String answer = reader.readLine();
            checkStudentAnswer(student, answer, i);
        }

        reader.close();
        System.out.printf("Student %s %s. Your result - %d", firstName, lastName, studentDao.getTestResultByStudent(student));
    }

    private void getNextQuestion(int countLine) throws IOException {

        CSVReader readerTest = null;
        try {
            readerTest = new CSVReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(questionResourceFile)), ';', '"', countLine);
            String[] nextLine = readerTest.readNext();
            if (nextLine != null) {
                for (String line : nextLine
                ) {
                    System.out.println(line);
                }
            }
        } finally {
            if (readerTest != null) {
                readerTest.close();
            }
        }
    }

    private void checkStudentAnswer(Student student, String answer, int countLine) throws IOException {

        CSVReader readerAnswer = null;
        try {
            readerAnswer = new CSVReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(answerResourceFile)), ';', '"', countLine);
            String[] nextLine = readerAnswer.readNext();
            if (nextLine != null) {
                if (nextLine[0].equals(answer)) {
                    studentDao.addPointToTestResult(student);
                }
            }
        } finally {
            if (readerAnswer != null) {
                readerAnswer.close();
            }
        }
    }

}
