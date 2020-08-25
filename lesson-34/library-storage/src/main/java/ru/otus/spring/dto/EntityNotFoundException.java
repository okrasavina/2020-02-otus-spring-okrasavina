package ru.otus.spring.dto;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String objName, String wordEnding) {
        super(String.format("%s не найден%s по переданным параметрам", objName, wordEnding));
    }
}
