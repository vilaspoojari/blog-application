package com.mountblue.blog.blog_application.controller;

import com.mountblue.blog.blog_application.dto.PostDto;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PostController {
    private final PostService service;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/newpost")
    public String showNewPostForm() {
        return "new_post";
    }

    @PostMapping("/newpost")
    public String savePost(@ModelAttribute PostDto post) {
        service.savePose(post);
        return "redirect:/";
    }

    @GetMapping("/postId/{id}")
    public String showNewPostForm(@PathVariable int id, Model model) {

        Post post =  service.fetchPostById(id).get();
        model.addAttribute("post",post);
        return "post";
    }
}
