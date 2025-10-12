package com.mountblue.blog.blog_application.repository;

import com.mountblue.blog.blog_application.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
