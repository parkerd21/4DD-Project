package com.henryschein.DD.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "VALUE")
public class DataValue implements Serializable {
    @Id
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "DATAELEMENT_ID")
    private DataElement dataElement;
    private String equation;
    private String value;
}
