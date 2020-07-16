package ru.otus.spring.library.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultJobService implements JobService {
    private final JobOperator jobOperator;
    @Getter
    private Long jobExecutionId;

    @SneakyThrows
    @Override
    public void startJob() {
        jobExecutionId = jobOperator.start("migrateJpaToMongoJob", "");
        System.out.println(jobOperator.getSummary(jobExecutionId));
    }

    @SneakyThrows
    @Override
    public void restartJob() {
        jobOperator.restart(jobExecutionId);
    }

    @SneakyThrows
    @Override
    public String showJobInfo() {
        return jobOperator.getSummary(jobExecutionId);
    }
}
