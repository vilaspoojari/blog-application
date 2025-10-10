package com.mountblue.blog.blog_application.service;

import com.mountblue.blog.blog_application.model.PostSummary;
import com.mountblue.blog.blog_application.repository.DashboardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public Page<PostSummary> getPostsSummary(List<String> authors,
                                             List<String> tags,
                                             LocalDate startDate,
                                             LocalDate endDate,
                                             String search,
                                             int page,
                                             int size,
                                             String sortField,
                                             String order) {

        Sort sort = Sort.by(sortField != null ? sortField : "publishedAt");
        sort = "desc".equalsIgnoreCase(order) ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        return dashboardRepository.findPostList(authors, tags, startDate, endDate, search, pageable);
    }

    public List<String> fetchAuthorList(){
        return dashboardRepository.fetchAuthorList();
    }
}