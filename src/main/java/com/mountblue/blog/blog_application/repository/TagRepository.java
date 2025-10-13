package com.mountblue.blog.blog_application.repository;

import com.mountblue.blog.blog_application.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository  extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String tagName);

}
