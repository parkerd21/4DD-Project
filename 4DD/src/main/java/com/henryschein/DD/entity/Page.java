package com.henryschein.DD.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PAGE")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAGE_ID")
    private Integer pageId;

    @Column(name = "BOOK_ID")
    private Integer bookId;

    @OneToMany(mappedBy = "pageId", cascade= {CascadeType.ALL})
    //@JoinColumn(name = "page_id")
    private List<DataElement> dataElements = new ArrayList<>();

    public Page (Integer bookId) {
        this.pageId = null;
        this.bookId = bookId;
        this.dataElements = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("{" + "\n\tpageId: ").append(pageId).append("\n\tbookId: ").append(bookId).append("\n\tdataElements: [");
        try {
            for (DataElement dataElement : dataElements) {
                msg.append("\n").append(dataElement.toString());
            }
            msg.append("\n\t]\n}\n");
        }
        catch (NullPointerException e) {
            msg.append("]\n}\n");
        }
        return msg.toString();
    }

    //    public void setDataElements(List<DataElement> dataElements) {
//        this.dataElements = dataElements;
//        if (this.dataElements != null) {
//            updateDataElementsPageIds();
//        }
//    }

//    private void updateDataElementsPageIds() {
//        for (DataElement dataElement: this.dataElements) {
//            dataElement.setPageId(this.pageId);
//        }
//    }
}
