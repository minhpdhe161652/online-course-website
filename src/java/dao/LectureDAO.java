package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Lecture;
import util.DBUtil;

public class LectureDAO {
    private final DBUtil dbContext = new DBUtil();

    public Lecture getLectureBySectionID(int sectionID) {
        Lecture lecture = null;
        String sql = "SELECT LectureID, SectionID, LectureName, LectureURL, CreatedDate, LectureDescription "
                   + "FROM Lectures "
                   + "WHERE SectionID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sectionID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lecture = new Lecture();
                    lecture.setLectureID(rs.getInt("LectureID"));
                    lecture.setSectionID(rs.getInt("SectionID"));
                    lecture.setLectureName(rs.getString("LectureName"));
                    lecture.setLectureURL(rs.getString("LectureURL"));
                    lecture.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    lecture.setLectureDescription(rs.getString("LectureDescription"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecture;
    }

    public List<Lecture> getAllLecturesByCourseID(int courseID) {
        List<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT l.LectureID, l.SectionID, l.LectureName, l.LectureURL, l.CreatedDate, l.LectureDescription "
                   + "FROM Lectures l "
                   + "JOIN CourseSections cs ON l.SectionID = cs.SectionID "
                   + "WHERE cs.CourseID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Lecture lecture = new Lecture();
                    lecture.setLectureID(rs.getInt("LectureID"));
                    lecture.setSectionID(rs.getInt("SectionID"));
                    lecture.setLectureName(rs.getString("LectureName"));
                    lecture.setLectureURL(rs.getString("LectureURL"));
                    lecture.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    lecture.setLectureDescription(rs.getString("LectureDescription"));
                    lectures.add(lecture);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lectures;
    }

    public Lecture getNextLecture(int currentLectureID, int sectionID) {
        Lecture nextLecture = null;
        String sql = "SELECT LectureID, SectionID, LectureName, LectureURL, CreatedDate, LectureDescription "
                   + "FROM Lectures "
                   + "WHERE SectionID = ? AND LectureID > ? "
                   + "ORDER BY LectureID ASC LIMIT 1";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sectionID);
            stmt.setInt(2, currentLectureID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nextLecture = new Lecture();
                    nextLecture.setLectureID(rs.getInt("LectureID"));
                    nextLecture.setSectionID(rs.getInt("SectionID"));
                    nextLecture.setLectureName(rs.getString("LectureName"));
                    nextLecture.setLectureURL(rs.getString("LectureURL"));
                    nextLecture.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    nextLecture.setLectureDescription(rs.getString("LectureDescription"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextLecture;
    }
}
