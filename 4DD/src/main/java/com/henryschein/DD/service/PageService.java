package com.henryschein.DD.service;

import com.henryschein.DD.TheCacheManager;
import com.henryschein.DD.dao.PageDAO;
import com.henryschein.DD.dto.PageDTO;
import com.henryschein.DD.entity.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PageService {
    private final TheCacheManager cacheManager;
    private final PageDAO pageDAO;

    public PageService(TheCacheManager cacheManager, PageDAO pageDAO) {
        this.cacheManager = cacheManager;
        this.pageDAO = pageDAO;
    }

    public Page getById(Integer pageId) {
        Page page = pageDAO.getById(pageId);
        if (Objects.nonNull(page)) {
            log.info("database: retrieved page " + pageId);
        } else {
            log.info("database: page " + pageId + " not found");
        }
        return page;
    }

    public List<Page> getAll() {
        List<Page> pageList = pageDAO.findAll();
        log.info("database: retrieved all pages");
        return pageList;
    }

    public Page createNewPage(Integer bookId) {
        try {
            Page page = pageDAO.saveAndFlush(new Page(bookId));
            log.info("database: added new page " + page.getPageId() + " in book " + bookId);
            return page;
        } catch (Exception e) {
            log.error("database: failed to create page in book " + bookId);
            return null;
        }
    }

    public Page updatePageWithBookId(PageDTO pageDTO) {
        Page page = getById(pageDTO.getPageId());
        if (Objects.nonNull(page)) {
            page.setBookId(pageDTO.getBookId());
            log.info("database: updated page " + page.getPageId());
            return pageDAO.saveAndFlush(page);
        } else {
            log.error("database: failed to update page " + pageDTO.getPageId());
            return null;
        }
    }

    public void deletePageById(Integer pageId) {
        Page page = getById(pageId);
        if (Objects.nonNull(page)) {
            pageDAO.deleteById(pageId);
            log.info("database: deleted page " + pageId);
            cacheManager.invalidateDataElementListCache();
            cacheManager.invalidateDataElementCacheByPage(page.getPageId());
        } else
            log.error("database: failed to delete page " + pageId);
    }
}
