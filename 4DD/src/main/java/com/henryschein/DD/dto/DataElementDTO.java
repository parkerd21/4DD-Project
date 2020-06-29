package com.henryschein.DD.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataElementDTO {
    private Long dataId;
    private Integer xcoord;
    private Integer ycoord;
    private Integer zcoord;
    private String value;
    private Long pageId;
}
