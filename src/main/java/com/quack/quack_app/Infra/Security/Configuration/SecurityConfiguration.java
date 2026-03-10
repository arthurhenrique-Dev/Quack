package com.quack.quack_app.Infra.Security.Configuration;

import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Infra.Security.Filter.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf-> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.PUT, "/quack/user/customize/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/quack/user/customize/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/quack/user/follow/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/quack/user/unfollow/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/quack/review").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/quack/review/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/quack/review/**").hasAnyRole("USER", "MODERATOR")
                        .requestMatchers(HttpMethod.PUT, "/quack/management/**").hasRole("MODERATOR")
                        .requestMatchers(HttpMethod.POST, "/quack/management/**").hasRole("MODERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/quack/management/**").hasRole("MODERATOR")
                        .requestMatchers(HttpMethod.POST, "/quack/games").hasRole("MODERATOR")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
