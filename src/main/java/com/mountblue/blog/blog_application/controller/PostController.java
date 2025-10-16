package com.mountblue.blog.blog_application.controller;

import com.mountblue.blog.blog_application.dto.PostDto;
import com.mountblue.blog.blog_application.dto.PostFilterDto;
import com.mountblue.blog.blog_application.dto.PostPreviewDto;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.service.PostService;
import com.mountblue.blog.blog_application.service.TagService;
import com.mountblue.blog.blog_application.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private final PostService postService;
    private final TagService tagService;
    private final Utils utils;

    @Autowired
    public PostController(PostService postService, TagService tagService, Utils utils) {
        this.postService = postService;
        this.tagService = tagService;
        this.utils = utils;
    }

    @GetMapping("/")
    public String getPosts(PostFilterDto filterDto,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {

        Page<PostPreviewDto> postPage = postService.getPosts(filterDto);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("totalCount", postPage.getTotalElements());
        model.addAttribute("filterDto", filterDto);
        model.addAttribute("authors", postService.fetchAuthorList());
        model.addAttribute("tags", tagService.fetchTags());

        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        return "home";
    }

    @GetMapping("/post")
    public String showPost() {
        return "new_post";
    }

    @PostMapping("/post/new")
    public String savePost(@ModelAttribute PostDto post,
                           @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);

        postService.savePost(post, userEmail, isAdmin);
        return "redirect:/";
    }

    @GetMapping("/post/{postId}")
    public String showPost(@PathVariable int postId,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {

        boolean isAuthorOrAdmin = false;
        Post post = postService.fetchPostById(postId);

        if (userDetails != null) {
            isAuthorOrAdmin = utils.isAuthor(userDetails.getUsername(), post)
                    || utils.isRoleAdmin(userDetails);
        }

        model.addAttribute("post", post);
        model.addAttribute("isAuthor", isAuthorOrAdmin);

        return "post";
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable long postId,
                             @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);

        postService.deletePost(postId, userEmail, isAdmin);
        return "redirect:/";
    }

    @GetMapping("/post/edit/{postId}")
    public String editPost(@PathVariable Long postId,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);

        PostDto postDto = postService.fetchPostResponse(postId, userEmail, isAdmin);
        model.addAttribute("postDto", postDto);

        return "edit_post";
    }

    @PatchMapping("/post/update")
    public String updatePost(@ModelAttribute PostDto postDto,
                             @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);

        postService.updatePost(postDto, userEmail, isAdmin);
        return "redirect:/post/" + postDto.getId();
    }
}