package com.henryschein.DD.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "PAGE")
public class Page {

    @Id
    @Column(name = "PAGE_ID")
    private Long pageId;

    @OneToMany(cascade= {CascadeType.ALL})
    @JoinColumn(name = "data_id")
    private List<DataElement> dataElements;
}
