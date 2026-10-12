module com.example.lodgebilling {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Grants permission to use the database

    opens com.example.lodgebilling to javafx.fxml;
    opens com.example.lodgebilling.controller to javafx.fxml;

    // Grants permission for the TableView to read the properties in your Bill class
    opens com.example.lodgebilling.model to javafx.base;

    exports com.example.lodgebilling;
    exports com.example.lodgebilling.controller;
    exports com.example.lodgebilling.model;
}