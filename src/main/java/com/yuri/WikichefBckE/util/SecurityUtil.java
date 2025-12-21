package com.yuri.WikichefBckE.util;

import com.yuri.WikichefBckE.modelo.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    /**
     * Gets the authenticated User entity from SecurityContext
     * In this application, User implements UserDetails, so the principal is a User instance
     * @return User entity if authenticated
     * @throws IllegalStateException if user is not authenticated
     */
    public static User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        
        Object principal = authentication.getPrincipal();
        
        // In this application, User implements UserDetails, so principal is a User instance
        if (principal instanceof User) {
            return (User) principal;
        }
        
        // This shouldn't happen in normal flow, but handle it gracefully
        throw new IllegalStateException("Authenticated principal is not a User instance");
    }

    /**
     * Gets the authenticated user's email (username) from SecurityContext
     * @return email if authenticated, null otherwise
     */
    public static String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        if (principal instanceof String) {
            return (String) principal;
        }
        
        return null;
    }
}

