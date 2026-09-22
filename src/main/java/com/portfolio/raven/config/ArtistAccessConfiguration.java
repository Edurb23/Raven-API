package com.portfolio.raven.config;

import com.portfolio.raven.service.AdminControlService;
import jakarta.servlet.http.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import java.util.UUID;

@Configuration
public class ArtistAccessConfiguration implements WebMvcConfigurer {
    private final AdminControlService controls;
    public ArtistAccessConfiguration(AdminControlService controls) { this.controls = controls; }
    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String path = request.getRequestURI();
                if (request.getMethod().equals("OPTIONS")) return true;
                if (request.getMethod().equals("GET")) controls.requireEnabled("artist_catalog");
                if (path.endsWith("/vote") || path.endsWith("/votes")) controls.requireEnabled("artist_photo_voting");
                if (path.equals("/artist/upload/imagem")) controls.requireEnabled("artist_photo_uploads");
                if (path.endsWith("/vote") || path.endsWith("/votes")) {
                    var id = path.split("/")[2];
                    try { if (controls.blocked(UUID.fromString(id))) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"); }
                    catch (IllegalArgumentException error) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid artist ID"); }
                }
                return true;
            }
        }).addPathPatterns("/artist", "/artist/**");
    }
}
