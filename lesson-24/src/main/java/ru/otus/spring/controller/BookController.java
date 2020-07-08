package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.dto.LibraryAuthor;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryGenre;
import ru.otus.spring.service.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/")
    public String showWelcomePage() {
        return "index";
    }

    @GetMapping("/search/genre")
    public String showFormForSearchingBooksByGenre(Model model) {
        model.addAttribute("genre", new LibraryGenre());
        return "search-by-genre";
    }

    @GetMapping("/search/genre/book")
    public String searchListBookByGenre(@RequestParam("name") String name, Model model) {
        List<LibraryBook> books = bookService.getListBooksByGenreName(name);
        setModelAttribute(model, books, String.format("Список книг по жанру '%s'", name), false);
        return "book-list";
    }

    @GetMapping("/search/author")
    public String showFormForSearchingBooksByAuthor(Model model) {
        model.addAttribute("author", new LibraryAuthor());
        return "search-by-author";
    }

    @GetMapping("/search/author/book")
    public String searchListBookByAuthor(@RequestParam("name") String name, Model model) {
        List<LibraryBook> books = bookService.getListBooksByAuthorName(name);
        setModelAttribute(model, books, String.format("Список книг по автору '%s'", name), false);
        return "book-list";
    }

    @GetMapping("/search/title")
    public String showFormForSearchingBooksByTitle(Model model) {
        model.addAttribute("book", new LibraryBook());
        return "search-by-title";
    }

    @GetMapping("/search/title/book")
    public String searchListBookByTitle(@RequestParam("title") String title, Model model) {
        List<LibraryBook> books = bookService.getListBooksByTitle(title);
        setModelAttribute(model, books, String.format("Список книг по названию '%s'", title), false);
        return "book-list";
    }

    @GetMapping("/book")
    public String getListBook(Model model) {
        List<LibraryBook> books = bookService.getListBook();
        setModelAttribute(model, books, "Список книг", true);
        return "book-list";
    }

    @GetMapping("book/new")
    public String addBook(Model model) {
        model.addAttribute("book", new LibraryBook());
        return "book-edit";
    }

    @GetMapping("/book/edit")
    public String showEditPage(@RequestParam("number") Long number, Model model) {
        LibraryBook book = bookService.getBookById(number);
        model.addAttribute("book", book);
        return "book-edit";
    }

    @PostMapping(value = "/book/edit", params = "save")
    public String saveBook(LibraryBook book) {
        bookService.saveBook(book);
        return "redirect:/book";
    }

    @PostMapping(value = "/book/edit", params = "cancel")
    public String cancelChanging() {
        return "redirect:/book";
    }

    @GetMapping("/book/delete")
    public String showDeletePage(@RequestParam("number") Long number, Model model) {
        LibraryBook book = bookService.getBookById(number);
        model.addAttribute("book", book);
        return "book-delete";
    }

    @PostMapping(value = "/book/delete", params = "delete")
    public String deleteBook(LibraryBook libraryBook) {
        bookService.deleteBook(libraryBook);
        return "redirect:/book";
    }

    @PostMapping(value = "/book/delete", params = "cancel")
    public String cancelDeleting() {
        return "redirect:/book";
    }

    private void setModelAttribute(Model model, List<LibraryBook> books, String headerMessage, boolean isNewObject) {
        model.addAttribute("books", books);
        model.addAttribute("header", headerMessage);
        model.addAttribute("isNew", isNewObject);
    }
}
