package com.mountblue.blog.blog_application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PostFilterDto {

    private int start = 0;
    private int limit = 10;
    private List<Long> authorIds;
    private List<Long> tagIds;
    private String sortField = "publishedAt";
    private String order = "desc";
    private String search;
    private LocalDate startDate;
    private LocalDate endDate;
}