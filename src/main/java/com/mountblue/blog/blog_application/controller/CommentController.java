package com.mountblue.blog.blog_application.controller;

import com.mountblue.blog.blog_application.dto.CommentDto;
import com.mountblue.blog.blog_application.model.Comment;
import com.mountblue.blog.blog_application.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {
    CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public String addComment(@ModelAttribute CommentDto commentDto){
        commentService.addComment(commentDto);
        return "redirect:/postId/" + commentDto.getId();
    }

    @DeleteMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable long commentId){
        Long postId = commentService.deleteComment(commentId);
        return "redirect:/postId/" + postId;
    }

    @GetMapping("/edit/{commentId}")
    public String editComment(@PathVariable long commentId, Model model){
        Comment comment = commentService.fetchCommentById(commentId);
        model.addAttribute("comment",comment);
        return "edit_comment";
    }

    @PatchMapping("/update")
    public String updateComment(@ModelAttribute CommentDto postDto){
        Long postId = commentService.updateComment(postDto);
        return "redirect:/postId/" + postId;
    }
}
