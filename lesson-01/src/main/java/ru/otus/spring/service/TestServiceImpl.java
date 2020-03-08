package ru.otus.spring.service;

import au.com.bytecode.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.dao.StudentDao;
import ru.otus.spring.domain.Student;

import java.io.*;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService{

    private final StudentDao studentDao;
    private final int COUNT_QUESTIONS = 5;

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

        CSVReader readerTest = new CSVReader(new FileReader("src/main/resources/Questions.csv"), ';', '"', countLine);
        String[] nextLine = readerTest.readNext();
        if (nextLine != null) {
            for (String line: nextLine
            ) {
                System.out.println(line);
            }
        }
        readerTest.close();
    }

    private void checkStudentAnswer(Student student, String answer, int countLine) throws IOException {

        CSVReader readerAnswer = new CSVReader(new FileReader("src/main/resources/Answer.csv"), ';', '"', countLine);
        String[] nextLine = readerAnswer.readNext();
        if (nextLine != null) {
            if (nextLine[0].equals(answer)) {
                studentDao.addPointToTestResult(student);
            }
        }
        readerAnswer.close();
    }

}
