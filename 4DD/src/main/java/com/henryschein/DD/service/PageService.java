package com.henryschein.DD.service;

import com.henryschein.DD.dao.PageDAO;
import com.henryschein.DD.dto.PageDTO;
import com.henryschein.DD.entity.Page;
import org.springframework.stereotype.Service;

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
        Page newPage = new Page(pageDTO);
        pageDAO.saveAndFlush(newPage);
        return newPage;
    }
}
