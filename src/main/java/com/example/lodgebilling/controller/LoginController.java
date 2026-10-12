package com.example.lodgebilling.controller;

import com.example.lodgebilling.database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Label headerTitle;
    @FXML private Label headerSubtitle;
    @FXML private Button actionButton;
    @FXML private Hyperlink toggleLink;

    private boolean isRegisterMode = false;

    @FXML
    public void handlePrimaryAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMsg("Credentials required.", "#DC2626");
            return;
        }

        if (isRegisterMode) {
            if (DatabaseManager.registerUser(username, password)) {
                showMsg("Account created! Please sign in.", "#059669");
                toggleMode();
            } else {
                showMsg("Username unavailable.", "#DC2626");
            }
        } else {
            if (DatabaseManager.authenticateUser(username, password)) {
                loadDashboard(event);
            } else {
                showMsg("Access denied. Invalid credentials.", "#DC2626");
            }
        }
    }

    @FXML
    public void toggleMode() {
        isRegisterMode = !isRegisterMode;
        errorLabel.setText("");
        if (isRegisterMode) {
            headerTitle.setText("Create Account");
            headerSubtitle.setText("Register a new staff account.");
            actionButton.setText("Register");
            toggleLink.setText("Already have an account? Sign in");
        } else {
            headerTitle.setText("Welcome Back");
            headerSubtitle.setText("Please enter your details to sign in.");
            actionButton.setText("Sign In");
            toggleLink.setText("Create a new account");
        }
    }

    private void loadDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/lodgebilling/lodge-billing.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 1200, 800));
            stage.setTitle("Paradise Lodge - Dashboard");
            stage.centerOnScreen();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showMsg(String msg, String color) {
        errorLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
        errorLabel.setText(msg);
    }
}