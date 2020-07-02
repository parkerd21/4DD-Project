package com.henryschein.DD.service;

import com.henryschein.DD.dao.PageDAO;
import com.henryschein.DD.dto.PageDTO;
import com.henryschein.DD.entity.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    PageDAO pageDAO;

    public PageService(PageDAO pageDAO) {
        this.pageDAO = pageDAO;
    }

    public Page getById(Long pageId) {
        return pageDAO.getById(pageId);
    }

    public Page add(PageDTO pageDTO) {
        Page initialPage = new Page();
        initialPage.setBookId(pageDTO.getBookId());
        Page newPage = pageDAO.saveAndFlush(initialPage);
        newPage.setDataElements(pageDTO.getDataElements());
        return pageDAO.saveAndFlush(newPage);
    }

    public List<Page> getAll() {
        return pageDAO.findAll();
    }
}
