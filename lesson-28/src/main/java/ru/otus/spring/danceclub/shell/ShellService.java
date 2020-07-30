package ru.otus.spring.danceclub.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.danceclub.service.DancePartyService;

@ShellComponent
@RequiredArgsConstructor
public class ShellService {

    private final DancePartyService danceService;

    @ShellMethod(value = "Start dance party", key = "party")
    public void startParty(String countDancer) {
        int countCouple = Integer.parseInt(countDancer);
        danceService.startDanceParty(countCouple);
    }
}
