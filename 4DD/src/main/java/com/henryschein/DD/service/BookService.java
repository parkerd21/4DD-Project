package com.henryschein.DD.service;

import com.henryschein.DD.dao.BookDAO;
import com.henryschein.DD.dto.BookDTO;
import com.henryschein.DD.entity.Book;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private BookDAO bookDAO;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Cacheable(value = "books", key = "#bookId")
    @SneakyThrows
    public Book getById(Long bookId) {
        Thread.sleep(5000);
        return bookDAO.getById(bookId);
    }

    public Book createNewBook(String title) {
        return bookDAO.saveAndFlush(new Book(title));
    }

    private boolean bookExists(Long bookId) {
        Book book = getById(bookId);
        return Objects.nonNull(book);
    }

    public List<Book> getAll() {
        return bookDAO.findAll();
    }

    @CacheEvict(value = "books", key = "#bookId")
    public String deleteById(Long bookId) {
        if (bookExists(bookId)) {
            bookDAO.deleteById(bookId);
            return "Deleted book with id: " + bookId;
        }
        else return "Couldn't find book with id: " + bookId + " to delete";
    }

    @CachePut(value = "books", key = "#bookId")
    public Book updateBookTitle(BookDTO bookDTO) {
        if (bookExists(bookDTO.getBookId())) {
            Book initialBook = getById(bookDTO.getBookId());
            initialBook.setTitle(bookDTO.getTitle());
            return bookDAO.saveAndFlush(initialBook);
        }
        else
            return null;
    }
}
