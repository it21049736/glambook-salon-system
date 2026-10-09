package com.glambook.servlet.common;

import com.glambook.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Small helper so every servlet reads the logged in user and
 * stores one-time messages in the same way.
 */
public class SessionHelper {

    public static final String LOGGED_USER = "loggedUser";

    private SessionHelper() {
    }

    // returns the logged in user, or null if nobody is logged in
    public static User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute(LOGGED_USER);
    }

    public static boolean isAdmin(HttpServletRequest request) {
        User user = getLoggedUser(request);
        return user != null && user.canAccessAdmin();
    }

    // success message shown once on the next page (after a redirect)
    public static void setFlash(HttpServletRequest request, String message) {
        request.getSession().setAttribute("flash", message);
    }

    // error message shown once on the next page (after a redirect)
    public static void setFlashError(HttpServletRequest request, String message) {
        request.getSession().setAttribute("flashError", message);
    }
}
