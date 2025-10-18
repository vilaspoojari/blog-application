package com.mountblue.blog.blog_application.rest;

import com.mountblue.blog.blog_application.dto.*;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.service.PostService;
import com.mountblue.blog.blog_application.service.TagService;
import com.mountblue.blog.blog_application.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v2")
public class PostRestController {

    private final PostService postService;
    private final TagService tagService;
    private final Utils utils;

    @Autowired
    public PostRestController(PostService postService, TagService tagService, Utils utils) {
        this.postService = postService;
        this.tagService = tagService;
        this.utils = utils;
    }

    @GetMapping
    public ResponseEntity<PostFilterResponseDto> getPosts( PostFilterRequestDto filterDto,
                           @AuthenticationPrincipal UserDetails userDetails) {

        Page<PostPreviewDto> postPage = postService.getPosts(filterDto);

        PostFilterResponseDto postFilterResponseDto = new PostFilterResponseDto();

        postFilterResponseDto.setPosts(postPage.getContent());
        postFilterResponseDto.setCount(postPage.getTotalElements());
        postFilterResponseDto.setAuthors(postService.fetchAuthorList());
        postFilterResponseDto.setTags(tagService.fetchTags());


        return new ResponseEntity<>(postFilterResponseDto,HttpStatusCode.valueOf(200));
    }

    @PostMapping("/post/new")
    public ResponseEntity<String> createPost(@RequestBody PostDto post,
                                             @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);

        Long postId = postService.createPost(post, userEmail, isAdmin);
        return new ResponseEntity<>("Successfully created new post with Id: "
                + postId, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Map<String,Object>> getPost(@PathVariable int postId,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        boolean isAuthorOrAdmin = false;

        Post post = postService.fetchPostById(postId);
        String userEmail = userDetails.getUsername();

        if (userEmail != null) {
            isAuthorOrAdmin = utils.isAuthor(userDetails.getUsername(), post)
                    || utils.isRoleAdmin(userDetails);
        }


        PostDto postDto = PostDto.fromEntity(post);

        Map<String, Object> response = new HashMap<>();

        response.put("post", postDto);
        response.put("isAuthor", isAuthorOrAdmin);

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable long postId,
                                             @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);

        postService.deletePost(postId, userEmail, isAdmin);
        return new ResponseEntity<>("Post deleted successfully - Post Id: ", HttpStatusCode.valueOf(200));
    }

    @PatchMapping("/post/update")
    public ResponseEntity<String> updatePost(@RequestBody PostRequestDto postDto,
                             @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);

        postService.updatePost(postDto, userEmail, isAdmin);
        return new ResponseEntity<>("Post Updated successfully - Post Id: ", HttpStatusCode.valueOf(200));
    }
}
