package com.henryschein.DD.dto;

import com.henryschein.DD.entity.DataValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataElementDTO {
    private Integer pageId;
    private Integer xcoord;
    private Integer ycoord;
    private Integer zcoord;
    private DataValue value;
    private Integer dataId;

    public DataElementDTO(Integer pageId, Integer x, Integer y) {
        this.pageId = pageId;
        this.xcoord = x;
        this.ycoord = y;
    }

    public DataElementDTO(Integer pageId, Integer x, Integer y, Integer z) {
        this.pageId = pageId;
        this.xcoord = x;
        this.ycoord = y;
        this.zcoord = z;
    }

    @Override
    public String toString() {
        return "{" +
                " pageId: " + pageId +
                ", xcoord: " + xcoord +
                ", ycoord: " + ycoord +
                ", zcoord: " + zcoord +
                ", value: " + value.getValue() +
                ", dataId: " + dataId +
                " }";
    }

    // used for unique key when we don't know the dataId
    public String getPageIdXY() {
        return pageId.toString() + xcoord + ycoord;
    }

    public  String getPageIdXYZ() {
        return  pageId.toString() + xcoord + ycoord + zcoord;
    }
}
