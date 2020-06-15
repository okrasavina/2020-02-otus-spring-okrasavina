package ru.otus.spring.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.EntityNotFoundException;

@ControllerAdvice
public class DefaultController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(EntityNotAllowedDeleteException.class)
    public ResponseEntity<String> handleEntityNotAllowedDeleteException(EntityNotAllowedDeleteException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
}
