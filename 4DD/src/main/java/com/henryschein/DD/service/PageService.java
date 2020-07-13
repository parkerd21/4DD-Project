package com.henryschein.DD.service;

import com.henryschein.DD.dao.PageDAO;
import com.henryschein.DD.dto.PageDTO;
import com.henryschein.DD.entity.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class PageService {

    PageDAO pageDAO;

    public PageService(PageDAO pageDAO) {
        this.pageDAO = pageDAO;
    }

    public Page getById(Long pageId) {
        return pageDAO.getById(pageId);
    }

    public List<Page> getAll() {
        return pageDAO.findAll();
    }

    public Page createNewPage(Long bookId) {
        return pageDAO.saveAndFlush(new Page(bookId));
    }

    public Page updateBookId(PageDTO pageDTO) {
        if (pageExists(pageDTO.getPageId())) {
            Page initialPage = getById(pageDTO.getPageId());
            initialPage.setBookId(pageDTO.getBookId());
            return pageDAO.saveAndFlush(initialPage);
        }
        else
            return null;
    }

    private boolean pageExists(Long pageId) {
        Page page = pageDAO.getById(pageId);
        return Objects.nonNull(page);
    }

    @Transactional
    public String deleteById(Long pageId) {
        if (pageExists(pageId)) {
            pageDAO.deleteById(pageId);
            return "Deleted Page with id: " + pageId;
        }
        else
            return "Couldn't find page with id: " + pageId + " to delete";
    }
}
