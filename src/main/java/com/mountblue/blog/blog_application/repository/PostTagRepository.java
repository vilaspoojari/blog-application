package com.mountblue.blog.blog_application.repository;

import com.mountblue.blog.blog_application.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository  extends JpaRepository<PostTag, Long> {
}
