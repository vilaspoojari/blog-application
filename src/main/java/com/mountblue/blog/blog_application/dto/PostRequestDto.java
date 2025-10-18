package com.mountblue.blog.blog_application.dto;

import com.mountblue.blog.blog_application.model.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequestDto {

    private Long id;
    private String title;
    private String content;
    private List<String> tags;

    public static PostRequestDto fromEntity(Post post){
        PostRequestDto postDto = new PostRequestDto();

        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setTags(post.getPostTags().stream().map(postTag -> postTag.getTag().getName()).toList());
        return postDto;
    }

}
