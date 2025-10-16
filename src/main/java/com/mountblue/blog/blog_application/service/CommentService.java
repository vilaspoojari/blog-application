package com.mountblue.blog.blog_application.service;

import com.mountblue.blog.blog_application.dto.CommentDto;
import com.mountblue.blog.blog_application.exception.NoDataFound;
import com.mountblue.blog.blog_application.model.Comment;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.repository.CommentRepository;
import com.mountblue.blog.blog_application.repository.PostRepository;
import com.mountblue.blog.blog_application.util.Utils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final Utils utils;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          Utils utils){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.utils = utils;
    }

    public void addComment(CommentDto commentDto){
        try {
            Post post = postRepository.getReferenceById(commentDto.getId());

            Comment comment = new Comment();
            comment.setName(commentDto.getName());
            comment.setEmail(commentDto.getEmail());
            comment.setComment(commentDto.getComment());
            comment.setPost(post);

            commentRepository.save(comment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add comment. Please try again later. Error: " + e.getMessage());
        }
    }

    public Long deleteComment(long commentId, String userEmail, boolean isAdmin) {
        try {
            Comment comment = commentRepository.getReferenceById(commentId);
            Post post = comment.getPost();

            if(!isAdmin && utils.isAuthor(userEmail,post)){
                throw new AccessDeniedException("You are not authorized to delete this comment.");
            }

            Long postId = comment.getPost().getId();
            commentRepository.delete(comment);

            return postId;
        } catch (AccessDeniedException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to delete comment. Please try again later. Error: " + e.getMessage());
        }
    }

    public Comment fetchCommentById(Long commentId, String userEmail, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new NoDataFound("Comment Not Found with id " + commentId)
        );

        if(!isAdmin && !utils.isAuthor(userEmail,comment.getPost())){
            throw new AccessDeniedException("You are not authorized to edit this comment.");
        }

        return comment;
    }


    public Long updateComment(CommentDto commentDto, String userEmail, boolean isAdmin) {
        try {
            Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(
                    () -> new NoDataFound("Comment Not Found with id " + commentDto.getId())
            );
            Post post = comment.getPost();

            if(!isAdmin && !utils.isAuthor(userEmail,post)){
                throw new AccessDeniedException("You are not authorized to update this comment.");
            }

            comment.setComment(commentDto.getComment());
            commentRepository.save(comment);

            return post.getId();
        } catch (AccessDeniedException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to update comment. Please try again later. Error: " + e.getMessage());
        }
    }
}
