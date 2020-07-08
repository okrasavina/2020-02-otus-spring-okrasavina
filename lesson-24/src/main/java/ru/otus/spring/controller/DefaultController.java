package ru.otus.spring.controller;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.spring.dto.EntityNotFoundException;
import ru.otus.spring.dto.UserAlreadyExistsException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class DefaultController {

    @ExceptionHandler({EntityNotFoundException.class,
            UserAlreadyExistsException.class,
            UsernameNotFoundException.class})
    public ModelAndView handleEntityNotFoundException(RuntimeException e) {
        ModelAndView modelAndView = new ModelAndView("error-500");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}
