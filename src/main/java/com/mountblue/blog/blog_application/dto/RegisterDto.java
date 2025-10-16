package com.mountblue.blog.blog_application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

    private String name;
    private String email;
    private String password;
    private String confirmPassword;
}