package com.henryschein.DD.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "DATA_ELEMENT")
public class DataElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DATA_ID")
    private Long data_id;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "XCOORD")
    private Integer xcoord;

    @Column(name = "YCOORD")
    private Integer ycoord;

    @OneToMany(mappedBy = "dataId", cascade= {CascadeType.ALL})
    private List<History> listOfHistory;

    public DataElement(String value, int xCoord, int yCoord) {
        this.value = value;
        this.xcoord = xCoord;
        this.ycoord = yCoord;
    }

    public DataElement(String value, String coords) {
        this.value = value;
        setCoords(coords);
    }

    public void setCoords(int x, int y) {
        xcoord = x;
        ycoord = y;
    }

    public void setCoords(String coords) {
        xcoord = Integer.parseInt(coords.substring(1,2));
        ycoord = Integer.parseInt(coords.substring(3,4));
    }

    public String getCoords() {
        return "[" + xcoord + "," + ycoord + "]";
    }
}
