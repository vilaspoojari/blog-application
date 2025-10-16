package com.mountblue.blog.blog_application.controller;

import com.mountblue.blog.blog_application.dto.CommentDto;
import com.mountblue.blog.blog_application.model.Comment;
import com.mountblue.blog.blog_application.service.CommentService;
import com.mountblue.blog.blog_application.util.Utils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final Utils utils;

    public CommentController(CommentService commentService, Utils utils) {
        this.commentService = commentService;
        this.utils = utils;
    }

    @GetMapping("/add/{postId}")
    public String addComment(@PathVariable long postId, Model model) {
        model.addAttribute("post", postId);
        return "add_comment";
    }

    @PostMapping("/add")
    public String addComment(@ModelAttribute CommentDto commentDto) {
        commentService.addComment(commentDto);
        return "redirect:/post/" + commentDto.getId();
    }

    @DeleteMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable long commentId,
                                @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);
        Long postId = commentService.deleteComment(commentId, userEmail, isAdmin);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/edit/{commentId}")
    public String editComment(@PathVariable long commentId,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);
        Comment comment = commentService.fetchCommentById(commentId, userEmail, isAdmin);
        model.addAttribute("comment", comment);
        return "edit_comment";
    }

    @PatchMapping("/update")
    public String updateComment(@ModelAttribute CommentDto postDto,
                                @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);
        Long postId = commentService.updateComment(postDto, userEmail, isAdmin);
        return "redirect:/post/" + postId;
    }
}