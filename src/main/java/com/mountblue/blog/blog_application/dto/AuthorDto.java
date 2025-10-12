package com.mountblue.blog.blog_application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDto {
    private Long id;
    private String name;

    public AuthorDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
