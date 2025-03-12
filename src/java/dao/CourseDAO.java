/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import model.Course;
import model.Category;
import model.Subcategory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
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
    
    public boolean addCourse(Course course, int createdBy) {
        String sql = "INSERT INTO Courses (CourseName, Description, LevelID, Price, ImageURL, CreatedBy, IsPublished, IsCancelled, CreatedDate) "
                    + "VALUES (?, ?, (SELECT LevelID FROM CourseLevels WHERE LevelName = ?), ?, ?, ?, ?, 0, 0, GETDATE())";
                    
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());
            ps.setString(3, course.getLevelName());
            ps.setBigDecimal(4, course.getPrice());
            ps.setString(5, course.getImageURL());
            ps.setInt(6, createdBy);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("Error adding new course: " + course.getCourseName());
            e.printStackTrace();
            return false;
        }
    }

    public int saveBasicInfo(Course course, int createdBy) {
        // Validate input
        if (course == null || course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            throw new IllegalArgumentException("Course name is required");
        }
        
        String sql = "INSERT INTO Courses (CourseName, Description, LevelID, SubcategoryID, Price, ImageURL, " +
                    "CreatedBy, IsPublished, IsCancelled, LanguageID, InstructorID, CreatedDate, LastUpdate) " +
                    "VALUES (?, ?, " +
                    "(SELECT LevelID FROM CourseLevels WHERE LevelName = ?), " +
                    "?, ?, ?, ?, 0, 0, ?, ?, GETDATE(), GETDATE())";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Log debug information
            System.out.println("Debug saveBasicInfo - Giá trị đầu vào:");
            System.out.println("CourseName: " + course.getCourseName());
            System.out.println("Description: " + course.getDescription());
            System.out.println("LevelName: " + course.getLevelName());
            System.out.println("SubcategoryID: " + course.getSubcategoryID());
            System.out.println("Price: " + course.getPrice());
            System.out.println("ImageURL: " + course.getImageURL());
            System.out.println("CreatedBy: " + createdBy);
            System.out.println("LanguageID: " + course.getLanguageID());
            
            int paramIndex = 1;
            ps.setString(paramIndex++, course.getCourseName().trim());
            ps.setString(paramIndex++, course.getDescription());
            
            // Xử lý LevelName
            if (course.getLevelName() == null || course.getLevelName().trim().isEmpty()) {
                ps.setString(paramIndex++, "Beginner"); // Mặc định là Beginner nếu không có
            } else {
                ps.setString(paramIndex++, course.getLevelName());
            }
            
            // Xử lý SubcategoryID
            if (course.getSubcategoryID() <= 0) {
                ps.setNull(paramIndex++, java.sql.Types.INTEGER);
            } else {
                ps.setInt(paramIndex++, course.getSubcategoryID());
            }
            
            // Kiểm tra null và xử lý giá trị price (mặc định là 0 VNĐ)
            if (course.getPrice() != null) {
                ps.setBigDecimal(paramIndex++, course.getPrice());
            } else {
                ps.setBigDecimal(paramIndex++, BigDecimal.ZERO);
            }
            
            ps.setString(paramIndex++, course.getImageURL());
            ps.setInt(paramIndex++, createdBy);
            
            // Xử lý LanguageID
            if (course.getLanguageID() <= 0) {
                ps.setInt(paramIndex++, 1); // Mặc định là 1 (tiếng Anh)
            } else {
                ps.setInt(paramIndex++, course.getLanguageID());
            }
            
            // InstructorID = CreatedBy nếu chưa có
            if (course.getInstructorID() <= 0) {
                ps.setInt(paramIndex++, createdBy);
            } else {
                ps.setInt(paramIndex++, course.getInstructorID());
            }
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            return -1;
            
        } catch (Exception e) {
            System.err.println("Error saving basic course info: " + course.getCourseName());
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT c.CategoryID, c.CategoryName, " +
                    "s.SubcategoryID, s.SubcategoryName " +
                    "FROM Categories c " +
                    "LEFT JOIN Subcategories s ON c.CategoryID = s.CategoryID " +
                    "ORDER BY c.CategoryID, s.SubcategoryID";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                Category currentCategory = null;
                
                while (rs.next()) {
                    int categoryId = rs.getInt("CategoryID");
                    
                    // Nếu là category mới
                    if (currentCategory == null || currentCategory.getCategoryID() != categoryId) {
                        currentCategory = new Category();
                        currentCategory.setCategoryID(categoryId);
                        currentCategory.setCategoryName(rs.getString("CategoryName"));
                        categories.add(currentCategory);
                    }
                    
                    // Thêm subcategory nếu có
                    int subcategoryId = rs.getInt("SubcategoryID");
                    if (!rs.wasNull()) {
                        Subcategory subcategory = new Subcategory();
                        subcategory.setSubcategoryID(subcategoryId);
                        subcategory.setCategoryID(categoryId);
                        subcategory.setSubcategoryName(rs.getString("SubcategoryName"));
                        currentCategory.addSubcategory(subcategory);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting categories");
            e.printStackTrace();
        }
        return categories;
    }

    public List<Course> getPendingCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Courses WHERE IsPublished = 0 AND isCancelled = 0 ORDER BY CreatedDate DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setTitle(rs.getString("Title"));
                course.setDescription(rs.getString("Description"));
                course.setInstructorID(rs.getInt("InstructorID"));
                course.setPrice(rs.getBigDecimal("Price"));
                course.setCategoryID(rs.getInt("CategoryID"));
                course.setSubcategoryID(rs.getInt("SubcategoryID"));
                course.setLanguageID(rs.getInt("LanguageID"));
                course.setLevel(rs.getString("Level"));
                course.setCreatedDate(rs.getTimestamp("CreatedDate"));
                course.setUpdatedDate(rs.getTimestamp("UpdatedDate"));
                course.setIsPublished(rs.getBoolean("IsPublished"));
                course.setTotalEnrolled(rs.getInt("TotalEnrolled"));
                course.setIsCancelled(rs.getBoolean("IsCancelled"));
                courses.add(course);
            }
            
        } catch (Exception e) {
            System.err.println("Error getting pending courses");
            e.printStackTrace();
        }
        return courses;
    }
    
    public List<Course> getLatestApprovedCourses(int limit) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT TOP ? * FROM Courses " +
                    "WHERE IsPublished = 1 AND isCancelled = 0 " +
                    "ORDER BY UpdatedDate DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setTitle(rs.getString("Title"));
                course.setDescription(rs.getString("Description"));
                course.setInstructorID(rs.getInt("InstructorID"));
                course.setPrice(rs.getBigDecimal("Price"));
                course.setCategoryID(rs.getInt("CategoryID"));
                course.setSubcategoryID(rs.getInt("SubcategoryID"));
                course.setLanguageID(rs.getInt("LanguageID"));
                course.setLevel(rs.getString("Level"));
                course.setCreatedDate(rs.getTimestamp("CreatedDate"));
                course.setUpdatedDate(rs.getTimestamp("UpdatedDate"));
                course.setIsPublished(rs.getBoolean("IsPublished"));
                course.setTotalEnrolled(rs.getInt("TotalEnrolled"));
                course.setIsCancelled(rs.getBoolean("IsCancelled"));
                courses.add(course);
            }
            
        } catch (Exception e) {
            System.err.println("Error getting latest approved courses");
            e.printStackTrace();
        }
        return courses;
    }
    
    public boolean updateCourse(Course course) {
        String sql = "UPDATE Courses SET " +
                    "CourseName = ?, Description = ?, InstructorID = ?, Price = ?, " +
                    "SubcategoryID = ?, LanguageID = ?, LevelID = " +
                    "(SELECT LevelID FROM CourseLevels WHERE LevelName = ?), " +
                    "LastUpdate = GETDATE(), IsPublished = ?, " +
                    "IsCancelled = ?, ImageURL = ? " +
                    "WHERE CourseID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Debug thông tin trước khi cập nhật
            System.out.println("DEBUG updateCourse - Thông tin khóa học:");
            System.out.println("CourseID: " + course.getCourseID());
            System.out.println("CourseName: " + course.getCourseName());
            System.out.println("Description: " + course.getDescription());
            System.out.println("InstructorID: " + course.getInstructorID());
            System.out.println("SubcategoryID: " + course.getSubcategoryID());
            System.out.println("LanguageID: " + course.getLanguageID());
            System.out.println("LevelName: " + course.getLevelName());
            System.out.println("Price (VND): " + (course.getPrice() != null ? course.getPrice() : BigDecimal.ZERO));
            System.out.println("IsPublished: " + course.getIsPublished());
            System.out.println("IsCancelled: " + course.getIsCancelled());
            
            // Kiểm tra các trường bắt buộc
            if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
                throw new IllegalArgumentException("Tên khóa học không được để trống");
            }
            
            if (course.getCourseID() <= 0) {
                throw new IllegalArgumentException("ID khóa học không hợp lệ: " + course.getCourseID());
            }
            
            int paramIndex = 1;
            ps.setString(paramIndex++, course.getCourseName());
            ps.setString(paramIndex++, course.getDescription());
            ps.setInt(paramIndex++, course.getInstructorID());
            
            // Xử lý giá trị price, mặc định là 0 VND nếu null
            ps.setBigDecimal(paramIndex++, course.getPrice() != null ? course.getPrice() : BigDecimal.ZERO);
            
            // Xử lý các trường có thể null
            if (course.getSubcategoryID() <= 0) {
                ps.setNull(paramIndex++, java.sql.Types.INTEGER);
            } else {
                ps.setInt(paramIndex++, course.getSubcategoryID());
            }
            
            if (course.getLanguageID() <= 0) {
                ps.setNull(paramIndex++, java.sql.Types.INTEGER);
            } else {
                ps.setInt(paramIndex++, course.getLanguageID());
            }
            
            // Xử lý LevelName
            if (course.getLevelName() == null || course.getLevelName().trim().isEmpty()) {
                ps.setString(paramIndex++, "Beginner"); // Mặc định là Beginner nếu không có
            } else {
                ps.setString(paramIndex++, course.getLevelName());
            }
            
            ps.setBoolean(paramIndex++, course.getIsPublished());
            ps.setBoolean(paramIndex++, course.getIsCancelled());
            ps.setString(paramIndex++, course.getImageURL());
            ps.setInt(paramIndex++, course.getCourseID());
            
            int rowsAffected = ps.executeUpdate();
            System.out.println("Số dòng bị ảnh hưởng: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật khóa học: " + course.getCourseName());
            System.err.println("Chi tiết lỗi: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM Courses WHERE CourseID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course();
                    course.setCourseID(rs.getInt("CourseID"));
                    course.setCourseName(rs.getString("CourseName"));
                    course.setTitle(rs.getString("Title"));
                    course.setDescription(rs.getString("Description"));
                    course.setLevelName(rs.getString("LevelName"));
                    course.setPrice(rs.getDouble("Price"));
                    course.setImageURL(rs.getString("ImageURL"));
                    course.setInstructorID(rs.getInt("InstructorID"));
                    course.setCategoryID(rs.getInt("CategoryID"));
                    course.setSubcategoryID(rs.getInt("SubcategoryID"));
                    course.setLanguageID(rs.getInt("LanguageID"));
                    course.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    course.setUpdatedDate(rs.getTimestamp("UpdatedDate"));
                    course.setIsPublished(rs.getBoolean("IsPublished"));
                    course.setTotalEnrolled(rs.getInt("TotalEnrolled"));
                    course.setIsCancelled(rs.getBoolean("IsCancelled"));
                    return course;
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting course by ID: " + courseId);
            e.printStackTrace();
        }
        return null;
    }

    public int getChapterCount(int courseId) {
        String sql = "SELECT COUNT(*) as ChapterCount FROM Chapters WHERE CourseID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ChapterCount");
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting chapter count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getChaptersWithoutLessons(int courseId) {
        List<String> chaptersWithoutLessons = new ArrayList<>();
        String sql = "SELECT c.ChapterID, c.ChapterNumber, c.Title " +
                    "FROM Chapters c " +
                    "LEFT JOIN Lessons l ON c.ChapterID = l.ChapterID " +
                    "WHERE c.CourseID = ? " +
                    "GROUP BY c.ChapterID, c.ChapterNumber, c.Title " +
                    "HAVING COUNT(l.LessonID) = 0 " +
                    "ORDER BY c.ChapterNumber";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String chapterInfo = String.format("Chương %d - %s", 
                        rs.getInt("ChapterNumber"), 
                        rs.getString("Title"));
                    chaptersWithoutLessons.add(chapterInfo);
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking chapters without lessons: " + e.getMessage());
            e.printStackTrace();
        }
        return chaptersWithoutLessons;
    }
    
    public List<Course> getCoursesByInstructor(int instructorId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Courses WHERE InstructorID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, instructorId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseID(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("Description"));
                course.setLevelName(rs.getString("LevelName"));
                course.setPrice(rs.getDouble("Price"));
                course.setImageURL(rs.getString("ImageURL"));
                course.setInstructorID(rs.getInt("InstructorID"));
               
                course.setCreatedDate(rs.getTimestamp("CreatedDate"));
                courses.add(course);
            }
            
        } catch (Exception e) {
            System.err.println("Error getting courses by instructor: " + instructorId);
            e.printStackTrace();
        }
        return courses;
    }
    
    public boolean updateCourseInfo(Course course) {
        String sql = "UPDATE Courses SET CourseName = ?, Description = ?, LevelName = ?, "
                + "Price = ?, ImageURL = ? WHERE CourseID = ? AND InstructorID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());
            ps.setString(3, course.getLevelName());
            ps.setBigDecimal(4, course.getPrice());
            ps.setString(5, course.getImageURL());
            ps.setInt(6, course.getCourseID());
            ps.setInt(7, course.getInstructorID());
            
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.err.println("Error updating course info: " + course.getCourseName());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM Courses WHERE CourseID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, courseId);
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            System.err.println("Error deleting course: " + courseId);
            e.printStackTrace();
            return false;
        }
    }

    public Course getCourseByNameAndInstructor(String courseName, int instructorId) {
        String sql = "SELECT * FROM Courses WHERE CourseName = ? AND InstructorID = ?";
        System.out.println("DEBUG - Tìm khóa học với tên: " + courseName + " và instructorId: " + instructorId);
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseName);
            ps.setInt(2, instructorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course();
                    course.setCourseID(rs.getInt("CourseID"));
                    course.setCourseName(rs.getString("CourseName"));
                    course.setTitle(rs.getString("Title"));
                    course.setDescription(rs.getString("Description"));
                    course.setLevelName(rs.getString("LevelName"));
                    course.setPrice(rs.getBigDecimal("Price"));
                    course.setImageURL(rs.getString("ImageURL"));
                    course.setInstructorID(rs.getInt("InstructorID"));
                    course.setCategoryID(rs.getInt("CategoryID"));
                    course.setSubcategoryID(rs.getInt("SubcategoryID"));
                    course.setLanguageID(rs.getInt("LanguageID"));
                    course.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    course.setUpdatedDate(rs.getTimestamp("UpdatedDate"));
                    course.setIsPublished(rs.getBoolean("IsPublished"));
                    course.setTotalEnrolled(rs.getInt("TotalEnrolled"));
                    course.setIsCancelled(rs.getBoolean("IsCancelled"));
                    System.out.println("DEBUG - Tìm thấy khóa học trong CSDL: " + course.getCourseID());
                    return course;
                } else {
                    System.out.println("DEBUG - Không tìm thấy khóa học với tên: " + courseName + " và instructorId: " + instructorId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting course by name and instructor: " + courseName + ", " + instructorId);
            e.printStackTrace();
        }
        return null;
    }

    public Course getCourseByTitleAndInstructor(String title, int instructorId) {
        String sql = "SELECT * FROM Courses WHERE CourseName = ? AND InstructorID = ?";
        System.out.println("DEBUG - Tìm khóa học với courseName: " + title + " và instructorId: " + instructorId);
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setInt(2, instructorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course();
                    course.setCourseID(rs.getInt("CourseID"));
                    course.setCourseName(rs.getString("CourseName"));
                    course.setTitle(rs.getString("Title"));
                    course.setDescription(rs.getString("Description"));
                    course.setLevelName(rs.getString("LevelName"));
                    course.setPrice(rs.getBigDecimal("Price"));
                    course.setImageURL(rs.getString("ImageURL"));
                    course.setInstructorID(rs.getInt("InstructorID"));
                    course.setCategoryID(rs.getInt("CategoryID"));
                    course.setSubcategoryID(rs.getInt("SubcategoryID"));
                    course.setLanguageID(rs.getInt("LanguageID"));
                    course.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    course.setUpdatedDate(rs.getTimestamp("UpdatedDate"));
                    course.setIsPublished(rs.getBoolean("IsPublished"));
                    course.setTotalEnrolled(rs.getInt("TotalEnrolled"));
                    course.setIsCancelled(rs.getBoolean("IsCancelled"));
                    System.out.println("DEBUG - Tìm thấy khóa học trong CSDL: " + course.getCourseID());
                    return course;
                } else {
                    System.out.println("DEBUG - Không tìm thấy khóa học với title: " + title + " và instructorId: " + instructorId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting course by title and instructor: " + title + ", " + instructorId);
            e.printStackTrace();
        }
        return null;
    }
}
