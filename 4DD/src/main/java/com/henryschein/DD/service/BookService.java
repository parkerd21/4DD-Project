package com.henryschein.DD.service;

import com.henryschein.DD.TheCacheManager;
import com.henryschein.DD.dao.BookDAO;
import com.henryschein.DD.dto.BookDTO;
import com.henryschein.DD.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class BookService {

    private final BookDAO bookDAO;
    private final TheCacheManager cacheManager;

    public BookService(BookDAO bookDAO, TheCacheManager cacheManager) {
        this.bookDAO = bookDAO;
        this.cacheManager = cacheManager;
    }

    public Book getById(Integer bookId) {
        Book book = bookDAO.getById(bookId);
        if (Objects.nonNull(book)) {
            log.info("database: retrieved book " + bookId);
        } else
            log.info("database: book " + bookId + " not found");
        return book;
    }

    public Book createNewBook(String title) {
        Book book = bookDAO.saveAndFlush(new Book(title));
        log.info("database: added new book " + book.getBookId());
        return book;
    }

    public List<Book> getAll() {
        List<Book> bookList = bookDAO.findAll();
        log.info("database: retrieved all books");
        return bookList;
    }

    public void deleteById(Integer bookId) {
        Book book = getById(bookId);
        if (Objects.nonNull(book)) {
            cacheManager.invalidateDataElementListCache();
            cacheManager.invalidateDataElementCacheByBook(book);
            bookDAO.deleteById(bookId);
            log.info("database: deleted book " + bookId);
        } else
            log.error("database: failed to delete book " + bookId);
    }

    public Book updateBookTitle(BookDTO bookDTO) {
        Book book = getById(bookDTO.getBookId());
        if (Objects.nonNull(book)) {
            book.setTitle(bookDTO.getTitle());
            log.info("database: updated book " + book.getBookId());
            return bookDAO.saveAndFlush(book);
        } else {
            log.error("database: failed to update book " + bookDTO.getBookId());
            return null;
        }
    }
}
