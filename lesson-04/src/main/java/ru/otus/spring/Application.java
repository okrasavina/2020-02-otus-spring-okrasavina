package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.spring.config.ApplicationSettings;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationSettings.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
