package com.example.praventa.utils;

import com.example.praventa.controller.BaseSidebarController;
import com.example.praventa.controller.user.SidebarController;
import com.example.praventa.model.users.User;

public class Session {
    private static User currentUser;
    private static boolean isUpdatedPersonalDisease = false;

    private static String defaultPage = "beranda_user.fxml";

    public static String getDefaultPage() {
        return defaultPage;
    }

    public static void setDefaultPage(String page) {
        defaultPage = page;
    }

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

    public static BaseSidebarController sidebarController;

    public static void setSidebarController(BaseSidebarController controller) {
        sidebarController = controller;
    }

    public static BaseSidebarController getSidebarController() {
        return sidebarController;
    }

    public static boolean isUpdatedPersonalDisease() {
        return isUpdatedPersonalDisease;
    }

    public static void setUpdatedPersonalDisease(boolean updated) {
        isUpdatedPersonalDisease = updated;
    }
}
