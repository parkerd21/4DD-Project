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

public class PageDTO {
    private Long pageId;
    private Long bookId;
    private List<DataElementDTO> dataElements;
}
