module com.example.praventa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jakarta.xml.bind;
    requires java.net.http;
    requires com.google.gson;
    requires itextpdf;
    requires javafx.swing;
    requires javafx.web;

    opens com.example.praventa.model.users to jakarta.xml.bind;
    opens com.example.praventa.controller to javafx.fxml;
    opens com.example.praventa.controller.admin to javafx.fxml;
    opens com.example.praventa.controller.pakar to javafx.fxml;

    exports com.example.praventa;
    exports com.example.praventa.controller;
    exports com.example.praventa.model.articles;
    exports com.example.praventa.model.users;
    exports com.example.praventa.model.questionnaire;
    opens com.example.praventa.model.questionnaire to jakarta.xml.bind;

    opens com.example.praventa to javafx.fxml;
    exports com.example.praventa.controller.user;
    exports com.example.praventa.controller.admin;
    exports com.example.praventa.controller.pakar;
    opens com.example.praventa.controller.user to javafx.fxml;
    opens com.example.praventa.model.articles to jakarta.xml.bind, javafx.fxml;
}
