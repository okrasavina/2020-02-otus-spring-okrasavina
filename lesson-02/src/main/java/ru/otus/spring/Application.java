package ru.otus.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.service.TestService;

import java.io.IOException;

@ComponentScan
@Configuration
@PropertySource("classpath:application.properties")
@Slf4j
public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

        TestService service = context.getBean(TestService.class);

        try {
            service.run();
        } catch (IOException e) {
            log.warn(e.getMessage());
        }

    }

}