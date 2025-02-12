/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.DBUtil;

public class CourseDAO {

    private final DBUtil dbContext = new DBUtil();

    public List<Course> getCoursesByLevel(String levelName) {
        if (levelName == null || levelName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.CourseID, c.CourseName, c.Description, l.LevelName, c.Price, c.ImageURL "
                    + "FROM Courses c "
                    + "JOIN CourseLevels l ON c.LevelID = l.LevelID "
                    + "WHERE l.LevelName = ?";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, levelName.trim());
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setCourseID(rs.getInt("CourseID"));
                    course.setCourseName(rs.getString("CourseName"));
                    course.setDescription(rs.getString("Description"));
                    course.setLevelName(rs.getString("LevelName"));
                    course.setPrice(rs.getDouble("Price"));
                    course.setImageURL(rs.getString("ImageURL"));
                    courses.add(course);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting courses by level: " + levelName);
            e.printStackTrace();
            throw new RuntimeException("Failed to get courses by level: " + e.getMessage(), e);
        }
        return courses;
    }
    
    public List<Course> getFeaturedCourses() throws Exception {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT TOP 6 c.CourseID, c.CourseName, c.Price, c.ImageURL, " +
                    "u.FirstName + ' ' + u.LastName as InstructorName " +
                    "FROM Courses c " +
                    "JOIN Users u ON c.CreatedBy = u.UserID " +
                    "WHERE c.IsPublished = 1 AND c.isCancelled = 0 " +
                    "ORDER BY c.TotalEnrolled DESC";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setPrice(rs.getDouble("Price"));
                course.setImageURL(rs.getString("ImageURL"));
                course.setInstructorName(rs.getString("InstructorName"));
                courses.add(course);
            }
        }
        return courses;
    }
}
