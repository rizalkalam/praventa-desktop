package com.example.praventa.utils;

import com.example.praventa.controller.BaseSidebarController;
import com.example.praventa.controller.user.SidebarController;
import com.example.praventa.model.users.User;

public class Session {
    private static User currentUser;

    /**
     * Set user yang sedang login.
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Ambil user yang sedang aktif.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Cek apakah ada user yang sedang login.
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Ambil ID user aktif (jika ada).
     */
    public static String getCurrentUserId() {
        return (currentUser != null) ? currentUser.getId() + "" : null;
    }

    /**
     * Logout / clear session.
     */
    public static void clear() {
        currentUser = null;
    }

    private static BaseSidebarController sidebarController;

    public static void setSidebarController(BaseSidebarController controller) {
        sidebarController = controller;
    }

    public static BaseSidebarController getSidebarController() {
        return sidebarController;
    }

}
