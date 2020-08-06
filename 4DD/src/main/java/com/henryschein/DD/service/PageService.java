package com.henryschein.DD.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.henryschein.DD.dao.PageDAO;
import com.henryschein.DD.dto.PageDTO;
import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.entity.Page;
import com.henryschein.DD.service.cache.PageCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PageService {

    private PageDAO pageDAO;
    private PageCacheService pageCacheService;
    private Cache<Integer, DataElement> pageCache = Caffeine.newBuilder()
            .build();

    public PageService(PageDAO pageDAO, PageCacheService pageCacheService) {
        this.pageDAO = pageDAO;
        this.pageCacheService = pageCacheService;
    }

    public Page getById(Integer pageId) {
        return pageCacheService.getById(pageId);
    }

    public List<Page> getAll() {
        return pageCacheService.getAll();
    }

    public Page createNewPage(Integer bookId) {
        return pageCacheService.createNewPage(bookId);
    }

    public Page updateBookId(PageDTO pageDTO) {
        if (pageExists(pageDTO.getPageId())) {
            Page page = pageCacheService.getById(pageDTO.getPageId());
            page.setBookId(pageDTO.getBookId());
            return pageCacheService.updateBookId(page);
        }
        else {
            log.error("page " + pageDTO.getPageId() + " not found");
            return null;
        }
    }

    private boolean pageExists(Integer pageId) {
        Page page = pageCacheService.getById(pageId);
        return Objects.nonNull(page);
    }

    public void deleteById(Integer pageId) {
        if (pageExists(pageId)) {
            pageCacheService.deleteById(pageId);
        }
        else
            log.error("Cannot delete page with id: " + pageId + ". The page was not found");
    }
}
