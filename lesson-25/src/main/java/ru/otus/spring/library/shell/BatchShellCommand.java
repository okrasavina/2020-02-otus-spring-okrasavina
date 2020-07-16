package ru.otus.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.library.service.JobService;

@RequiredArgsConstructor
@ShellComponent
public class BatchShellCommand {
    private final JobService jobService;

    @ShellMethod(value = "Start migration job", key = "start")
    @ShellMethodAvailability(value = "isAvailableToStart")
    public void startMigrationJob() {
        jobService.startJob();
    }

    @ShellMethod(value = "Restart migration job", key = "restart")
    @ShellMethodAvailability(value = "isAvailableToRestart")
    public void restartMigrationJob() {
        jobService.restartJob();
    }

    @ShellMethod(value = "Show info about job execution", key = "info")
    @ShellMethodAvailability(value = "isAvailableToRestart")
    public String showJobInfo() {
        return jobService.showJobInfo();
    }

    private Availability isAvailableToStart() {
        return jobService.getJobExecutionId() == null ? Availability.available() :
                Availability.unavailable("Job is completed. Call command \"restart\" to restart job.");
    }

    private Availability isAvailableToRestart() {
        return jobService.getJobExecutionId() != null ? Availability.available() :
                Availability.unavailable("Job was not started. Call command \"start\" to start job first time.");
    }
}
