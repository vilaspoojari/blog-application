package com.mountblue.blog.blog_application.service;

import com.mountblue.blog.blog_application.dto.CommentDto;
import com.mountblue.blog.blog_application.dto.PostDto;
import com.mountblue.blog.blog_application.exception.NoDataFound;
import com.mountblue.blog.blog_application.model.Comment;
import com.mountblue.blog.blog_application.model.Post;
import com.mountblue.blog.blog_application.model.User;
import com.mountblue.blog.blog_application.repository.CommentRepository;
import com.mountblue.blog.blog_application.repository.PostRepository;
import com.mountblue.blog.blog_application.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    CommentRepository commentRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    public void addComment(CommentDto commentDto){
        try {
            Post post = postRepository.getReferenceById(commentDto.getId());
            User user = userRepository.findByName("Alice");
            Comment comment = new Comment();
            comment.setName(user.getName());
            int i=1/0;
            comment.setEmail(user.getEmail());
            comment.setComment(commentDto.getComment());
            comment.setPost(post);
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add comment. Please try again later. Error: " + e.getMessage());
        }
    }

    public Long deleteComment(long commentId) {
        try {
            Comment comment = commentRepository.getReferenceById(commentId);
            Long postId = comment.getPost().getId();
            commentRepository.delete(comment);
            return postId;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete comment. Please try again later. Error: " + e.getMessage());
        }
    }

    public Comment fetchCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()-> new NoDataFound("Comment Not Found with id " + commentId));
    }


    public Long updateComment(CommentDto commentDto) {
        try {
            Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(() -> new NoDataFound("Comment Not Found with id " + commentDto.getId()));
            Long postId = comment.getPost().getId();
            comment.setComment(commentDto.getComment());
            commentRepository.save(comment);
            return postId;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update comment. Please try again later. Error: " + e.getMessage());
        }
    }
}
