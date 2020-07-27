package com.henryschein.DD.service.cache;

import com.henryschein.DD.dao.PageDAO;
import com.henryschein.DD.entity.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PageCacheService {

    private PageDAO pageDAO;
    private static final String DELETE_CACHE_ALL_PAGES_MESSAGE = "cache allPages: deleted all pages";

    public PageCacheService (PageDAO pageDAO) {
        this.pageDAO = pageDAO;
    }

    @Cacheable(value = "pages", key = "#pageId")
    public Page getById(Integer pageId) {
        Page page = pageDAO.getById(pageId);
        if (Objects.nonNull(page)) {
            log.info("database: retrieving page " + pageId);
            log.info("cache pages: added page " +pageId);
        }
        else
            log.error("page " + pageId + " not found");

        return page;
    }

    @Cacheable(value = "allPages")
    public List<Page> getAll() {
        log.info("database: retrieving all pages");
        log.info("cache pages: added all pages");
        return pageDAO.findAll();
    }

    @CacheEvict(value = "allPages", allEntries = true)
    public Page createNewPage(Integer bookId) {
        Page page = pageDAO.saveAndFlush(new Page(bookId));
        log.info(DELETE_CACHE_ALL_PAGES_MESSAGE);
        log.info("database: added new page " + page.getPageId());
        return page;
    }

    @Caching(evict = {@CacheEvict(value = "allPages", allEntries = true)},
            put = {@CachePut(value = "pages", key = "#page.getPageId()")
            })
    public Page updateBookId(Page page) {
        log.info(DELETE_CACHE_ALL_PAGES_MESSAGE);
        log.info("database: updated page " + page.getPageId());
        log.info("cache pages: updated page " + page.getPageId());
        return pageDAO.saveAndFlush(page);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allPages", allEntries = true),
            @CacheEvict(value = "pages", key = "#pageId")
    })
    public void deleteById(Integer pageId) {
        log.info(DELETE_CACHE_ALL_PAGES_MESSAGE);
        log.info("database: deleted page " + pageId);
        log.info("cache pages: deleted page " + pageId);
        pageDAO.deleteById(pageId);
    }
}
