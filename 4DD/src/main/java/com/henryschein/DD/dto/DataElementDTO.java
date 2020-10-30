package com.henryschein.DD.dto;

import com.henryschein.DD.entity.DataValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DataElementDTO {
    private Integer pageId;
    private Integer xcoord;
    private Integer ycoord;
    private Integer zcoord;
    private DataValue dataValue;
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

    public DataElementDTO(Integer pageId, String coord) {
        if (coord.contains("[") && coord.contains("]")) {
            try {
                coord = StringUtils.trimLeadingCharacter(coord, '[');
                coord = StringUtils.trimTrailingCharacter(coord, ']');
                String[] coordArray = coord.split(",");
                this.xcoord = Integer.parseInt(StringUtils.trimAllWhitespace(coordArray[0]));
                this.ycoord = Integer.parseInt(StringUtils.trimAllWhitespace(coordArray[1]));
                this.pageId = pageId;
            } catch (Exception e) {
                log.error("Error creating DataElementDTO with these values as its coords: " + coord);
            }
        }

    }

    @Override
    public String toString() {
        return "{" +
                " pageId: " + pageId +
                ", xcoord: " + xcoord +
                ", ycoord: " + ycoord +
                ", zcoord: " + zcoord +
                ", value: " + dataValue.getValue() +
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
