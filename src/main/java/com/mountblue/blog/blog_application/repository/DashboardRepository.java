package com.mountblue.blog.blog_application.repository;

import com.mountblue.blog.blog_application.model.PostSummary;
import com.mountblue.blog.blog_application.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DashboardRepository extends JpaRepository<Post, Long> {

    @Query("""
            SELECT DISTINCT new com.mountblue.blog.blog_application.model.PostSummary(
                p.id, p.excerpt, p.title, p.author, p.publishedAt
            )
            FROM Post p
            JOIN p.postTags pt
            JOIN pt.tag t
            WHERE
                (:authors IS NULL OR p.author IN :authors)
                AND (:tags IS NULL OR t.name IN :tags)
                AND (
                    :search IS NULL OR 
                    p.title ILIKE %:search% OR
                    p.content ILIKE %:search% OR
                    p.author ILIKE %:search% OR
                    t.name ILIKE %:search%
                )
            """)
    Page<PostSummary> findPostList(
            @Param("authors") List<String> authors,
            @Param("tags") List<String> tags,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("SELECT DISTINCT p.author FROM Post p ORDER BY p.author")
    List<String> fetchAuthorList();
}