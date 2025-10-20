package com.mountblue.blog.blog_application.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_tags")
@Getter
@Setter
public class PostTag {

    @EmbeddedId
    private PostTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Getter
    @Setter
    @Embeddable
    public static class PostTagId implements Serializable {
        private Long postId;
        private Long tagId;

        public PostTagId() {}

        public PostTagId(Long postId, Long tagId) {
            this.postId = postId;
            this.tagId = tagId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PostTagId)) return false;
            PostTagId that = (PostTagId) o;
            return postId.equals(that.postId) && tagId.equals(that.tagId);
        }

        @Override
        public int hashCode() {
            return postId.hashCode() + tagId.hashCode();
        }
    }
}