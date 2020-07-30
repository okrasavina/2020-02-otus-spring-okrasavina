package ru.otus.spring.danceclub.service;

import ru.otus.spring.danceclub.dto.DanceCouple;
import ru.otus.spring.danceclub.dto.Dancer;

import java.util.List;

public interface DanceManagerService {

    DanceCouple makeCouple(Dancer dancer);

}
