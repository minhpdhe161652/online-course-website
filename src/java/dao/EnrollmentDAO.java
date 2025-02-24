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
            // Lấy userID & courseID từ transaction
            String query = "SELECT userID, courseID FROM Transactions WHERE transactionID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, transactionID);
            var rs = ps.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt("userID");
                int courseID = rs.getInt("courseID");

                // Thêm vào bảng Enrollment
                String insertSQL = "INSERT INTO Enrollment (userID, courseID) VALUES (?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(insertSQL);
                ps2.setInt(1, userID);
                ps2.setInt(2, courseID);
                ps2.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<EnrolledCourseDTO> getEnrolledCoursesByUserId(int userID) {
        List<EnrolledCourseDTO> enrolledCourses = new ArrayList<>();
        String query = "SELECT e.enrollmentID, e.enrollmentDate, c.courseID, c.courseName " +
                       "FROM Enrollment e " +
                       "JOIN Courses c ON e.courseID = c.courseID " +
                       "WHERE e.userID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EnrolledCourseDTO enrollment = new EnrolledCourseDTO(
                        rs.getInt("enrollmentId"),
                        rs.getInt("courseID"),
                        rs.getString("courseName"),
                        rs.getTimestamp("enrollmentDate")
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
        String query = "SELECT COUNT(*) FROM Enrollment WHERE userID = ? AND courseID = ?";
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
