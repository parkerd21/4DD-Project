package com.henryschein.DD.entity;

import com.henryschein.DD.dto.DataElementDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "DATA_ELEMENT")
public class DataElement {
    @Column(name = "PAGE_ID")
    private Integer pageId;
    private Integer xcoord;
    private Integer ycoord;
    private Integer zcoord;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DATA_ID", referencedColumnName = "id")
    private DataValue dataValue;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DATA_ID")
    private Integer dataId;


    public DataElement(DataValue value, Integer xcoord, Integer ycoord) {
        this.dataValue = value;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }

    public DataElement(DataElementDTO dataElementDTO) {
        this.dataId = dataElementDTO.getDataId();
        this.xcoord = dataElementDTO.getXcoord();
        this.ycoord = dataElementDTO.getYcoord();
        this.zcoord = dataElementDTO.getZcoord();
        this.dataValue = Objects.isNull(dataElementDTO.getDataValue().getId()) ? null : dataElementDTO.getDataValue();
        this.pageId = dataElementDTO.getPageId();
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

    @Transient
    public String getPageIdXY() {
        return pageId.toString() + xcoord + ycoord;
    }
}
