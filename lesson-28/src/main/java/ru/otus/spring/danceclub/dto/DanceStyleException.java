package ru.otus.spring.danceclub.dto;

public class DanceStyleException extends RuntimeException{
    public DanceStyleException(String styleName) {
        super(String.format("This party doesn't dance %s", styleName));
    }
}
