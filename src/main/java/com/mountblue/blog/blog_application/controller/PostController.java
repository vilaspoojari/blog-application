package com.mountblue.blog.blog_application.controller;

import com.mountblue.blog.blog_application.dto.PostDto;
import com.mountblue.blog.blog_application.dto.PostFilterDto;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.model.PostTag;
import com.mountblue.blog.blog_application.service.PostService;
import com.mountblue.blog.blog_application.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
public class PostController {
    private final PostService service;
    private final TagService tagService;

    @Autowired
    public PostController(PostService service, TagService tagService) {
        this.service = service;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String getPosts(PostFilterDto filterDto, Model model) {

        var postPage = service.getPosts(filterDto);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("totalCount", postPage.getTotalElements());
        model.addAttribute("filterDto", filterDto);

        model.addAttribute("authors", service.fetchAuthorList());
        model.addAttribute("tags", tagService.fetchTags());

        return "index";
    }


    @GetMapping("/newpost")
    public String showPost() {
        return "new_post";
    }

    @PostMapping("/newpost")
    public String savePost(@ModelAttribute PostDto post) {
        service.savePost(post);
        return "redirect:/";
    }

    @GetMapping("/postId/{id}")
    public String showPost(@PathVariable int id, Model model) {
        Post post =  service.fetchPostById(id);
        model.addAttribute("post",post);
        return "post";
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable long postId){
        service.deletePost(postId);
        return "redirect:/";
    }

    @GetMapping("/post/edit/{postId}")
    public String editPost(@PathVariable Long postId, Model model){
        PostDto postDto = service.fetchPostResponse(postId);
        model.addAttribute("postDto",postDto);
        return "edit_post";
    }


    @PatchMapping("/post/update")
    public String updatePost(@ModelAttribute PostDto postDto){
        service.updatePost(postDto);
        return "redirect:/";
    }

}
