package com.mountblue.blog.blog_application.service;

import com.mountblue.blog.blog_application.dto.RegisterDto;
import com.mountblue.blog.blog_application.model.User;
import com.mountblue.blog.blog_application.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public void registerUser(RegisterDto registerDto){
        try {
            if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
                throw new IllegalArgumentException("Password and Confirm Password do not match");
            }

            User user = new User();

            user.setName(registerDto.getName());
            user.setEmail(registerDto.getEmail());

            String hashedPassword = passwordEncoder.encode(registerDto.getPassword());
            user.setPassword(hashedPassword);

            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw  e;
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User or email already exists. Please use a different email.");
        }catch (Exception e) {
            throw new RuntimeException("Failed to register user. Please try again later. Error: " + e.getMessage());
        }
    }

}
