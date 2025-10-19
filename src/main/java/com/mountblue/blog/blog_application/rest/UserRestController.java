package com.mountblue.blog.blog_application.rest;

import com.mountblue.blog.blog_application.dto.RegisterDto;
import com.mountblue.blog.blog_application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) {
            userService.registerUser(registerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}