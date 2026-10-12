package com.example.lodgebilling.database;

import com.example.lodgebilling.model.Bill;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:paradise_lodge.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createUsers = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE NOT NULL, password TEXT NOT NULL);";
        String createBills = "CREATE TABLE IF NOT EXISTS bills (bill_id TEXT PRIMARY KEY, guest_name TEXT NOT NULL, room_type TEXT NOT NULL, nights INTEGER NOT NULL, rate_per_night REAL NOT NULL, extras REAL NOT NULL, amount_paid REAL NOT NULL, total REAL NOT NULL, balance REAL NOT NULL);";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createBills);
            stmt.execute("INSERT OR IGNORE INTO users (username, password) VALUES ('admin', 'admin123');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    public static boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    public static void saveBill(Bill bill) throws SQLException {
        String sql = "INSERT OR REPLACE INTO bills(bill_id, guest_name, room_type, nights, rate_per_night, extras, amount_paid, total, balance) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bill.getBillId());
            pstmt.setString(2, bill.getGuestName());
            pstmt.setString(3, bill.getRoomType());
            pstmt.setInt(4, bill.getNights());
            pstmt.setDouble(5, bill.getRatePerNight());
            pstmt.setDouble(6, bill.getExtras());
            pstmt.setDouble(7, bill.getAmountPaid());
            pstmt.setDouble(8, bill.getTotal());
            pstmt.setDouble(9, bill.getBalance());
            pstmt.executeUpdate();
        }
    }

    public static List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM bills")) {
            while (rs.next()) {
                bills.add(new Bill(rs.getString("bill_id"), rs.getString("guest_name"), rs.getString("room_type"), rs.getInt("nights"), rs.getDouble("rate_per_night"), rs.getDouble("extras"), rs.getDouble("amount_paid")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return bills;
    }

    public static void deleteBill(String billId) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM bills WHERE bill_id = ?")) {
            pstmt.setString(1, billId);
            pstmt.executeUpdate();
        }
    }
}