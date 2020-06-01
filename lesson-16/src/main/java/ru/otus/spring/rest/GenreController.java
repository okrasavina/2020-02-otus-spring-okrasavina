package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.service.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genre")
    public String getListGenre(Model model) {
        List<LibraryGenre> genres = genreService.getListGenre();
        model.addAttribute("genres", genres);
        return "genre-list";
    }

    @GetMapping("/genre/new")
    public String addGenre(Model model) {
        model.addAttribute("genre", new LibraryGenre());
        model.addAttribute("notNew", false);
        return "genre-edit";
    }

    @GetMapping("/genre/edit")
    public String showEditPage(@RequestParam("number") Long number, Model model) {
        LibraryGenre genre = genreService.getGenreById(number);
        model.addAttribute("genre", genre);
        model.addAttribute("notNew", true);
        return "genre-edit";
    }

    @PostMapping(value = "/genre/edit", params = "save")
    public String saveGenre(LibraryGenre genre) {
        genreService.saveGenre(genre);
        return "redirect:/genre";
    }

    @PostMapping(value = "/genre/edit", params = "cancel")
    public String cancelChanging() {
        return "redirect:/genre";
    }

    @GetMapping("/genre/delete")
    public String showDeletePage(@RequestParam("number") Long number, Model model) {
        LibraryGenre genre = genreService.getGenreById(number);
        model.addAttribute("genre", genre);
        return "genre-delete";
    }

    @PostMapping(value = "/genre/delete", params = "delete")
    public String deleteGenre(LibraryGenre genre) {
        genreService.deleteGenre(genre);
        return "redirect:/genre";
    }

    @PostMapping(value = "/genre/delete", params = "cancel")
    public String cancelDeleting() {
        return "redirect:/genre";
    }

}
