package com.mountblue.blog.blog_application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostPreviewDto {

    private Long id;
    private String title;
    private String excerpt;
    private String authorName;
    private LocalDateTime publishedAt;

    public PostPreviewDto(Long id, String title, String excerpt, String authorName, LocalDateTime publishedAt) {
        this.id = id;
        this.title = title;
        this.excerpt = excerpt;
        this.authorName = authorName;
        this.publishedAt = publishedAt;
    }

}