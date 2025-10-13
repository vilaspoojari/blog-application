package com.mountblue.blog.blog_application.controller;

import com.mountblue.blog.blog_application.dto.PostDto;
import com.mountblue.blog.blog_application.dto.PostFilterDto;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.service.PostService;
import com.mountblue.blog.blog_application.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class PostController {

    private final PostService postService;
    private final TagService tagService;

    @Autowired
    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String getPosts(PostFilterDto filterDto, Model model) {
        var postPage = postService.getPosts(filterDto);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("totalCount", postPage.getTotalElements());
        model.addAttribute("filterDto", filterDto);
        model.addAttribute("authors", postService.fetchAuthorList());
        model.addAttribute("tags", tagService.fetchTags());

        return "index";
    }

    @GetMapping("/post")
    public String showPost() {
        return "new_post";
    }

    @PostMapping("/post/new")
    public String savePost(@ModelAttribute PostDto post) {
        postService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("/postId/{postId}")
    public String showPost(@PathVariable int postId, Model model) {
        Post post =  postService.fetchPostById(postId);
        model.addAttribute("post",post);
        return "post";
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable long postId){
        postService.deletePost(postId);
        return "redirect:/";
    }

    @GetMapping("/post/edit/{postId}")
    public String editPost(@PathVariable Long postId, Model model){
        PostDto postDto = postService.fetchPostResponse(postId);
        model.addAttribute("postDto",postDto);
        return "edit_post";
    }

    @PatchMapping("/post/update")
    public String updatePost(@ModelAttribute PostDto postDto){
        postService.updatePost(postDto);
        return "redirect:/";
    }
}
