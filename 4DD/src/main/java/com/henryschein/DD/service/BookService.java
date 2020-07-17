package com.henryschein.DD.service;

import com.henryschein.DD.dao.BookDAO;
import com.henryschein.DD.dto.BookDTO;
import com.henryschein.DD.entity.Book;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@CacheConfig(cacheNames = {"books"})
public class BookService {

    private BookDAO bookDAO;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public Book getById(Long bookId) {
        return bookDAO.getById(bookId);
    }

    public Book createNewBook(String title) {
        return bookDAO.saveAndFlush(new Book(title));
    }

    private boolean bookExists(Long bookId) {
        Book book = getById(bookId);
        return Objects.nonNull(book);
    }
    @Cacheable(value = "allBooks")
    public List<Book> getAll() {
        return bookDAO.findAll();
    }

    @Caching(evict = {
            @CacheEvict(value = "allBooks", allEntries = true),
            @CacheEvict(value = "books", key = "#bookId")
    })
    public String deleteById(Long bookId) {
        if (bookExists(bookId)) {
            bookDAO.deleteById(bookId);
            return "Deleted book with id: " + bookId;
        }
        else return "Couldn't find book with id: " + bookId + " to delete";
    }

    @Caching(evict = {@CacheEvict(value = "allBooks", allEntries = true)},
            put = {@CachePut(value = "books", key = "#bookDTO.getBookId()")
    })
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
