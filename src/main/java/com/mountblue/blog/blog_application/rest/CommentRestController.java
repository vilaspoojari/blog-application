package com.mountblue.blog.blog_application.rest;

import com.mountblue.blog.blog_application.dto.CommentDto;
import com.mountblue.blog.blog_application.service.CommentService;
import com.mountblue.blog.blog_application.util.Utils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/comments")
public class CommentRestController {
    private final CommentService commentService;
    private final Utils utils;

    public CommentRestController(CommentService commentService, Utils utils) {
        this.commentService = commentService;
        this.utils = utils;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody CommentDto commentDto) {
        commentService.addComment(commentDto);
        return new ResponseEntity<>("Comment added successfully", HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long commentId,
                                                @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);

        Long postId = commentService.deleteComment(commentId, userEmail, isAdmin);
        return new ResponseEntity<>("Comment deleted successfully: "+postId, HttpStatusCode.valueOf(200));
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateComment(@RequestBody CommentDto postDto,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        boolean isAdmin = utils.isRoleAdmin(userDetails);
        Long postId = commentService.updateComment(postDto, userEmail, isAdmin);
        return new ResponseEntity<>("Comment is updated successfully for post Id: "+postId,HttpStatusCode.valueOf(200));
    }
}
