module com.example.lodgebilling {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.lodgebilling to javafx.fxml;
    opens com.example.lodgebilling.controller to javafx.fxml;

    exports com.example.lodgebilling;
    exports com.example.lodgebilling.controller;
    exports com.example.lodgebilling.model;
}