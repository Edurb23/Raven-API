package com.portfolio.raven.security;

import com.portfolio.raven.entity.User;
import com.portfolio.raven.repository.UserRepository;
import com.portfolio.raven.service.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request);

        log.debug("[SecurityFilter] {} {} | token present: {}",
                request.getMethod(), request.getRequestURI(), token != null);

        if (token != null) {
            String login = tokenService.validateToken(token);

            log.debug("[SecurityFilter] token validated, subject (email): {}", login);

            if (login == null) {
                log.warn("[SecurityFilter] token validation FAILED for URI: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            User user = userRepository.findByEmail(login);

            if (user != null) {
                log.debug("[SecurityFilter] user found: {} | authorities: {}", user.getEmail(), user.getAuthorities());
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("[SecurityFilter] no user found for email: {}", login);
            }
        }

        filterChain.doFilter(request, response);
    }
}
