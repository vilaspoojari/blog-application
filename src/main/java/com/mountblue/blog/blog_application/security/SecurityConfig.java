package com.mountblue.blog.blog_application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT email, password, true as enabled FROM users WHERE LOWER(email) = LOWER(?)"
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT email, role FROM users WHERE LOWER(email) = LOWER(?)"
        );

        return jdbcUserDetailsManager;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/v2/**"))
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(config ->
                        config
                                .requestMatchers(HttpMethod.GET, "/**", "/register", "/post/**", "/comments/add/**"
                                        , "/api/v2/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/register", "/comments/add",
                                        "/api/v2/register", "/api/v2/comments/add").permitAll()
                                .requestMatchers(HttpMethod.GET, "/post", "/post/edit/**", "/comments/edit/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/post/new",
                                        "/api/v2/post/new").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/post/**", "/comments/delete/**",
                                        "/api/v2/comments/delete/**","/api/v2/post/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/post/update", "/comments/update",
                                        "/api/v2/post/update", "/api/v2/comments/update").hasAnyRole("USER", "ADMIN")

                                .anyRequest().authenticated())

                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/", true)
                                .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return httpSecurity.build();
    }
}
