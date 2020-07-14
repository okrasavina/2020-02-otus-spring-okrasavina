package ru.otus.spring.library.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@RequiredArgsConstructor
@Configuration
public class JobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MongoTemplate mongoTemplate;
    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Bean
    public TaskletStep dropMongoDBStep() {
        return stepBuilderFactory
                .get("dropMongoDBStep")
                .tasklet((stepContribution, chunkContext) -> {
                    mongoTemplate.getDb().drop();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job migrateJpaToMongoJob(Step authorMigrationStep,
                                    Step genreMigrationStep,
                                    Step bookMigrationStep,
                                    Step commentMigrationStep) {
        return jobBuilderFactory.get("migrateJpaToMongoJob")
                .incrementer(jobParameters -> jobParameters)
                .start(dropMongoDBStep())
                .next(authorMigrationStep)
                .next(genreMigrationStep)
                .next(bookMigrationStep)
                .next(commentMigrationStep)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        logger.info("Начало миграции с Jpa на Mongo.");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        logger.info("Конец миграции с Jpa на Mongo.");
                    }
                })
                .build();
    }
}
