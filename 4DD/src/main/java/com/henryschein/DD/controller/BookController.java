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

    @PostMapping("/{title}")
    public Book createNewBook(@PathVariable String title) {
        return bookService.createNewBook(title);
    }

    @PutMapping("/")
    public Book updateBookTitle(@RequestBody BookDTO bookDTO) {
        return bookService.updateBookTitle(bookDTO);
    }

    @DeleteMapping("/{bookId}")
    public void deleteById(@PathVariable Long bookId) {
        bookService.deleteById(bookId);
    }
}
