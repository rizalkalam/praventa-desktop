module com.example.praventa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.example.praventa.controller to javafx.fxml;

    exports com.example.praventa;
}
