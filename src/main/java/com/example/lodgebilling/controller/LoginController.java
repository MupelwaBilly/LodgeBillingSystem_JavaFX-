package com.example.lodgebilling.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Default Credentials: admin / admin123
        if ("admin".equalsIgnoreCase(username) && "admin123".equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lodgebilling/lodge-billing.fxml"));
                Scene billingScene = new Scene(loader.load(), 900, 650);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Lodge Billing & Invoicing System");
                stage.setScene(billingScene);
                stage.setResizable(true);
                stage.centerOnScreen();
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Error loading main application view.");
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }
}