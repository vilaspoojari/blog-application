package com.mountblue.blog.blog_application.repository;

import com.mountblue.blog.blog_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String alice);
}
