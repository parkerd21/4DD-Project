package com.henryschein.DD.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "HISTORY")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Long history_id;


    @Column(name = "VALUE")
    private String value;

//    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REMOVE})
//    @JoinColumn(name = "dataId")
//    private DataElement dataElement;

    @JoinColumn(name = "dataId")
    private Long dataId;


}
