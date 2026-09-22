package com.portfolio.raven.security;

import com.portfolio.raven.entity.User;
import com.portfolio.raven.service.AdminControlService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class AdminActivityFilter extends OncePerRequestFilter {
    private final AdminControlService controls;
    public AdminActivityFilter(AdminControlService controls) { this.controls = controls; }
    @Override protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return request.getMethod().equals("OPTIONS") || path.equals("/admin/logs") || path.startsWith("/swagger") || path.startsWith("/v3/");
    }
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        long start = System.nanoTime();
        boolean failed = false;
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String actor = authentication != null && authentication.getPrincipal() instanceof User user && user.getId() != null ? user.getId().toString() : null;
        try { chain.doFilter(request, response); }
        catch (ServletException | IOException | RuntimeException error) { failed = true; throw error; }
        finally {
            try { controls.record(actor, request.getMethod(), request.getRequestURI(), failed ? 500 : response.getStatus(), (System.nanoTime()-start)/1_000_000); }
            catch (RuntimeException error) { logger.warn("Could not persist request activity log"); }
        }
    }
}
