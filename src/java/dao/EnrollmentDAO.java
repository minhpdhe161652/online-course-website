/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.EnrolledCourseDTO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import util.DBUtil;

/**
 *
 * @author FPTSHOP
 */
public class EnrollmentDAO {
    
    public void enrollUser(int transactionID) {
        try (Connection conn = DBUtil.getConnection()) {
            // Get userID & courseID from transaction
            String query = "SELECT UserID, CourseID FROM Transactions t "
                    + "JOIN TransactionDetails td ON t.TransactionID = td.TransactionID "
                    + "WHERE t.TransactionID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, transactionID);
            var rs = ps.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt("UserID");
                int courseID = rs.getInt("CourseID");

                // Insert into CourseEnrollments
                String insertSQL = "INSERT INTO CourseEnrollments (StudentID, CourseID, EnrollmentDate, IsCompleted, CompletionDate) "
                        + "VALUES (?, ?, GETDATE(), 0, NULL)";
                PreparedStatement ps2 = conn.prepareStatement(insertSQL);
                ps2.setInt(1, userID);
                ps2.setInt(2, courseID);
                ps2.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<EnrolledCourseDTO> getEnrolledCoursesByUserId(int userId) {
        List<EnrolledCourseDTO> enrolledCourses = new ArrayList<>();
        String query = "SELECT ce.EnrollmentID, ce.EnrollmentDate, c.CourseID, c.CourseName, ce.IsCompleted "
                + "FROM CourseEnrollments ce "
                + "JOIN Courses c ON ce.CourseID = c.CourseID "
                + "WHERE ce.StudentID = ? "
                + "ORDER BY ce.EnrollmentDate DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EnrolledCourseDTO enrollment = new EnrolledCourseDTO(
                        rs.getInt("EnrollmentID"),
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getTimestamp("EnrollmentDate"),
                        rs.getBoolean("IsCompleted")
                    );
                    enrolledCourses.add(enrollment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enrolledCourses;
    }
    
    public boolean isUserEnrolled(int userId, int courseId) {
        String query = "SELECT COUNT(*) FROM CourseEnrollments WHERE StudentID = ? AND CourseID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
