package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.CourseSection;
import util.DBUtil;

public class CourseSectionDAO {
    private final DBUtil dbContext = new DBUtil();

    public List<CourseSection> getSectionsByCourseId(int courseId) {
        List<CourseSection> sections = new ArrayList<>();
        String sql = "SELECT * FROM CourseSections WHERE CourseID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CourseSection section = new CourseSection();
                    section.setSectionID(rs.getInt("SectionID"));
                    section.setCourseID(rs.getInt("CourseID"));
                    section.setSectionName(rs.getString("SectionName"));
                    section.setSectionDescription(rs.getString("SectionDescription"));
                    section.setSectionOrder(rs.getInt("SectionOrder"));
                    sections.add(section);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Debug log to check the number of sections retrieved
        System.out.println("Number of sections retrieved: " + sections.size());
        return sections;
    }
}
