package com.mountblue.blog.blog_application.util;

import com.mountblue.blog.blog_application.model.Post;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class Utils {
    public boolean isAuthor(String userEmail, Post post) {
        return post.getAuthor().getEmail().equals(userEmail);
    }

    public boolean isRoleAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
