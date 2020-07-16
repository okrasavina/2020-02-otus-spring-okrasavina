package ru.otus.spring.library.service;

public interface JobService {
    void startJob();

    Long getJobExecutionId();

    void restartJob();

    String showJobInfo();
}
