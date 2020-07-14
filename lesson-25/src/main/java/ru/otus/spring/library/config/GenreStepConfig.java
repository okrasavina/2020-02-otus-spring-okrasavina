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
import ru.otus.spring.library.domain.jpa.Genre;
import ru.otus.spring.library.domain.mongo.GenreMongo;
import ru.otus.spring.library.service.JpaToMongoTransformService;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class GenreStepConfig {
    private final EntityManagerFactory managerFactory;
    private final MongoTemplate mongoTemplate;
    private final JpaToMongoTransformService service;
    private final StepBuilderFactory stepBuilderFactory;

    @StepScope
    @Bean
    public JpaPagingItemReader<Genre> genreReader() {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreReader")
                .entityManagerFactory(managerFactory)
                .queryString("select g from Genre g")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Genre, GenreMongo> genreProcessor() {
        return service::transformGenreToGenreMongo;
    }

    @StepScope
    @Bean
    public MongoItemWriter<GenreMongo> genreWriter() {
        return new MongoItemWriterBuilder<GenreMongo>()
                .collection("genre")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step genreMigrationStep(JpaPagingItemReader<Genre> genreReader,
                                   ItemProcessor<Genre, GenreMongo> genreProcessor,
                                   MongoItemWriter<GenreMongo> genreWriter) {
        return stepBuilderFactory.get("genreMigrationStep")
                .<Genre, GenreMongo>chunk(3)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .allowStartIfComplete(true)
                .build();
    }
}
