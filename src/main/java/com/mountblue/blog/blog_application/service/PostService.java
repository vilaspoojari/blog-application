package com.mountblue.blog.blog_application.service;


import com.mountblue.blog.blog_application.dto.PostDto;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.model.PostTag;
import com.mountblue.blog.blog_application.model.Tag;
import com.mountblue.blog.blog_application.repository.PostRepository;
import com.mountblue.blog.blog_application.repository.PostTagRepository;
import com.mountblue.blog.blog_application.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository
            ,PostTagRepository postTagRepository){
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
    }

    @Transactional
    public void savePose(PostDto postDto){
        List<String> tagNames = postDto.getTags();
        String expert = postDto.getContent().substring(0, Math.min(postDto.getContent().length(), 100));
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setAuthor(postDto.getAuthor());
        post.setExcerpt(expert);
        post.setIsPublished(true);
        post.setPublishedAt(LocalDateTime.now());
        post = postRepository.save(post);
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    });

            PostTag postTag = new PostTag();
            postTag.setPost(post);
            postTag.setTag(tag);
            postTag.setId(new PostTag.PostTagId(post.getId(), tag.getId()));
            postTagRepository.save(postTag);
        }
    }

    public Optional<Post> fetchPostById(long id){
        return postRepository.findById(id);
    }
}
