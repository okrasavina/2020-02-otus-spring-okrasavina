package ru.otus.spring.dto;

import java.sql.SQLException;

public class EntityNotAllowedDeleteException extends SQLException {
    public EntityNotAllowedDeleteException(String objectName) {
        super(String.format("Данный %s не может быть удален. По нему есть книги.", objectName));
    }
}
