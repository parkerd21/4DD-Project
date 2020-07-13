package com.henryschein.DD.entity;

import com.henryschein.DD.dto.BookDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    private Long bookId;

    @OneToMany(mappedBy = "bookId", cascade= {CascadeType.ALL})
    //@JoinColumn(name = "page_id")
    private List<Page> pages = new ArrayList<>();

    public Book(BookDTO bookDTO) {
        this.bookId = bookDTO.getBookId();
        this.pages = new ArrayList<>();
    }
}
