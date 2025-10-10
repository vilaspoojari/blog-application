package com.mountblue.blog.blog_application.repository;

import com.mountblue.blog.blog_application.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository  extends JpaRepository<Tag, Long> {
    @Query("SELECT DISTINCT t.name FROM Tag t ORDER BY t.name")
    List<String> findDistinctTagNames();

    Optional<Tag> findByName(String tagName);
}
