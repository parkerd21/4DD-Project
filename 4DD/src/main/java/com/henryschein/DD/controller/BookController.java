package com.henryschein.DD.controller;

import com.henryschein.DD.dto.BookDTO;
import com.henryschein.DD.entity.Book;
import com.henryschein.DD.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("book")
public class BookController {

    BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{bookId}")
    public Book getById(@PathVariable Long bookId) {
        return bookService.getById(bookId);
    }

    @GetMapping("/all")
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @PostMapping("/")
    public Book createNewBook() {
        return bookService.createNewBook();
    }

    // put

    @DeleteMapping("/{bookId}")
    public String deleteById(@PathVariable Long bookId) {
        return bookService.deleteById(bookId);
    }
}
