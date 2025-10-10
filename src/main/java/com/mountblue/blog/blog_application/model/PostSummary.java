package com.mountblue.blog.blog_application.model;

import java.time.LocalDateTime;

public class PostSummary {

    private Long id;
    private String title;
    private String excerpt;
    private String authorName;
    private LocalDateTime publishedAt;

    public PostSummary(Long id, String excerpt, String title, String authorName, LocalDateTime publishedAt) {
        this.id = id;
        this.excerpt = excerpt;
        this.title = title;
        this.authorName = authorName;
        this.publishedAt = publishedAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString() {
        return "PostSummary{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", authorName='" + authorName + '\'' +
                ", publishedAt=" + publishedAt +
                '}';
    }
}