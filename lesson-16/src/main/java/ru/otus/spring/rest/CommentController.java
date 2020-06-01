package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.dto.LibraryBook;
import ru.otus.spring.dto.LibraryComment;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BookService bookService;

    @GetMapping("/comment")
    public String getListCommentsByBook(@RequestParam("number") Long bookNumber, Model model) {
        LibraryBook book = bookService.getBookById(bookNumber);
        List<LibraryComment> comments = commentService.getListCommentsByBook(book);
        model.addAttribute("comments", comments);
        model.addAttribute("libraryBook", book);
        return "comment-list";
    }

    @GetMapping("/comment/new")
    public String addComment(@RequestParam("number") Long bookNumber, Model model) {
        LibraryBook book = bookService.getBookById(bookNumber);
        model.addAttribute("comment", new LibraryComment(book));
        return "comment-edit";
    }

    @GetMapping("/comment/edit")
    public String showEditPage(@RequestParam("number") Long commentNumber, Model model) {
        LibraryComment comment = commentService.getCommentById(commentNumber);
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @PostMapping(value = "/comment/edit", params = "save")
    public String saveComment(@RequestParam("bookNumber") Long bookNumber, LibraryComment comment) {
        comment.setLibraryBook(bookService.getBookById(bookNumber));
        commentService.saveComment(comment);
        return String.format("redirect:/comment?number=%d", bookNumber);
    }

    @PostMapping(value = "/comment/edit", params = "cancel")
    public String cancelChanging(@RequestParam("bookNumber") Long bookNumber) {
        return String.format("redirect:/comment?number=%d", bookNumber);
    }

    @GetMapping("/comment/delete")
    public String showDeletePage(@RequestParam("number") Long commentNumber, Model model) {
        LibraryComment comment = commentService.getCommentById(commentNumber);
        model.addAttribute("comment", comment);
        return "comment-delete";
    }

    @PostMapping(value = "/comment/delete", params = "delete")
    public String deleteComment(@RequestParam("bookNumber") Long bookNumber, LibraryComment comment) {
        comment.setLibraryBook(bookService.getBookById(bookNumber));
        commentService.deleteComment(comment);
        return String.format("redirect:/comment?number=%d", bookNumber);
    }

    @PostMapping(value = "/comment/delete", params = "cancel")
    public String cancelDeleting(@RequestParam("bookNumber") Long bookNumber) {
        return String.format("redirect:/comment?number=%d", bookNumber);
    }
}
