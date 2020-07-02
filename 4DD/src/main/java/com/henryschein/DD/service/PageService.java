package com.henryschein.DD.service;

import com.henryschein.DD.dao.PageDAO;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.dto.PageDTO;
import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.entity.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    PageDAO pageDAO;
    DataElementService dataElementService;

    public PageService(PageDAO pageDAO, DataElementService dataElementService) {
        this.pageDAO = pageDAO;
        this.dataElementService = dataElementService;
    }

    public Page getById(Long pageId) {
        return pageDAO.getById(pageId);
    }

    public List<Page> getAll() {
        return pageDAO.findAll();
    }


    public Page add(PageDTO pageDTO) {
        if (!pageExists(pageDTO.getPageId())) {
            Page initialPage = new Page();
            initialPage.setBookId(pageDTO.getBookId());
            Page newPage = pageDAO.saveAndFlush(initialPage);
            // add dataElements one at a time
            // TODO: check that all of the dataElements are unique coordinates (X,Y)
            // TODO: set all z values to 1 in dataELements
            newPage.setDataElements(pageDTO.getDataElements());
            return pageDAO.saveAndFlush(newPage);
        }
        else
            return null;
    }

    private void addDataElements(PageDTO pageDTO) {

        for (DataElement dataElement : pageDTO.getDataElements()) {

        }

    }



    public Page update(PageDTO pageDTO) {
        if (pageExists(pageDTO.getPageId())) {
            Page initialPage = new Page(pageDTO);
            return pageDAO.saveAndFlush(initialPage);
        }
        else
            return null;

    }

    private boolean pageExists(Long pageId) {
        Page page = pageDAO.getById(pageId);
        return page != null;
    }
}
