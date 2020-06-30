package ru.otus.spring.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.changelogs.InitMongoDBChangeLog;

@Configuration
public class ApplicationConfig {

    @Bean
    public Mongock mongock(MongoClient mongoClient, MongoProps mongoProps) {
        return new SpringMongockBuilder(mongoClient, mongoProps.getDatabase(),
                InitMongoDBChangeLog.class.getPackageName()).build();
    }
}
