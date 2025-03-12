package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import util.DBUtil;

public class CourseApprovalDAO {
    public List<Course> getPendingCourses() throws SQLException {
        List<Course> pendingCourses = new ArrayList<>();
        String sql = "SELECT c.*, u.FirstName + ' ' + u.LastName as InstructorName " +
                    "FROM Courses c " +
                    "JOIN Users u ON c.InstructorID = u.UserID " +
                    "WHERE c.IsPublished = 0 AND c.isCancelled = 0";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("Description"));
                course.setPrice(rs.getBigDecimal("Price"));
                course.setImageURL(rs.getString("ImageURL"));
                course.setInstructorName(rs.getString("InstructorName"));
                course.setRequirements(rs.getString("Requirements"));
                course.setCreatedDate(rs.getTimestamp("CreatedDate"));
                pendingCourses.add(course);
            }
        }
        return pendingCourses;
    }
    
    public boolean approveCourse(int courseId) throws SQLException {
        String sql = "UPDATE Courses SET IsPublished = 1 WHERE CourseID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean rejectCourse(int courseId, String reason) throws SQLException {
        String sql = "UPDATE Courses SET isCancelled = 1 WHERE CourseID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            return stmt.executeUpdate() > 0;
        }
    }
}
