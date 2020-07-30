package ru.otus.spring.danceclub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dancer {
    private String name;
    private Sex sex;
}
