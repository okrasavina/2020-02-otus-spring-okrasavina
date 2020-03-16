package ru.otus.spring.dao;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;

import java.util.HashMap;
import java.util.Map;

@Getter
@Service
public class StudentDaoImpl implements StudentDao {

    private final static Integer DEFAULT_TEST_RESULT = 0;
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
            throw new StudentNotFoundException("Student " + student.getFirstName() + " " + student.getLastName() + " not found.");
        } else {
            testResult.put(student, testResult.get(student) + 1);
        }
    }

    @Override
    public Integer getTestResultByStudent(Student student) throws StudentNotFoundException {
        if (!(testResult.containsKey(student))) {
            throw new StudentNotFoundException("Student " + student.getFirstName() + " " + student.getLastName() + " not found.");
        } else {
            return testResult.get(student);
        }
    }

    @Override
    public String getFullName(Student student) throws StudentNotFoundException {
        if (!(testResult.containsKey(student))) {
            throw new StudentNotFoundException("Student " + student.getFirstName() + " " + student.getLastName() + " not found.");
        } else {
            return student.getFirstName() + " " + student.getLastName();
        }
    }

}
