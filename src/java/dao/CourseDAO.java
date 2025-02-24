/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Course;
import model.CourseFeedback;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DBUtil;

public class CourseDAO {

    private final DBUtil dbContext = new DBUtil();

    /*private Course extractCourseFromResultSet(ResultSet rs) throws Exception {
        Course course = new Course();
        course.setCourseID(rs.getInt("CourseID"));
        course.setCourseName(rs.getString("CourseName"));
        course.setDescription(rs.getString("Description"));
        course.setPrice(rs.getDouble("Price"));
        course.setLevelName("levelName");
        course.setLanguage(rs.getString("LanguageID"));
        course.setCreatedDate(rs.getString("CreatedDate"));
        course.setInstructorName("Jason Kim");
        course.setImageURL("imageURL");
        return course;
    }*/

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.CourseID, c.CourseName, c.[Description], c.Price, cl.LevelName, cla.LanguageName, c.CreatedDate, u.FirstName+' '+u.LastName AS InstructorName, c.ImageURL from Courses c \n"
                + "INNER JOIN CourseLevels cl ON c.LevelID = cl.LevelID INNER JOIN CourseLanguages cla ON c.LanguageID = cla.LanguageID\n"
                + "INNER JOIN Users u ON c.[InstructorID] = u.UserID AND u.RoleID = 2";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("Description"));
                course.setPrice(rs.getDouble("Price"));
                course.setLevelName(rs.getString("LevelName"));
                course.setLanguage(rs.getString("LanguageName"));
                course.setCreatedDate(rs.getString("CreatedDate"));
                course.setInstructorName(rs.getString("InstructorName"));
                course.setImageURL(rs.getString("ImageURL"));
                courses.add(course);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return courses;
    }

    public List<Course> getCoursesByLevel(String level) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.CourseID, c.CourseName, c.[Description], c.Price, cl.LevelName, cla.LanguageName, c.CreatedDate, u.FirstName+' '+u.LastName AS InstructorName, c.ImageURL from Courses c \n"
                + "INNER JOIN CourseLevels cl ON c.LevelID = cl.LevelID INNER JOIN CourseLanguages cla ON c.LanguageID = cla.LanguageID\n"
                + "INNER JOIN Users u ON c.[InstructorID] = u.UserID AND u.RoleID = 2 AND c.LevelID = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, level);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("Description"));
                course.setPrice(rs.getDouble("Price"));
                course.setLevelName(rs.getString("LevelName"));
                course.setLanguage(rs.getString("LanguageName"));
                course.setCreatedDate(rs.getString("CreatedDate"));
                course.setInstructorName(rs.getString("InstructorName"));
                course.setImageURL(rs.getString("ImageURL"));
                courses.add(course);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return courses;
    }
    
    

    public List<Course> searchCourses(String keyword) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.CourseID, c.CourseName, c.[Description], c.Price, cl.LevelName, cla.LanguageName, c.CreatedDate, u.FirstName+' '+u.LastName AS InstructorName, c.ImageURL from Courses c \n"
                + "INNER JOIN CourseLevels cl ON c.LevelID = cl.LevelID INNER JOIN CourseLanguages cla ON c.LanguageID = cla.LanguageID\n"
                + "INNER JOIN Users u ON c.[InstructorID] = u.UserID AND c.CourseName LIKE ? ";       
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("Description"));
                course.setPrice(rs.getDouble("Price"));
                course.setLevelName(rs.getString("LevelName"));
                course.setLanguage(rs.getString("LanguageName"));
                course.setCreatedDate(rs.getString("CreatedDate"));
                course.setInstructorName(rs.getString("InstructorName"));
                course.setImageURL(rs.getString("ImageURL"));
                courses.add(course);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return courses;

    }

    public List<Course> getCoursesSortedByDate(boolean newest) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.CourseID, c.CourseName, c.[Description], c.Price, cl.LevelName, cla.LanguageName, c.CreatedDate, u.FirstName+' '+u.LastName AS InstructorName, c.ImageURL from Courses c \n"
                + "INNER JOIN CourseLevels cl ON c.LevelID = cl.LevelID INNER JOIN CourseLanguages cla ON c.LanguageID = cla.LanguageID\n"
                + "INNER JOIN Users u ON c.[InstructorID] = u.UserID AND u.RoleID = 2 ORDER BY c.CreatedDate " + (newest ? "DESC" : "ASC");
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("Description"));
                course.setPrice(rs.getDouble("Price"));
                course.setLevelName(rs.getString("LevelName"));
                course.setLanguage(rs.getString("LanguageName"));
                course.setCreatedDate(rs.getString("CreatedDate"));
                course.setInstructorName(rs.getString("InstructorName"));
                course.setImageURL(rs.getString("ImageURL"));
                courses.add(course);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return courses;
    }

    public List<Course> getCoursesSortedByPrice(boolean highToLow) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.CourseID, c.CourseName, c.[Description], c.Price, cl.LevelName, cla.LanguageName, c.CreatedDate, u.FirstName+' '+u.LastName AS InstructorName, c.ImageURL from Courses c \n"
                + "INNER JOIN CourseLevels cl ON c.LevelID = cl.LevelID INNER JOIN CourseLanguages cla ON c.LanguageID = cla.LanguageID\n"
                + "INNER JOIN Users u ON c.[InstructorID] = u.UserID AND u.RoleID = 2 ORDER BY c.Price " + (highToLow ? "DESC" : "ASC");
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("Description"));
                course.setPrice(rs.getDouble("Price"));
                course.setLevelName(rs.getString("LevelName"));
                course.setLanguage(rs.getString("LanguageName"));
                course.setCreatedDate(rs.getString("CreatedDate"));
                course.setInstructorName(rs.getString("InstructorName"));
                course.setImageURL(rs.getString("ImageURL"));
                courses.add(course);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return courses;
    }

    public List<Course> getFeaturedCourses() throws Exception {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT TOP 6 c.CourseID, c.CourseName, c.Price, c.ImageURL, "
                + "u.FirstName + ' ' + u.LastName as InstructorName "
                + "FROM Courses c "
                + "JOIN Users u ON c.CreatedBy = u.UserID "
                + "WHERE c.IsPublished = 1 AND c.isCancelled = 0 "
                + "ORDER BY c.TotalEnrolled DESC";

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public List<CourseFeedback> getFeedbacksByCourseId(int courseId) {
        List<CourseFeedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.*, u.FirstName, u.LastName FROM CourseFeedback f "
                + "JOIN Users u ON f.StudentID = u.UserID "
                + "WHERE f.CourseID = ? ORDER BY f.FeedbackDate DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                CourseFeedback feedback = new CourseFeedback();
                feedback.setFeedbackID(rs.getInt("FeedbackID"));
                feedback.setCourseID(rs.getInt("CourseID"));
                feedback.setStudentID(rs.getInt("StudentID"));
                feedback.setFeedbackText(rs.getString("FeedbackText"));
                feedback.setRating(rs.getInt("Rating"));
                feedback.setFeedBackDate(rs.getString("FeedbackDate"));
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return feedbacks;
    }

    public Course getCourseById(int courseId) {
        String sql = "SELECT c.CourseID, c.CourseName, c.[Description], c.Price, cl.LevelName, "
                + "cla.LanguageName, c.CreatedDate, u.FirstName+' '+u.LastName AS InstructorName, "
                + "c.ImageURL, c.InstructorID "
                + "FROM Courses c "
                + "INNER JOIN CourseLevels cl ON c.LevelID = cl.LevelID "
                + "INNER JOIN CourseLanguages cla ON c.LanguageID = cla.LanguageID "
                + "INNER JOIN Users u ON c.InstructorID = u.UserID AND u.RoleID = 2 "
                + "WHERE c.CourseID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("Description"));
                course.setPrice(rs.getDouble("Price"));
                course.setLevelName(rs.getString("LevelName"));
                course.setLanguage(rs.getString("LanguageName"));
                course.setCreatedDate(rs.getString("CreatedDate"));
                course.setInstructorName(rs.getString("InstructorName"));
                course.setImageURL(rs.getString("ImageURL"));
                course.setInstructorId(rs.getInt("InstructorID"));
                return course;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
   
    
}
