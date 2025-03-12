package dao;

import model.Chapter;
import model.Lesson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.DBUtil;

public class ChapterDAO {
    private final DBUtil dbContext = new DBUtil();
    
    public int addChapter(Chapter chapter) {
        if (chapter == null) {
            System.err.println("Error: Chapter object is null");
            return -1;
        }
        
        if (chapter.getCourseID() <= 0) {
            System.err.println("Error: Invalid CourseID: " + chapter.getCourseID());
            return -1;
        }
        
        String sql = "INSERT INTO CourseSections (CourseID, SectionName, SectionDescription, SectionOrder, CreatedDate) " +
                    "VALUES (?, ?, ?, ?, GETDATE())";
                  
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            System.out.println("=== Debug addChapter ===");
            System.out.println("CourseID: " + chapter.getCourseID());
            System.out.println("SectionName: " + chapter.getChapterName());
            System.out.println("SectionDescription: " + chapter.getDescription());
            System.out.println("SectionOrder: " + chapter.getOrderIndex());
            
            // Validate connection
            if (conn == null) {
                System.err.println("Error: Database connection is null");
                return -1;
            }
            
            ps.setInt(1, chapter.getCourseID());
            ps.setString(2, chapter.getChapterName());
            ps.setString(3, chapter.getDescription());
            ps.setInt(4, chapter.getOrderIndex());
            
            int affectedRows = ps.executeUpdate();
            System.out.println("Affected rows: " + affectedRows);
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        System.out.println("Generated SectionID: " + generatedId);
                        return generatedId;
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("=== Error adding section ===");
            System.err.println("Section name: " + chapter.getChapterName());
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    public boolean addLesson(Lesson lesson) {
        if (lesson == null) {
            System.err.println("Error: Lesson object is null");
            return false;
        }
        
        String sql = "INSERT INTO Lectures (SectionID, LectureName, LectureDescription, LectureURL, CreatedDate) "
                  + "VALUES (?, ?, ?, ?, GETDATE())";
                  
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            System.out.println("=== Debug addLesson ===");
            System.out.println("SectionID: " + lesson.getChapterID());
            System.out.println("LectureName: " + lesson.getLessonName());
            System.out.println("Description: " + lesson.getDescription());
            System.out.println("URL: " + lesson.getVideoURL());
            
            // Validate connection
            if (conn == null) {
                System.err.println("Error: Database connection is null");
                return false;
            }
            
            ps.setInt(1, lesson.getChapterID());
            ps.setString(2, lesson.getLessonName());
            ps.setString(3, lesson.getDescription() != null ? lesson.getDescription() : "");
            
            // Handle null or empty URL
            String videoUrl = lesson.getVideoURL();
            if (videoUrl == null || videoUrl.trim().isEmpty()) {
                ps.setNull(4, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(4, videoUrl.trim());
            }
            
            int rowsAffected = ps.executeUpdate();
            System.out.println("Affected rows: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("=== Error adding lecture ===");
            System.err.println("Lecture name: " + lesson.getLessonName());
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Chapter> getChaptersByCourse(int courseID) {
        List<Chapter> chapters = new ArrayList<>();
        String sql = "SELECT * FROM CourseSections WHERE CourseID = ? ORDER BY SectionOrder";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, courseID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Chapter chapter = new Chapter();
                chapter.setChapterID(rs.getInt("SectionID"));
                chapter.setCourseID(rs.getInt("CourseID"));
                chapter.setChapterName(rs.getString("SectionName"));
                chapter.setDescription(rs.getString("SectionDescription"));
                chapter.setOrderIndex(rs.getInt("SectionOrder"));
                
                // Lấy danh sách bài giảng của chương
                chapter.setLessons(getLessonsByChapter(chapter.getChapterID()));
                chapters.add(chapter);
            }
            
        } catch (Exception e) {
            System.err.println("Error getting sections for course: " + courseID);
            e.printStackTrace();
        }
        return chapters;
    }
    
    private List<Lesson> getLessonsByChapter(int sectionID) {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT * FROM Lectures WHERE SectionID = ? ORDER BY LectureID";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sectionID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Lesson lesson = new Lesson();
                lesson.setLessonID(rs.getInt("LectureID"));
                lesson.setChapterID(rs.getInt("SectionID"));
                lesson.setLessonName(rs.getString("LectureName"));
                lesson.setDescription(rs.getString("LectureDescription"));
                lesson.setVideoURL(rs.getString("LectureURL"));
                lesson.setOrderIndex(0); // Không còn sử dụng OrderIndex
                lessons.add(lesson);
            }
            
        } catch (Exception e) {
            System.err.println("Error getting lectures for section: " + sectionID);
            e.printStackTrace();
        }
        return lessons;
    }
    
