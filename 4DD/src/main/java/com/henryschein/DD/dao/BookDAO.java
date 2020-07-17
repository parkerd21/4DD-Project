package com.henryschein.DD.dao;

import com.henryschein.DD.entity.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDAO extends JpaRepository<Book, Long> {

    @Cacheable(value = "books", key = "#bookId")
    @Query("select b from Book b where b.bookId = :id")
    Book getById(@Param("id") Long bookId);

}
