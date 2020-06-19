package ru.otus.spring.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spring.dto.EntityNotAllowedDeleteException;
import ru.otus.spring.dto.EntityNotFoundException;

@ControllerAdvice
public class DefaultController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseStatusException handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
    }

    @ExceptionHandler(EntityNotAllowedDeleteException.class)
    public ResponseStatusException handleEntityNotAllowedDeleteException(EntityNotAllowedDeleteException e) {
        return new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e);
    }
}
