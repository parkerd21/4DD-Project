package com.henryschein.DD.controller;

import com.henryschein.DD.dto.PageDTO;
import com.henryschein.DD.entity.Page;
import com.henryschein.DD.service.PageService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/")
    public Page add(@RequestBody PageDTO pageDTO) {
        return pageService.add(pageDTO);
    }

}
