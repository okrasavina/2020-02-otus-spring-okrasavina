package ru.otus.spring.service;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import ru.otus.spring.config.ApplicationSettings;
import ru.otus.spring.dao.StudentDao;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("тестируем TestService")
@SpringBootTest(classes = TestServiceImpl.class)
class TestServiceImplTest {

    private TestService testService;

    @MockBean
    private StudentDao studentDao;
    @MockBean
    private MessageSource messageSource;
    @MockBean
    private ConsoleReader consoleReader;
    @MockBean
    private ApplicationSettings applicationSettings;

    private static final String MESSAGE_PROPERTY_WELCOME = "test.welcome";
    private static final String MESSAGE_PROPERTY_TEST_RESULT = "student.result";
    private static final String MESSAGE_WELCOME = "Olga Krasavina, welcome to student's test service.";
    private static final String STUDENT_FIRST_NAME = "Olga";
    private static final String STUDENT_LAST_NAME = "Krasavina";
    private static final String MESSAGE_TEST_RESULT = "Student Olga Krasavina. Your result - 4.";

    @BeforeEach
    void setUp(){
        testService = new TestServiceImpl(studentDao, messageSource, consoleReader, applicationSettings);
        given(applicationSettings.getLocale()).willReturn(Locale.ENGLISH);
    }

    @DisplayName("должен корректно здороваться с новым студентом")
    @Test
    void shouldCorrectWelcomeNewStudent(){
        given(messageSource.getMessage(MESSAGE_PROPERTY_WELCOME, new String[] {STUDENT_FIRST_NAME, STUDENT_LAST_NAME}, applicationSettings.getLocale())).
                willReturn(MESSAGE_WELCOME);
        assertThat(testService.welcome(STUDENT_FIRST_NAME, STUDENT_LAST_NAME)).isEqualTo(MESSAGE_WELCOME);
        verify(messageSource, times(1)).
                getMessage(MESSAGE_PROPERTY_WELCOME, new String[] {STUDENT_FIRST_NAME, STUDENT_LAST_NAME}, applicationSettings.getLocale());
    }

    @DisplayName("должен выводить результат тестирования")
    @Test
    void shouldCorrectGetTestResult() {
        given(studentDao.getFullName(any())).willReturn(STUDENT_FIRST_NAME + " " + STUDENT_LAST_NAME);
        given(studentDao.getTestResultByStudent(any())).willReturn(4);
        given(messageSource.getMessage(MESSAGE_PROPERTY_TEST_RESULT, new String[] {STUDENT_FIRST_NAME + " " + STUDENT_LAST_NAME, "4"}, applicationSettings.getLocale())).
                willReturn(MESSAGE_TEST_RESULT);
        assertThat(testService.getResult()).isEqualTo(MESSAGE_TEST_RESULT);
        verify(messageSource, times(1)).getMessage(MESSAGE_PROPERTY_TEST_RESULT, new String[] {STUDENT_FIRST_NAME + " " + STUDENT_LAST_NAME, "4"}, applicationSettings.getLocale());
    }

}