    public int getChapterCount(int courseID) {
        String sql = "SELECT COUNT(*) as Count FROM CourseSections WHERE CourseID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, courseID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("Count");
            }
            
        } catch (Exception e) {
            System.err.println("Error counting sections for course: " + courseID);
            e.printStackTrace();
        }
        return 0;
    }
    
    public Lesson getLessonById(int lessonId) {
        String sql = "SELECT * FROM Lectures WHERE LectureID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, lessonId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Lesson lesson = new Lesson();
                    lesson.setLessonID(rs.getInt("LectureID"));
                    lesson.setChapterID(rs.getInt("SectionID"));
                    lesson.setLessonName(rs.getString("LectureName"));
                    lesson.setDescription(rs.getString("LectureDescription"));
                    lesson.setVideoURL(rs.getString("LectureURL"));
                    return lesson;
                }
            }
        } catch (Exception e) {
            System.err.println("Error in getLessonById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateLesson(Lesson lesson) {
        String sql = "UPDATE Lectures SET LectureName = ?, LectureDescription = ?, LectureURL = ? WHERE LectureID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, lesson.getLessonName());
            stmt.setString(2, lesson.getDescription());
            stmt.setString(3, lesson.getVideoURL());
            stmt.setInt(4, lesson.getLessonID());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("Error in updateLesson: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteLesson(int lessonId) {
        String sql = "DELETE FROM Lectures WHERE LectureID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, lessonId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("Error in deleteLesson: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Chapter getChapterById(int chapterId) {
        String sql = "SELECT * FROM CourseSections WHERE SectionID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, chapterId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Chapter chapter = new Chapter();
                    chapter.setChapterID(rs.getInt("SectionID"));
                    chapter.setCourseID(rs.getInt("CourseID"));
                    chapter.setChapterName(rs.getString("SectionName"));
                    chapter.setDescription(rs.getString("SectionDescription"));
                    chapter.setOrderIndex(rs.getInt("SectionOrder"));
                    return chapter;
                }
            }
        } catch (Exception e) {
            System.err.println("Error in getChapterById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateChapter(Chapter chapter) {
        String sql = "UPDATE CourseSections SET SectionName = ?, SectionDescription = ? WHERE SectionID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, chapter.getChapterName());
            stmt.setString(2, chapter.getDescription());
            stmt.setInt(3, chapter.getChapterID());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("Error in updateChapter: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteChapter(int chapterId) {
        // Bắt đầu transaction để đảm bảo tính nhất quán
        Connection conn = null;
        try {
            conn = dbContext.getConnection();
            conn.setAutoCommit(false);
            
            // Xóa tất cả bài giảng trong chương
            String deleteLessonsSQL = "DELETE FROM Lectures WHERE SectionID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteLessonsSQL)) {
                stmt.setInt(1, chapterId);
                stmt.executeUpdate();
            }
            
            // Xóa chương
            String deleteChapterSQL = "DELETE FROM CourseSections WHERE SectionID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteChapterSQL)) {
                stmt.setInt(1, chapterId);
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error in deleteChapter: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public boolean updateChapterOrder(List<Integer> chapterIds) {
        String sql = "UPDATE CourseSections SET SectionOrder = ? WHERE SectionID = ?";
        Connection conn = null;
        try {
            conn = dbContext.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < chapterIds.size(); i++) {
                    stmt.setInt(1, i + 1); // Thứ tự bắt đầu từ 1
                    stmt.setInt(2, chapterIds.get(i));
                    stmt.addBatch();
                }
                
                int[] results = stmt.executeBatch();
                boolean success = true;
                for (int result : results) {
                    if (result <= 0) {
                        success = false;
                        break;
                    }
                }
                
                if (success) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error in updateChapterOrder: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public boolean updateChapterOrder(int chapterId, int newOrder) {
        String sql = "UPDATE CourseSections SET SectionOrder = ? WHERE SectionID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, newOrder);
            ps.setInt(2, chapterId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("Error in updateChapterOrder: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả các chương và bài giảng liên quan đến một khóa học
     * @param courseID ID của khóa học cần xóa chương
     * @return true nếu xóa thành công, false nếu có lỗi
     */
    public boolean deleteChaptersByCourse(int courseID) {
        if (courseID <= 0) {
            System.err.println("Error: Invalid courseID " + courseID);
            return false;
        }
        
        // Xóa tất cả các bài giảng thuộc các chương của khóa học
        String deleteLecturesSQL = "DELETE FROM Lectures WHERE SectionID IN " +
                                  "(SELECT ChapterID FROM Chapters WHERE CourseID = ?)"; 
        
        // Xóa tất cả các chương thuộc khóa học
        String deleteChaptersSQL = "DELETE FROM Chapters WHERE CourseID = ?";
        
        Connection conn = null;
        try {
            conn = dbContext.getConnection();
            
            // Bắt đầu transaction
            conn.setAutoCommit(false);
            
            // Xóa các bài giảng trước
            try (PreparedStatement ps = conn.prepareStatement(deleteLecturesSQL)) {
                ps.setInt(1, courseID);
                int lecturesDeleted = ps.executeUpdate();
                System.out.println("Đã xóa " + lecturesDeleted + " bài giảng thuộc khóa học ID " + courseID);
            }
            
            // Sau đó xóa các chương
            try (PreparedStatement ps = conn.prepareStatement(deleteChaptersSQL)) {
                ps.setInt(1, courseID);
                int chaptersDeleted = ps.executeUpdate();
                System.out.println("Đã xóa " + chaptersDeleted + " chương thuộc khóa học ID " + courseID);
            }
            
            // Commit transaction
            conn.commit();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error deleting chapters for course " + courseID + ": " + e.getMessage());
            e.printStackTrace();
            
            // Rollback transaction nếu có lỗi
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // Đóng kết nối và reset autocommit
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
