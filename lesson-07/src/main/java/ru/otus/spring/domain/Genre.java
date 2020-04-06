package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Genre {
    private long id;
    private final String name;

    @Override
    public String toString() {
        return String.format("Genre {id = %d, name = '%s'}", id, name);
    }
}
