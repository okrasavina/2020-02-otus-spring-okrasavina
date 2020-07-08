package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.spring.dto.LibraryUser;
import ru.otus.spring.service.UserService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new LibraryUser());
        return "registration";
    }

    @PostMapping("/registration")
    public String addNewUser(LibraryUser libraryUser) {
        userService.saveNewUser(libraryUser);
        return "redirect:/";
    }

    @GetMapping("/accessDenied")
    public String accessDeniedHandle() {
        return "accessDenied";
    }
}
