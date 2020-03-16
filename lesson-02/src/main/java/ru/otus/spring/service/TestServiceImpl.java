package ru.otus.spring.service;

import au.com.bytecode.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.StudentDao;
import ru.otus.spring.domain.Student;

import java.io.*;
import java.util.Locale;

@Service
public class TestServiceImpl implements TestService {

    private final static int COUNT_QUESTIONS = 5;
    private final static Locale CURRENT_LOCALE = Locale.getDefault();
    private final StudentDao studentDao;
    private final String questionResourceFile;
    private final String answerResourceFile;
    private final MessageSource messageSource;

    @Autowired
    public TestServiceImpl(StudentDao studentDao, @Value("${file.answer}") String answerResourceFile, MessageSource messageSource) {
        this.studentDao = studentDao;
        this.answerResourceFile = answerResourceFile;
        this.messageSource = messageSource;
        this.questionResourceFile = messageSource.getMessage("file.question", null, CURRENT_LOCALE);
    }

    @Override
    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(messageSource.getMessage("student.first.name", null, CURRENT_LOCALE));
        String firstName = reader.readLine();
        System.out.println(messageSource.getMessage("student.last.name", null, CURRENT_LOCALE));
        String lastName = reader.readLine();

        Student student = studentDao.addNewStudent(firstName, lastName);

        for (int i = 0; i < COUNT_QUESTIONS; i++) {
            getNextQuestion(i);
            String answer = reader.readLine();
            checkStudentAnswer(student, answer, i);
        }

        reader.close();
        System.out.println(messageSource.getMessage("student.result", new String[] {studentDao.getFullName(student), studentDao.getTestResultByStudent(student).toString()}, CURRENT_LOCALE));
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
