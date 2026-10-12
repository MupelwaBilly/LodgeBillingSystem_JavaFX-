module com.example.lodgebilling {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.lodgebilling to javafx.fxml;
    opens com.example.lodgebilling.controller to javafx.fxml;
    opens com.example.lodgebilling.model to javafx.base;

    // ADD THIS LINE: Grants permission to load the photos
    opens com.example.lodgebilling.images;

    exports com.example.lodgebilling;
    exports com.example.lodgebilling.controller;
    exports com.example.lodgebilling.model;
}