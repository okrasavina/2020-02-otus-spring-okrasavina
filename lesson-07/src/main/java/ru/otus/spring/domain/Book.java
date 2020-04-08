package ru.otus.spring.domain;

import lombok.*;

@RequiredArgsConstructor
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Book {
    @Setter
    private long id;
    private final String title;
}
