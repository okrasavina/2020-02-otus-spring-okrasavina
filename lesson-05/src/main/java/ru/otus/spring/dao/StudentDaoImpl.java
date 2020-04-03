package ru.otus.spring.dao;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentDaoImpl implements StudentDao {

    private static final Integer DEFAULT_TEST_RESULT = 0;
    private final Map<Student, Integer> testResult = new HashMap<>();

    @Override
    public Student addNewStudent(String firstName, String lastName) {
        Student addStudent = new Student(firstName, lastName);
        testResult.put(addStudent, DEFAULT_TEST_RESULT);
        return addStudent;
    }

    @Override
    public void addPointToTestResult(Student student) throws StudentNotFoundException {
        if (!(testResult.containsKey(student))) {
            throw new StudentNotFoundException(student);
        } else {
            testResult.put(student, testResult.get(student) + 1);
        }
    }

    @Override
    public Integer getTestResultByStudent(Student student) throws StudentNotFoundException {
        if (!(testResult.containsKey(student))) {
            throw new StudentNotFoundException(student);
        } else {
            return testResult.get(student);
        }
    }

    @Override
    public String getFullName(Student student) throws StudentNotFoundException {
        if (!(testResult.containsKey(student))) {
            throw new StudentNotFoundException(student);
        } else {
            return student.getFirstName() + " " + student.getLastName();
        }
    }

}
