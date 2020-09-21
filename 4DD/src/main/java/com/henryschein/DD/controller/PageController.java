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
    public Page getById(@PathVariable Integer pageId) {
        return pageService.getById(pageId);
    }

    @GetMapping("/all")
    public List<Page> getAll() { return pageService.getAll(); }

    @PostMapping("/")
    public Page createNewPage(@RequestParam Integer bookId) {
        return pageService.createNewPage(bookId);
    }

    @PutMapping("/")
    public Page updateBookId(@RequestBody PageDTO pageDTO) {
        return pageService.updatePageWithBookId(pageDTO);
    }

    @DeleteMapping("/{pageId}")
    public void deleteById(@PathVariable Integer pageId) {
        pageService.deletePageById(pageId);
    }
}
