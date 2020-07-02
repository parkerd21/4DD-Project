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
    public Page add(@RequestBody PageDTO pageDTO) {
        pageDTO.setPageId(null);
        return pageService.add(pageDTO);
    }

    @PutMapping("/")
    public Page update(@RequestBody PageDTO pageDTO) { return pageService.update(pageDTO); }




}
