package ru.otus.spring.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.library.domain.jpa.Author;
import ru.otus.spring.library.domain.mongo.AuthorMongo;
import ru.otus.spring.library.service.JpaToMongoTransformService;

import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Configuration
public class AuthorStepConfig {
    private final EntityManagerFactory managerFactory;
    private final MongoTemplate mongoTemplate;
    private final JpaToMongoTransformService service;
    private final StepBuilderFactory stepBuilderFactory;

    @StepScope
    @Bean
    public JpaPagingItemReader<Author> authorReader() {
        return new JpaPagingItemReaderBuilder<Author>()
                .name("authorReader")
                .entityManagerFactory(managerFactory)
                .queryString("select a from Author a")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Author, AuthorMongo> authorProcessor() {
        return service::transformAuthorToAuthorMongo;
    }

    @StepScope
    @Bean
    public MongoItemWriter<AuthorMongo> authorWriter() {
        return new MongoItemWriterBuilder<AuthorMongo>()
                .collection("author")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step authorMigrationStep(JpaPagingItemReader<Author> authorReader,
                                    ItemProcessor<Author, AuthorMongo> authorProcessor,
                                    MongoItemWriter<AuthorMongo> authorWriter) {
        return stepBuilderFactory.get("authorMigrationStep")
                .<Author, AuthorMongo>chunk(3)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .allowStartIfComplete(true)
                .build();
    }
}
