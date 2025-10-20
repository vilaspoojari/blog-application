package com.mountblue.blog.blog_application.service;

import com.mountblue.blog.blog_application.dto.*;
import com.mountblue.blog.blog_application.exception.NoDataFound;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.model.PostTag;
import com.mountblue.blog.blog_application.model.Tag;
import com.mountblue.blog.blog_application.model.User;
import com.mountblue.blog.blog_application.repository.PostRepository;
import com.mountblue.blog.blog_application.repository.PostTagRepository;
import com.mountblue.blog.blog_application.repository.TagRepository;
import com.mountblue.blog.blog_application.repository.UserRepository;
import com.mountblue.blog.blog_application.util.Utils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final UserRepository userRepository;
    private final Utils utils;

    public PostService(PostRepository postRepository,
                       TagRepository tagRepository,
                       PostTagRepository postTagRepository,
                       UserRepository userRepository, Utils utils) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
        this.userRepository = userRepository;
        this.utils = utils;
    }

    @Transactional
    public Page<PostPreviewDto> getPosts(PostFilterRequestDto filter) {
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

            Pageable pageable = PageRequest.of(
                    page,
                    filter.getLimit(),
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

            if (start.isAfter(end)) {
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

    public List<AuthorDto> fetchAuthorList() {
        return postRepository.fetchAuthorList();
    }

    @Transactional
    public Long createPost(PostDto postDto, String email, boolean isAdmin) {
        try {
            List<String> tagNames = postDto.getTags();
            String excerpt = postDto.getContent().substring(0, Math.min(postDto.getContent().length(), 100));
            User user = userRepository.findByEmail(email);

            Post post = new Post();
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setAuthor(user);
            post.setExcerpt(excerpt);
            post.setIsPublished(true);
            post = postRepository.save(post);

            for (String tagName : tagNames) {
                if(tagName.isEmpty()){
                    continue;
                }

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
            return post.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save post, Error: " + e.getMessage());
        }
    }

    public Post fetchPostById(long postId) {
        try {
            return postRepository.findById(postId)
                    .orElseThrow(() -> new NoDataFound("Post Not Found with id " + postId));

        } catch (AccessDeniedException e) {
            throw e;
        }
        catch (Exception e) {
            throw new NoDataFound("Post Not Found with id " + postId);
        }
    }

    public Post fetchPostById(long postId, String userEmail, boolean isAdmin) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new NoDataFound("Post Not Found with id " + postId));

            if(!isAdmin && !utils.isAuthor(userEmail,post)){
                throw new AccessDeniedException("You are not authorized to edit this post.");
            }

            return post;

        } catch (AccessDeniedException e) {
            throw e;
        }
        catch (Exception e) {
            throw new NoDataFound("Post Not Found with id " + postId);
        }
    }

    public void deletePost(long postId, String userEmail, boolean isAdmin) {
        try {
            Post post = postRepository.getReferenceById(postId);

            if(!isAdmin && !utils.isAuthor(userEmail,post)){
                throw new AccessDeniedException("You are not authorized to delete this post.");
            }

            postRepository.deleteById(postId);
            List<Tag> unusedTags = tagRepository.findAll()
                    .stream()
                    .filter(tag -> tag.getPostTags().isEmpty())
                    .toList();

            if (!unusedTags.isEmpty()) {
                tagRepository.deleteAll(unusedTags);
            }
        } catch (AccessDeniedException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to delete post with id: " + postId);
        }
    }

    @Transactional
    public void updatePost(PostRequestDto postDto, String email, boolean isAdmin) {
        try {
            List<String> tagNames = postDto.getTags();
            String excerpt = postDto.getContent().substring(0, Math.min(postDto.getContent().length(), 100));
            User user = userRepository.findByEmail(email);

            Post post = postRepository.findById(postDto.getId())
                    .orElseThrow(() -> new NoDataFound("Post Not Found with id " + postDto.getId()));

            if(!utils.isAuthor(email,post) && !isAdmin){
                throw new AccessDeniedException("You are not authorized to edit this post.");
            }

            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setAuthor(user);
            post.setExcerpt(excerpt);
            post.setIsPublished(true);
            post = postRepository.save(post);

            for (String tagName : tagNames) {
                if(tagName.isEmpty()){
                    continue;
                }

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
        } catch (AccessDeniedException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to update post with id " + postDto.getId(), e);
        }
    }
}