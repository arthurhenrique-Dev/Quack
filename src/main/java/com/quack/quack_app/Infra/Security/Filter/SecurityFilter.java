package com.quack.quack_app.Infra.Security.Filter;

import com.quack.quack_app.Application.Ports.Output.Repositories.ModeratorRepository;
import com.quack.quack_app.Application.Ports.Output.Repositories.UserRepository;
import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Infra.Security.Service.TokenService;
import com.quack.quack_app.Infra.Security.Service.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final ModeratorRepository moderatorRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if (token != null) {
            var login = tokenService.validateToken(token);

            if (login != null) {
                var roleStr = tokenService.getClaim(token, "user_role");
                var emailVo = new Email(login);

                Optional<? extends BaseUser> userOptional;

                if ("MODERATOR".equals(roleStr)) {
                    userOptional = moderatorRepository.getModeratorByEmail(emailVo);
                } else {
                    userOptional = userRepository.getUserByEmail(emailVo);
                }

                userOptional.ifPresent(baseUser -> {
                    var userDetails = new UserDetailsImpl(baseUser);

                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}