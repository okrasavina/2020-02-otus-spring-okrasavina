package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.service.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author")
    public String getListAuthor(Model model) {
        List<LibraryAuthor> authors = authorService.getListAuthor();
        model.addAttribute("authors", authors);
        return "author-list";
    }

    @GetMapping("/author/new")
    public String addAuthor(Model model) {
        model.addAttribute("author", new LibraryAuthor());
        model.addAttribute("notNew", false);
        return "author-edit";
    }

    @GetMapping("author/edit")
    public String showEditPage(@RequestParam("number") Long number, Model model) {
        LibraryAuthor author = authorService.getAuthorById(number);
        model.addAttribute("author", author);
        model.addAttribute("notNew", true);
        return "author-edit";
    }

    @PostMapping(value = "author/edit", params = "save")
    public String saveAuthor(LibraryAuthor author) {
        authorService.saveAuthor(author);
        return "redirect:/author";
    }

    @PostMapping(value = "author/edit", params = "cancel")
    public String cancelChanging() {
        return "redirect:/author";
    }

    @GetMapping("author/delete")
    public String showDeletePage(@RequestParam("number") Long number, Model model) {
        LibraryAuthor author = authorService.getAuthorById(number);
        model.addAttribute("author", author);
        return "author-delete";
    }

    @PostMapping(value = "author/delete", params = "delete")
    public String deleteAuthor(LibraryAuthor author) {
        authorService.deleteAuthor(author);
        return "redirect:/author";
    }

    @PostMapping(value = "author/delete", params = "cancel")
    public String cancelDeleting() {
        return "redirect:/author";
    }

}
