package com.henryschein.DD.entity;

import com.henryschein.DD.dto.DataElementDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "DATA_ELEMENT")
public class DataElement {
    @Column(name = "PAGE_ID")
    private Long pageId;

    private Integer xcoord;
    private Integer ycoord;
    private Integer zcoord;
    private String value;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DATA_ID")
    private Long dataId;

    public DataElement(String value, Integer xcoord, Integer ycoord) {
        this.value = value;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }

    public DataElement(DataElementDTO dataElementDTO) {
        this.dataId = dataElementDTO.getDataId();
        this.xcoord = dataElementDTO.getXcoord();
        this.ycoord = dataElementDTO.getYcoord();
        this.zcoord = dataElementDTO.getZcoord();
        this.value = dataElementDTO.getValue();
        this.pageId = dataElementDTO.getPageId();
    }

    @Override
    public String toString() {
        return "{" +
                "\n\tpageId: " + pageId +
                "\n\txcoord: " + xcoord +
                "\n\tycoord: " + ycoord +
                "\n\tzcoord: " + zcoord +
                "\n\tvalue: " + value +
                "\n\tdataId: " + dataId +
                "\n}\n";
    }
}
