package ru.otus.spring.danceclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@IntegrationComponentScan
@EnableIntegration
@SpringBootApplication
public class DanceClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanceClubApplication.class, args);
    }

}
