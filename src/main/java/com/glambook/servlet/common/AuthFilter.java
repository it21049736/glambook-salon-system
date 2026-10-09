package com.glambook.servlet.common;

import com.glambook.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Runs before the protected pages.
 * - Not logged in: sent to the login page.
 * - Logged in but not an admin trying to open /admin/...: sent back to the home page.
 */
@WebFilter(urlPatterns = {"/admin/*", "/profile", "/profile/*", "/appointments/*",
        "/payments/*", "/reviews/submit", "/reviews/edit", "/reviews/delete"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String contextPath = request.getContextPath();

        User user = SessionHelper.getLoggedUser(request);
        if (user == null) {
            SessionHelper.setFlashError(request, "Please log in to continue.");
            response.sendRedirect(contextPath + "/login");
            return;
        }

        // role check: canAccessAdmin() is overridden in Customer (false) and AdminUser (true)
        String path = request.getRequestURI().substring(contextPath.length());
        if (path.startsWith("/admin") && !user.canAccessAdmin()) {
            SessionHelper.setFlashError(request, "Only salon admins can open that page.");
            response.sendRedirect(contextPath + "/");
            return;
        }

        // allowed, so continue to the servlet
        chain.doFilter(req, res);
    }
}
