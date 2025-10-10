package com.mountblue.blog.blog_application.repository;

import com.mountblue.blog.blog_application.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
