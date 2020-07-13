package com.henryschein.DD.service;

import com.henryschein.DD.dao.BookDAO;
import com.henryschein.DD.dto.BookDTO;
import com.henryschein.DD.entity.Book;
import com.henryschein.DD.entity.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BookService {

    private BookDAO bookDAO;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public Book getById(Long bookId) {
        return bookDAO.getById(bookId);
    }

    public Book createNewBook() {
        return bookDAO.saveAndFlush(new Book());
    }

    private boolean bookExists(Long bookId) {
        Book book = getById(bookId);
        return Objects.nonNull(book);
    }

    public List<Book> getAll() {
        return bookDAO.findAll();
    }

    public String deleteById(Long bookId) {
        if (bookExists(bookId)) {
            bookDAO.deleteById(bookId);
            return "Deleted book with id: " + bookId;
        }
        else return "Couldn't find book with id: " + bookId + " to delete";
    }
}
