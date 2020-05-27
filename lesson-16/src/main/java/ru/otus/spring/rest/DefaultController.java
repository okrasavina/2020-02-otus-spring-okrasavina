package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.spring.dto.EntityNotFoundException;

@ControllerAdvice
public class DefaultController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error-500");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}
