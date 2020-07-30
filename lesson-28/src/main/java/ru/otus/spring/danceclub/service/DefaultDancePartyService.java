package ru.otus.spring.danceclub.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.danceclub.config.DanceMakerGateway;
import ru.otus.spring.danceclub.dto.DanceCouple;
import ru.otus.spring.danceclub.dto.Dancer;
import ru.otus.spring.danceclub.dto.Sex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultDancePartyService implements DancePartyService {
    private final DanceMakerGateway gateway;

    private static final String[] MALE_NAMES = {"Dick", "Dan", "Tom", "Kith", "Fred", "Peter", "John"};

    @Override
    public void startDanceParty(int countCouple) {
        List<Dancer> dancers = generateDancers(countCouple);
        System.out.println("New dancers: " + dancers.stream()
                .map(Dancer::getName)
                .collect(Collectors.joining(", ")));
        System.out.println("Welcome to our dance party!");
        List<DanceCouple> couples = gateway.createDance(dancers);
        couples.forEach(System.out::println);
    }

    private List<Dancer> generateDancers(int count) {
        ArrayList<Dancer> dancers = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            dancers.add(generateNewDancer());
        }

        return dancers;
    }

    private Dancer generateNewDancer() {
        return new Dancer(MALE_NAMES[RandomUtils.nextInt(0, MALE_NAMES.length)], Sex.MALE);
    }
}
