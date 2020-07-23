package com.henryschein.DD.controller;

import com.henryschein.DD.dto.PageDTO;
import com.henryschein.DD.entity.Page;
import com.henryschein.DD.service.PageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("page")
public class PageController {

    PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/{pageId}")
    public Page getById(@PathVariable Long pageId) {
        return pageService.getById(pageId);
    }

    @GetMapping("/all")
    public List<Page> getAll() { return pageService.getAll(); }

    @PostMapping("/")
    public Page createNewPage(@RequestParam Long bookId) {
        return pageService.createNewPage(bookId);
    }

    @PutMapping("/")
    public Page updateBookId(@RequestBody PageDTO pageDTO) {
        return pageService.updateBookId(pageDTO);
    }

    @DeleteMapping("/{pageId}")
    public void deleteById(@PathVariable Long pageId) { pageService.deleteById(pageId);
    }
}
