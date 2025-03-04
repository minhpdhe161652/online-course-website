package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Quizzes;
import util.DBUtil;

public class QuizzesDAO {
    private final DBUtil dbContext = new DBUtil();

    public List<Quizzes> getQuizzesBySectionId(int sectionId) {
        List<Quizzes> quizzes = new ArrayList<>();
        String sql = "SELECT q.QuizID, q.QuizDescription, q.SectionID, q.QuizName, q.CreatedDate, q.Duration "
                   + "FROM Quizzes q "
                   + "WHERE q.SectionID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Quizzes quiz = new Quizzes();
                    quiz.setQuizID(rs.getInt("QuizID"));
                    quiz.setQuizDescription(rs.getString("QuizDescription"));
                    quiz.setSectionID(rs.getInt("SectionID"));
                    quiz.setQuizName(rs.getString("QuizName"));
                    quiz.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    quiz.setDuration(rs.getString("Duration"));
                    quizzes.add(quiz);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }
}
