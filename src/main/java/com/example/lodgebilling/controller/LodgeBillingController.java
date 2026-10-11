package com.example.lodgebilling.controller;

import com.example.lodgebilling.model.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class LodgeBillingController {

    @FXML private TextField guestNameField;
    @FXML private ComboBox<String> roomTypeCombo;
    @FXML private TextField nightsField;
    @FXML private TextField extrasField;
    @FXML private TextField amountPaidField;
    @FXML private TextArea notesArea;
    @FXML private Label statusLabel;

    @FXML private TableView<Bill> billsTable;
    @FXML private TableColumn<Bill, String> colBillId;
    @FXML private TableColumn<Bill, String> colGuest;
    @FXML private TableColumn<Bill, String> colRoom;
    @FXML private TableColumn<Bill, Integer> colNights;
    @FXML private TableColumn<Bill, String> colTotal;
    @FXML private TableColumn<Bill, String> colPaid;
    @FXML private TableColumn<Bill, String> colBalance;

    private final ObservableList<Bill> savedBills = FXCollections.observableArrayList();
    private int nextBillNumber = 1;

    @FXML
    public void initialize() {
        // Initialize Room Types
        roomTypeCombo.setItems(FXCollections.observableArrayList("Standard", "Deluxe", "Family"));
        roomTypeCombo.getSelectionModel().selectFirst();

        // Setup Table Columns
        colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        colGuest.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colNights.setCellValueFactory(new PropertyValueFactory<>("nights"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("formattedPaid"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("formattedBalance"));

        billsTable.setItems(savedBills);
    }

    private double getRoomRate(String roomType) {
        return switch (roomType) {
            case "Standard" -> 350.00;
            case "Deluxe" -> 550.00;
            case "Family" -> 750.00;
            default -> 0.0;
        };
    }

    @FXML
    public void calculateBill(ActionEvent event) {
        try {
            Bill tempBill = createBillFromInput("TEMP");
            statusLabel.setText(String.format("Calculated Total: K %.2f | Balance: K %.2f", tempBill.getTotal(), tempBill.getBalance()));
        } catch (IllegalArgumentException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void saveBill(ActionEvent event) {
        try {
            String billId = String.format("B%03d", nextBillNumber);
            Bill newBill = createBillFromInput(billId);

            savedBills.add(newBill);
            nextBillNumber++;

            statusLabel.setText(String.format("Saved %s. Balance: K %.2f", billId, newBill.getBalance()));

        } catch (IllegalArgumentException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private Bill createBillFromInput(String billId) {
        String guestName = guestNameField.getText().trim();
        String roomType = roomTypeCombo.getValue();

        if (guestName.isEmpty() || nightsField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name and nights are required.");
        }

        try {
            int nights = Integer.parseInt(nightsField.getText().trim());
            double extras = extrasField.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(extrasField.getText().trim());
            double amountPaid = amountPaidField.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(amountPaidField.getText().trim());

            if (nights <= 0) throw new IllegalArgumentException("Nights must be greater than 0.");
            if (extras < 0 || amountPaid < 0) throw new IllegalArgumentException("Amounts cannot be negative.");

            double rate = getRoomRate(roomType);
            return new Bill(billId, guestName, roomType, nights, rate, extras, amountPaid);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric input for nights, extras, or paid amount.");
        }
    }

    @FXML
    public void clearForm(ActionEvent event) {
        guestNameField.clear();
        roomTypeCombo.getSelectionModel().selectFirst();
        nightsField.clear();
        extrasField.clear();
        amountPaidField.clear();
        notesArea.clear();
        statusLabel.setText("");
    }

    @FXML
    public void deleteSelectedBill(ActionEvent event) {
        Bill selected = billsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            savedBills.remove(selected);
            statusLabel.setText("Deleted bill: " + selected.getBillId());
        } else {
            statusLabel.setText("Please select a bill to delete.");
        }
    }
}