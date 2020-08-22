package ru.otus.spring.dto;

import org.springframework.data.rest.core.config.Projection;
import ru.otus.spring.domain.Author;

import java.time.LocalDate;

@Projection(name = "excerpt", types = Author.class)
public interface AuthorExcerpt {

    String getName();

}
