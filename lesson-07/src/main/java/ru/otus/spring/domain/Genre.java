package ru.otus.spring.domain;

import lombok.*;

@RequiredArgsConstructor
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Genre {
    @Setter
    private long id;
    private final String name;

    @Override
    public String toString() {
        return String.format("Genre {id = %d, name = '%s'}", id, name);
    }
}
