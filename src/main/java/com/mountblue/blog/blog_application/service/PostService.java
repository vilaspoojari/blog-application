package com.mountblue.blog.blog_application.service;


import com.mountblue.blog.blog_application.dto.AuthorDto;
import com.mountblue.blog.blog_application.dto.PostDto;
import com.mountblue.blog.blog_application.dto.PostFilterDto;
import com.mountblue.blog.blog_application.dto.PostPreviewDto;
import com.mountblue.blog.blog_application.exception.NoDataFound;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.model.PostTag;
import com.mountblue.blog.blog_application.model.Tag;
import com.mountblue.blog.blog_application.model.User;
import com.mountblue.blog.blog_application.repository.PostRepository;
import com.mountblue.blog.blog_application.repository.PostTagRepository;
import com.mountblue.blog.blog_application.repository.TagRepository;
import com.mountblue.blog.blog_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository
            , PostTagRepository postTagRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
        this.userRepository = userRepository;
    }

    @Transactional()
    public Page<PostPreviewDto>  getPosts(PostFilterDto filter) {
        try {
            int page = filter.getStart() / filter.getLimit();
            List<Long> authors = filter.getAuthorIds();
            if (authors != null && authors.isEmpty()) {
                authors = null;
            }

            List<Long> tags = filter.getTagIds();
            if (tags != null && tags.isEmpty()) {
                tags = null;
            }

            Pageable pageable = PageRequest.of(page, filter.getLimit(),
                    filter.getOrder().equalsIgnoreCase("asc")
                            ? Sort.by(filter.getSortField()).ascending()
                            : Sort.by(filter.getSortField()).descending()
            );

            LocalDateTime start = filter.getStartDate() != null
                    ? filter.getStartDate().atStartOfDay()
                    : postRepository.findEarliestPublishedAt();

            LocalDateTime end = filter.getEndDate() != null
                    ? filter.getEndDate().atTime(23, 59, 59)
                    : postRepository.findLatestPublishedAt();

            if (start == null || end == null) {
                return Page.empty(pageable);
            }

            if(start.isAfter(end)){
                throw new IllegalArgumentException("Start date cannot be after end date.");
            }

            return postRepository.findPosts(
                    authors,
                    tags,
                    filter.getSearch(),
                    start,
                    end,
                    pageable
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch posts list, Error: " + e.getMessage());
        }
    }

    public List<AuthorDto> fetchAuthorList(){
        return postRepository.fetchAuthorList();
    }

    @Transactional
    public void savePost(PostDto postDto){
        try {
            List<String> tagNames = postDto.getTags();
            String expert = postDto.getContent().substring(0, Math.min(postDto.getContent().length(), 100));
            User user = userRepository.findByName("Alice");
            Post post = new Post();
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setAuthor(user);
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to save post, Error: "+ e.getMessage());
        }
    }

    public Post fetchPostById(long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NoDataFound("Post Not Found with id " + id)
        );
    }

    public PostDto fetchPostResponse(Long postId) {
        Post post = fetchPostById(postId);
        PostDto postDto = new PostDto();
        postDto.setId(postId);
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        List<String> tags = post.getPostTags()
                .stream()
                .map(postTag -> postTag.getTag().getName())
                .toList();
        postDto.setTags(tags);
        return postDto;
    }

    public void deletePost(long postId) {
        postRepository.deleteById(postId);

        List<Tag> unusedTags = tagRepository.findAll()
                .stream()
                .filter(tag -> tag.getPostTags().isEmpty())
                .toList();

        if (!unusedTags.isEmpty()) {
            tagRepository.deleteAll(unusedTags);
        }
    }

    @Transactional
    public void updatePost(PostDto postDto) {
        try {
            List<String> tagNames = postDto.getTags();
            String expert = postDto.getContent().substring(0, Math.min(postDto.getContent().length(), 100));
            User user = userRepository.findByName("Alice");
            Post post = postRepository.findById(postDto.getId()).orElseThrow(
                    () -> new NoDataFound("Post Not Found with id " + postDto.getId())
            );

            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setAuthor(user);
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
        }catch (NoDataFound e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException("Failed to update post with id " + postDto.getId(), e);
        }
    }
}
