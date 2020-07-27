package com.henryschein.DD.service;

import com.henryschein.DD.dao.BookDAO;
import com.henryschein.DD.dto.BookDTO;
import com.henryschein.DD.entity.Book;
import com.henryschein.DD.service.cache.BookCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class BookService {

    private BookDAO bookDAO;
    private BookCacheService bookCacheService;

    public BookService(BookDAO bookDAO, BookCacheService bookCacheService) {
        this.bookDAO = bookDAO;
        this.bookCacheService = bookCacheService;
    }

    public Book getById(Integer bookId) {
        return bookCacheService.getById(bookId);
    }

    public Book createNewBook(String title) {
        return bookCacheService.createNewBook(title);
    }

    private boolean bookExists(Integer bookId) {
        Book book = bookCacheService.getById(bookId);
        return Objects.nonNull(book);
    }

    public List<Book> getAll() {
        return bookCacheService.getAll();
    }

    public void deleteById(Integer bookId) {
        if (bookExists(bookId))
            bookCacheService.deleteById(bookId);
        else
            log.error("Cannot delete book with id: " + bookId + ". The book was not found");
    }

    public Book updateBookTitle(BookDTO bookDTO) {
        if (bookExists(bookDTO.getBookId())) {
            Book initialBook = bookCacheService.getById(bookDTO.getBookId());
            initialBook.setTitle(bookDTO.getTitle());
            return bookCacheService.updateBookTitle(initialBook);
        }
        else {
            log.error("book " + bookDTO.getBookId() + " not found");
            return null;
        }
    }
}
