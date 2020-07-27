package com.henryschein.DD.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class BookDTO {
    private Integer bookId;
    private String title;
    private List<PageDTO> pages;
}
