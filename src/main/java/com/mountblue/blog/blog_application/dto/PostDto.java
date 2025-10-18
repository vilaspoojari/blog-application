package com.mountblue.blog.blog_application.dto;

import com.mountblue.blog.blog_application.model.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private List<String> tags;
    private List<CommentDto> comments;

    public static PostDto fromEntity(Post post){
        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setTags(post.getPostTags().stream().map(postTag -> postTag.getTag().getName()).toList());
        postDto.setComments(post
                .getComments()
                .stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(comment.getId());
                    commentDto.setName(comment.getName());
                    commentDto.setEmail(comment.getEmail());
                    commentDto.setComment(comment.getComment());
                    return commentDto;
                })
                .toList());
        return postDto;
    }
}