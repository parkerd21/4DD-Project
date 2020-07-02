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
    private Long pageId;
    private Integer xcoord;
    private Integer ycoord;
    private Integer zcoord;
    private String value;
    private Long dataId;

    public DataElementDTO(Long pageId, Integer x, Integer y, String value) {
        this.pageId = pageId;
        this.xcoord = x;
        this.ycoord = y;
        this.value = value;
    }
}
