package ru.otus.spring.readerservice.rest;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@ControllerAdvice
public class DefaultController {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseStatusException handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ResponseStatusException(HttpStatus.NON_AUTHORITATIVE_INFORMATION, e.getMessage(), e);
    }

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    public ResponseStatusException handleUserPrincipalNotFoundException(UserPrincipalNotFoundException e) {
        return new ResponseStatusException(HttpStatus.NON_AUTHORITATIVE_INFORMATION, e.getMessage(), e);
    }


}
