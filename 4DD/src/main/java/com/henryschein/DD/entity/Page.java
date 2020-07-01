package com.henryschein.DD.entity;

import com.henryschein.DD.dto.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PAGE")
public class Page {

    @Id
    @Column(name = "PAGE_ID")
    private Long pageId;

    @Getter
    @Setter
    @Column(name = "BOOK_ID")
    private Long bookId;

    @Getter
    @Setter
    @OneToMany(cascade= {CascadeType.ALL})
    @JoinColumn(name = "page_id")
    private List<DataElement> dataElements;

    public Page(PageDTO pageDTO) {
        // TODO: this isn't going to work with a persistant dataBase unless we save the pageCounter to the dataBase
        this.pageId = getPageIdCounter();
        incrementPageIdCounter();

        this.bookId = pageDTO.getBookId();

        if (pageDTO.getDataElements() == null) {
            this.dataElements = new ArrayList<>();
        }
        else {
            this.dataElements = pageDTO.getDataElements();
            updateDataElementsPageIds();
        }
    }

    public Page() {

    }

    private static Long pageIdCounter = 1L;
    public static synchronized Long getPageIdCounter() {
        return pageIdCounter;
    }
    public static synchronized void incrementPageIdCounter() {
        pageIdCounter++;
    }

    public Long getPageId() {
        return this.pageId;
    }
    public void setPageId() {
        this.pageId = getPageIdCounter();
        incrementPageIdCounter();
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
