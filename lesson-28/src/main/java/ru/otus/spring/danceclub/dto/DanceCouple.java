package ru.otus.spring.danceclub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DanceCouple {
    private List<Dancer> dancers;
    private Style style;
    private List<Movement> movements;

    @Override
    public String toString() {
        return String.format("New dance couple: %s. Dance: %s",
                dancers.stream().map(Dancer::getName).collect(Collectors.joining(" and ")),
                movements.stream().map(Movement::toString).collect(Collectors.joining(", ")));
    }
}
