package com.example.lodgebilling.controller;

import com.example.lodgebilling.database.DatabaseManager;
import com.example.lodgebilling.model.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.SQLException;

public class LodgeBillingController {

    @FXML private TextField guestNameField, nightsField, extrasField, amountPaidField;
    @FXML private ComboBox<String> roomTypeCombo;
    @FXML private Label statusLabel;
    @FXML private ImageView roomImageView;
    @FXML private TableView<Bill> billsTable;
    @FXML private TableColumn<Bill, String> colBillId, colGuest, colRoom, colTotal, colPaid, colBalance;
    @FXML private TableColumn<Bill, Integer> colNights;

    private final ObservableList<Bill> savedBills = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        roomTypeCombo.setItems(FXCollections.observableArrayList("Standard", "Deluxe", "Family"));
        roomTypeCombo.getSelectionModel().selectFirst();
        roomTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateRoomImage(newVal));

        colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        colGuest.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colNights.setCellValueFactory(new PropertyValueFactory<>("nights"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("formattedPaid"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("formattedBalance"));

        loadBills();
        updateRoomImage(roomTypeCombo.getValue());
    }

    private void updateRoomImage(String room) {
        if (room == null) return;

        String fileName = room.toLowerCase() + ".jpg";
        String imagePath = "/com/example/lodgebilling/images/" + fileName;

        try {
            // Using java.net.URL is the most bulletproof way in Java 21
            java.net.URL resourceUrl = getClass().getResource(imagePath);

            if (resourceUrl != null) {
                roomImageView.setImage(new Image(resourceUrl.toExternalForm()));
            } else {
                // Fallback: Check if they were accidentally placed in the root resources folder
                java.net.URL rootUrl = getClass().getResource("/" + fileName);
                if (rootUrl != null) {
                    roomImageView.setImage(new Image(rootUrl.toExternalForm()));
                } else {
                    System.out.println(" ERROR: Java cannot find " + fileName);
                    System.out.println("Please make sure the photo is placed exactly here:");
                    System.out.println("src/main/resources/com/example/lodgebilling/images/" + fileName);
                }
            }
        } catch (Exception e) {
            System.out.println(" ERROR loading photo: " + e.getMessage());
        }
    }

    private void loadBills() {
        savedBills.setAll(DatabaseManager.getAllBills());
        billsTable.setItems(savedBills);
    }

    @FXML
    public void calculateBill(ActionEvent event) {
        try {
            Bill temp = createBill("TEMP");
            showStatus(String.format("Calculated Total: K %.2f | Balance: K %.2f", temp.getTotal(), temp.getBalance()), "#2563EB");
        } catch (Exception e) { showStatus(e.getMessage(), "#DC2626"); }
    }

    @FXML
    public void saveBill(ActionEvent event) {
        try {
            String newId = "B" + String.format("%03d", savedBills.size() + 1);
            Bill bill = createBill(newId);
            DatabaseManager.saveBill(bill);
            loadBills();
            showStatus("Saved " + newId + " successfully.", "#059669");
            nightsField.clear(); extrasField.clear(); amountPaidField.clear();
        } catch (Exception e) { showStatus("Error: " + e.getMessage(), "#DC2626"); }
    }

    private Bill createBill(String id) {
        if (guestNameField.getText().trim().isEmpty() || nightsField.getText().trim().isEmpty())
            throw new IllegalArgumentException("Guest name and nights required.");

        int nights = Integer.parseInt(nightsField.getText().trim());
        double extras = extrasField.getText().isEmpty() ? 0 : Double.parseDouble(extrasField.getText().trim());
        double paid = amountPaidField.getText().isEmpty() ? 0 : Double.parseDouble(amountPaidField.getText().trim());
        double rate = switch(roomTypeCombo.getValue()) { case "Standard"->350; case "Deluxe"->550; case "Family"->750; default->0; };

        return new Bill(id, guestNameField.getText().trim(), roomTypeCombo.getValue(), nights, rate, extras, paid);
    }

    @FXML
    public void deleteSelectedBill(ActionEvent event) {
        Bill selected = billsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                DatabaseManager.deleteBill(selected.getBillId());
                loadBills();
                showStatus("Deleted " + selected.getBillId(), "#D97706");
            } catch (SQLException e) { showStatus("DB Error.", "#DC2626"); }
        } else showStatus("Select a bill to delete.", "#DC2626");
    }

    @FXML
    public void clearForm() {
        guestNameField.clear(); nightsField.clear(); extrasField.clear(); amountPaidField.clear();
        roomTypeCombo.getSelectionModel().selectFirst();
        statusLabel.setText("");
    }

    @FXML
    public void logout(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/com/example/lodgebilling/login.fxml")).load(), 850, 550));
        stage.centerOnScreen();
    }

    private void showStatus(String msg, String color) {
        statusLabel.setStyle("-fx-text-fill: " + color + ";");
        statusLabel.setText(msg);
    }
}