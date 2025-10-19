package com.mountblue.blog.blog_application.dto;

import com.mountblue.blog.blog_application.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostFilterResponseDto {
    List<PostPreviewDto> posts;
    List<AuthorDto> authors;
    List<Tag> tags;
    long count;
}
