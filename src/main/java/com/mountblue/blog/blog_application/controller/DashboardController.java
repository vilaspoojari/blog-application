package com.mountblue.blog.blog_application.controller;

import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.model.PostSummary;
import com.mountblue.blog.blog_application.service.DashboardService;
import com.mountblue.blog.blog_application.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
public class DashboardController {
    private final DashboardService dashboardService;
    private final TagService tagService;

    @Autowired
    public  DashboardController(DashboardService dashboardService, TagService tagService){
        this.dashboardService = dashboardService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String getPostList(
            @RequestParam(required = false) String filterName,
            @RequestParam(required = false) List<String> authorName,
            @RequestParam(required = false) List<String> tagName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String order,
            Model model) {

        List<String> tags = tagService.fetchTags();
        List<String> authors = dashboardService.fetchAuthorList();
        List<String> filters = Arrays.asList("author", "published", "tags");
        LocalDate start = startDate != null && !startDate.isEmpty() ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null && !endDate.isEmpty() ? LocalDate.parse(endDate) : null;

        Page<PostSummary> postPage = dashboardService.getPostsSummary(
                authorName, tagName, start, end, search, page, limit, sortField, order
        );

        model.addAttribute("filters", filters);
        model.addAttribute("filterName", filterName);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("limit", limit);
        model.addAttribute("authors", authors);
        model.addAttribute("authorName", authorName);
        model.addAttribute("tags", tags);
        model.addAttribute("selectedTags", tagName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("search", search);
        model.addAttribute("sortField", sortField);
        model.addAttribute("order", order);

        return "index";
    }
}