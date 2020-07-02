package com.henryschein.DD.entity;

import com.henryschein.DD.dto.PageDTO;
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
    private Long pageId;

    @Column(name = "BOOK_ID")
    private Long bookId;

    @OneToMany(cascade= {CascadeType.ALL})
    @JoinColumn(name = "page_id")
    private List<DataElement> dataElements;

    public Page(PageDTO pageDTO) {
        this.pageId = pageDTO.getPageId();
        this.bookId = pageDTO.getBookId();
        if (pageDTO.getDataElements() == null) {
            this.dataElements = new ArrayList<>();
        }
        else {
            this.dataElements = pageDTO.getDataElements();
            updateDataElementsPageIds();
        }
    }

    public void setDataElements(List<DataElement> dataElements) {
        this.dataElements = dataElements;
        if (this.dataElements != null) {
            updateDataElementsPageIds();
        }
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("{" + "\n\tpageId: ").append(pageId).append("\n\tbookId: ").append(bookId).append("\n\tdataElements: [\n");
        for (DataElement dataElement : dataElements) {
            msg.append(dataElement.toString());
        }
        msg.append("\n\t]\n}\n");
        return msg.toString();
    }

    private void updateDataElementsPageIds() {
        for (DataElement dataElement: this.dataElements) {
            dataElement.setPageId(this.pageId);
        }
    }
}
