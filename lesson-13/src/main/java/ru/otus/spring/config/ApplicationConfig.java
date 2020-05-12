package ru.otus.spring.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    public static final String CHANGE_LOGS_SCAN_PACKAGE = "ru.otus.spring.changelogs";

    @Bean
    public Mongock mongock(MongoClient mongoClient, MongoProps mongoProps) {
        return new SpringMongockBuilder(mongoClient, mongoProps.getDatabase(), CHANGE_LOGS_SCAN_PACKAGE).build();
    }
}
