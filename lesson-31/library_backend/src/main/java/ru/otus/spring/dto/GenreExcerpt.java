package ru.otus.spring.dto;

import org.springframework.data.rest.core.config.Projection;
import ru.otus.spring.domain.Genre;

@Projection(name = "excerpt", types = Genre.class)
public interface GenreExcerpt {

    String getName();
}
