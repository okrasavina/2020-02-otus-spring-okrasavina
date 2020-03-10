package ru.otus.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.service.TestService;

import java.io.IOException;

@Slf4j
public class Application {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        TestService service = context.getBean(TestService.class);

        try {
            service.run();
        } catch (IOException e) {
            if (log.isWarnEnabled()) {
                log.warn(e.getMessage());
            }
        }

    }

}