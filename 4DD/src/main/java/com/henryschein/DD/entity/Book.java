package com.henryschein.DD.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private Long BOOK_ID;

    @OneToMany(cascade= {CascadeType.ALL})
    @JoinColumn(name = "pageId")
    private List<Page> pages;
}
