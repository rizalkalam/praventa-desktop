module com.example.praventa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    requires jakarta.xml.bind;

    opens com.example.praventa.model to javafx.fxml, jakarta.xml.bind;
    opens com.example.praventa.controller to javafx.fxml;

    exports com.example.praventa;
    exports com.example.praventa.model;
    exports com.example.praventa.controller;
}
