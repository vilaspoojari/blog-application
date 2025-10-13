package com.mountblue.blog.blog_application.repository;

import com.mountblue.blog.blog_application.dto.AuthorDto;
import com.mountblue.blog.blog_application.dto.PostPreviewDto;
import com.mountblue.blog.blog_application.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        SELECT DISTINCT new com.mountblue.blog.blog_application.dto.PostPreviewDto(
            p.id, p.title, p.excerpt, u.name, p.publishedAt
        )
        FROM Post p
        LEFT JOIN p.postTags pt
        LEFT JOIN pt.tag t
        LEFT JOIN p.author u
        WHERE
            (:authors IS NULL OR p.author.id IN :authors)
            AND (:tags IS NULL OR t.id IN :tags)
            AND (:search IS NULL OR
                 p.title ILIKE %:search% OR
                 p.content ILIKE %:search% OR
                 p.excerpt ILIKE %:search% OR
                 u.name ILIKE %:search% OR
                 t.name ILIKE %:search%
            )
            AND (p.publishedAt >= :startDate)
            AND (p.publishedAt <= :endDate)
        """)
    Page<PostPreviewDto> findPosts(
            @Param("authors") List<Long> authors,
            @Param("tags") List<Long> tags,
            @Param("search") String search,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query("SELECT DISTINCT new com.mountblue.blog.blog_application.dto.AuthorDto(p.author.id, p.author.name) FROM Post p")
    List<AuthorDto> fetchAuthorList();

    @Query("SELECT MIN(p.publishedAt) FROM Post p")
    LocalDateTime findEarliestPublishedAt();

    @Query("SELECT MAX(p.publishedAt) FROM Post p")
    LocalDateTime findLatestPublishedAt();

}
