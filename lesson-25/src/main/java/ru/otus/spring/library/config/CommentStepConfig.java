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
import ru.otus.spring.library.domain.jpa.Comment;
import ru.otus.spring.library.domain.mongo.CommentMongo;
import ru.otus.spring.library.service.JpaToMongoTransformService;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class CommentStepConfig {
    private final EntityManagerFactory managerFactory;
    private final MongoTemplate mongoTemplate;
    private final JpaToMongoTransformService service;
    private final StepBuilderFactory stepBuilderFactory;

    @StepScope
    @Bean
    public JpaPagingItemReader<Comment> commentReader() {
        return new JpaPagingItemReaderBuilder<Comment>()
                .name("commentReader")
                .entityManagerFactory(managerFactory)
                .queryString("select c from Comment c")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Comment, CommentMongo> commentProcessor() {
        return service::transformCommentToCommentMongo;
    }

    @StepScope
    @Bean
    public MongoItemWriter<CommentMongo> commentWriter() {
        return new MongoItemWriterBuilder<CommentMongo>()
                .collection("comment")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step commentMigrationStep(JpaPagingItemReader<Comment> commentReader,
                                     ItemProcessor<Comment, CommentMongo> commentProcessor,
                                     MongoItemWriter<CommentMongo> commentWriter) {
        return stepBuilderFactory.get("commentMigrationStep")
                .<Comment, CommentMongo>chunk(3)
                .reader(commentReader)
                .processor(commentProcessor)
                .writer(commentWriter)
                .allowStartIfComplete(true)
                .build();
    }
}
