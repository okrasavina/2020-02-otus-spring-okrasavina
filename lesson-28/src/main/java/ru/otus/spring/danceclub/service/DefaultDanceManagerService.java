package ru.otus.spring.danceclub.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.danceclub.dto.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultDanceManagerService implements DanceManagerService {

    private static final String[] FEMALE_NAMES = {"Kate", "Olga", "Bella", "Viola", "Amelie", "Jane", "Judie"};

    @Override
    public DanceCouple makeCouple(Dancer dancer) {
        Style style = getRandomStyle();
        DanceCouple couple = DanceCouple.builder()
                .dancers(List.of(dancer, generateFemaleDancer()))
                .style(style)
                .movements(getDanceByStyle(style))
                .build();

        return couple;
    }

    private Dancer generateFemaleDancer() {
        return new Dancer(FEMALE_NAMES[RandomUtils.nextInt(0, FEMALE_NAMES.length)], Sex.FEMALE);
    }

    private List<Movement> getDanceByStyle(Style style) {
        int countMovements = RandomUtils.nextInt(4, 20);
        List<Movement> movementsForDance = new ArrayList<>(countMovements);
        Movement[] movements = Movement.values();
        List<Movement> movementList = Arrays.stream(movements)
                .filter(m -> m.getStyle().equals(style))
                .collect(Collectors.toList());

        for (int i = 0; i < countMovements; i++) {
            movementsForDance.add(movementList.get(RandomUtils.nextInt(0, movementList.size())));
        }

        return movementsForDance;

    }

    private Style getRandomStyle() {
        Style[] styles = Style.values();
        return styles[RandomUtils.nextInt(0, styles.length)];
    }
}
