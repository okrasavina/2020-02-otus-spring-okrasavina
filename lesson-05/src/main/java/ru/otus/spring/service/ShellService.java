package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.config.ApplicationSettings;

@ShellComponent
@RequiredArgsConstructor
public class ShellService {

    private final TestService testService;
    private final MessageSource messageSource;
    private final ApplicationSettings applicationSettings;

    @ShellMethod(value = "Introduce yourself", key = {"i", "intro"})
    public String introduce(String firstName, String lastName) {
        return testService.welcome(firstName, lastName);
    }

    @ShellMethod(value = "start", key = "start")
    @ShellMethodAvailability(value = "isStartTestingAvailable")
    public String startTesting() throws Exception {
        testService.run();
        return testService.getResult();
    }

    private Availability isStartTestingAvailable() {
        return testService.getStudent() == null ?
                Availability.unavailable(messageSource.getMessage("test.available", null, applicationSettings.getLocale())) :
                Availability.available();
    }

}
