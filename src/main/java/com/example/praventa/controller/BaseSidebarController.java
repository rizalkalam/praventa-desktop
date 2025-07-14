package com.example.praventa.controller;

import javafx.scene.layout.AnchorPane;

public abstract class BaseSidebarController {
    protected AnchorPane contentTarget;

    public void setMainContent(AnchorPane contentTarget) {
        this.contentTarget = contentTarget;
        initializeUserProfile();
    }

    protected void initializeUserProfile() {
        // Bisa isi default info username / avatar jika dibutuhkan
    }

    public abstract void loadPage(String fxmlName);
}
