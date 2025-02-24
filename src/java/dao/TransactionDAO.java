/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import model.Transaction;
import util.DBUtil;

/**
 *
 * @author FPTSHOP
 */
public class TransactionDAO {

    public int createTransaction(Transaction transaction) {
        String query = "INSERT INTO Transactions(userID, courseID, amount, status) OUTPUT INSERTED.transactionID VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transaction.getUserId());
            stmt.setInt(2, transaction.getCourseId());
            stmt.setDouble(3, transaction.getAmount());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("transactionID"); // Trả về transactionID vừa tạo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateTransaction(int transactionId, String status) {
        String query = "UPDATE Transactions SET status = ? WHERE transactionId = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, transactionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
