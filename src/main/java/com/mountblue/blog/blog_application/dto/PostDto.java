package com.mountblue.blog.blog_application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDto {

    private String title;
    private String content;
    private String author;
    private List<String> tags;
}