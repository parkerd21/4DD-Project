package com.henryschein.DD.service.cache;

import com.henryschein.DD.TheCacheManager;
import com.henryschein.DD.dao.BookDAO;
import com.henryschein.DD.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookCacheService {
    private BookDAO bookDAO;
    private final TheCacheManager cacheManager;
    private static final String DELETE_CACHE_ALL_BOOKS_MESSAGE = "cache allBooks: deleted all books";

    public BookCacheService(BookDAO bookDAO, TheCacheManager cacheManager) {
        this.bookDAO = bookDAO;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "books", key = "#bookId")
    public Book getById(Integer bookId) {
        Book book = bookDAO.getById(bookId);
        if (Objects.nonNull(book)) {
            log.info("database: retrieving book " + bookId);
            log.info("cache books: added book " + bookId);
        }
        else
            log.error("book " + bookId + " not found");
        return book;
    }

    @Cacheable(value = "allBooks")
    public List<Book> getAll() {
        log.info("database: retrieving all books");
        log.info("cache books: added all books");
        return bookDAO.findAll();
    }


    @CacheEvict(value = "allBooks", allEntries = true)
    public Book createNewBook(String title) {
        Book book = bookDAO.saveAndFlush(new Book(title));
        log.info(DELETE_CACHE_ALL_BOOKS_MESSAGE);
        log.info("database: added new book " + book.getBookId());
        return book;
    }

    @Caching(evict = {
            @CacheEvict(value = "allBooks", allEntries = true),
            @CacheEvict(value = "books", key = "#bookId")
    })
    public void deleteById(Integer bookId) {
            log.info(DELETE_CACHE_ALL_BOOKS_MESSAGE);
            log.info("database: deleted book " + bookId);
            log.info("cache books: deleted book " + bookId);
            bookDAO.deleteById(bookId);
    }

    @Caching(evict = {@CacheEvict(value = "allBooks", allEntries = true)},
            put = {@CachePut(value = "books", key = "#book.getBookId()")
            })
    public Book updateBookTitle(Book book) {
        log.info(DELETE_CACHE_ALL_BOOKS_MESSAGE);
        log.info("database: updated book " + book.getBookId());
        log.info("cache books: updated book " + book.getBookId());
        return bookDAO.saveAndFlush(book);
    }
}
