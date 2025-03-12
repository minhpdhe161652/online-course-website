package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Quiz;
import model.QuizQuestion;
import model.QuizAnswer;
import model.QuizSubmission;
import utils.DBContext;

public class QuizDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
    public Quiz getQuizById(int quizId) throws SQLException {
        try {
            String query = "SELECT * FROM Quizzes WHERE QuizID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, quizId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizID(rs.getInt("QuizID"));
                quiz.setTitle(rs.getString("Title"));
                quiz.setQuizDescription(rs.getString("QuizDescription"));
                quiz.setCourseID(rs.getInt("CourseID"));
                quiz.setCreatedDate(rs.getTimestamp("CreatedDate"));
                quiz.setDuration(rs.getString("Duration"));
                quiz.setTimeLimit(rs.getInt("TimeLimit"));
                quiz.setPassingScore(rs.getInt("PassingScore"));
                quiz.setQuestions(getQuestionsByQuizId(quizId));
                return quiz;
            }
        } finally {
            closeResources();
        }
        return null;
    }
    
    public List<QuizQuestion> getQuestionsByQuizId(int quizId) throws SQLException {
        List<QuizQuestion> questions = new ArrayList<>();
        try {
            String query = "SELECT * FROM QuizQuestions WHERE QuizID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, quizId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                QuizQuestion question = new QuizQuestion();
                question.setQuestionID(rs.getInt("QuestionID"));
                question.setQuizID(rs.getInt("QuizID"));
                question.setQuestionText(rs.getString("QuestionText"));
                question.setQuestionType(rs.getString("QuestionType"));
                question.setOption1(rs.getString("Option1"));
                question.setOption2(rs.getString("Option2"));
                question.setOption3(rs.getString("Option3"));
                question.setOption4(rs.getString("Option4"));
                question.setCorrectAnswer(rs.getString("CorrectAnswer"));
                questions.add(question);
            }
        } finally {
            closeResources();
        }
        return questions;
    }
    
    public List<QuizAnswer> getAnswersByQuestionId(int questionId) throws SQLException {
        List<QuizAnswer> answers = new ArrayList<>();
        try {
            String query = "SELECT * FROM QuizQuestions WHERE QuestionID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, questionId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                // Tạo các đối tượng QuizAnswer từ các trường Option1-4
                String correctAnswer = rs.getString("CorrectAnswer");
                
                // Option1
                if (rs.getString("Option1") != null && !rs.getString("Option1").isEmpty()) {
                    QuizAnswer answer = new QuizAnswer();
                    answer.setAnswerID(1); // ID tương ứng với vị trí
                    answer.setQuestionID(questionId);
                    answer.setAnswerText(rs.getString("Option1"));
                    answer.setIsCorrect("1".equals(correctAnswer));
                    answers.add(answer);
                }
                
                // Option2
                if (rs.getString("Option2") != null && !rs.getString("Option2").isEmpty()) {
                    QuizAnswer answer = new QuizAnswer();
                    answer.setAnswerID(2);
                    answer.setQuestionID(questionId);
                    answer.setAnswerText(rs.getString("Option2"));
                    answer.setIsCorrect("2".equals(correctAnswer));
                    answers.add(answer);
                }
                
                // Option3
                if (rs.getString("Option3") != null && !rs.getString("Option3").isEmpty()) {
                    QuizAnswer answer = new QuizAnswer();
                    answer.setAnswerID(3);
                    answer.setQuestionID(questionId);
                    answer.setAnswerText(rs.getString("Option3"));
                    answer.setIsCorrect("3".equals(correctAnswer));
                    answers.add(answer);
                }
                
                // Option4
                if (rs.getString("Option4") != null && !rs.getString("Option4").isEmpty()) {
                    QuizAnswer answer = new QuizAnswer();
                    answer.setAnswerID(4);
                    answer.setQuestionID(questionId);
                    answer.setAnswerText(rs.getString("Option4"));
                    answer.setIsCorrect("4".equals(correctAnswer));
                    answers.add(answer);
                }
            }
        } finally {
            closeResources();
        }
        return answers;
    }
    
    public QuizSubmission submitQuiz(int studentId, int quizId, List<Integer> selectedAnswers, List<Integer> questionIds) throws SQLException {
        QuizSubmission submission = null;
        conn = new DBContext().getConnection();
        try {
            conn.setAutoCommit(false);
            
            // Create submission
            String insertSubmission = "INSERT INTO QuizSubmissions (StudentID, QuizID, SubmissionDate) VALUES (?, ?, GETDATE())";
            ps = conn.prepareStatement(insertSubmission, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, studentId);
            ps.setInt(2, quizId);
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            int submissionId = 0;
            if (rs.next()) {
                submissionId = rs.getInt(1);
            }
            
            // Save answers
            String insertDetails = "INSERT INTO QuizSubmissionDetails (QuizSubmissionID, QuestionID, AnswerID) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(insertDetails);
            
            for (int i = 0; i < selectedAnswers.size(); i++) {
                ps.setInt(1, submissionId);
                ps.setInt(2, questionIds.get(i)); 
                ps.setInt(3, selectedAnswers.get(i));
                ps.addBatch();
            }
            ps.executeBatch();
            
            conn.commit();
            
            // Get submission details
            submission = getSubmissionById(submissionId);
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw ex;
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
            closeResources();
        }
        return submission;
    }
    
    public QuizSubmission getSubmissionById(int submissionId) throws SQLException {
        QuizSubmission submission = null;
        try {
            String query = "SELECT * FROM QuizSubmissions WHERE SubmissionID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, submissionId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                submission = new QuizSubmission();
                submission.setSubmissionID(rs.getInt("SubmissionID"));
                submission.setStudentID(rs.getInt("StudentID"));
                submission.setQuizID(rs.getInt("QuizID"));
                submission.setSubmissionDate(rs.getTimestamp("SubmissionDate"));
                submission.setTimeSpent(rs.getString("TimeSpent"));
                
                // Get submission details
                List<Integer> selectedAnswers = new ArrayList<>();
                String detailsQuery = "SELECT * FROM QuizSubmissionDetails WHERE QuizSubmissionID = ? ORDER BY QuestionID";
                ps = conn.prepareStatement(detailsQuery);
                ps.setInt(1, submissionId);
                ResultSet detailsRs = ps.executeQuery();
                
                while (detailsRs.next()) {
                    selectedAnswers.add(detailsRs.getInt("AnswerID"));
                }
                submission.setSelectedAnswers(selectedAnswers);
            }
        } finally {
            closeResources();
        }
        return submission;
    }
    
    public int calculateScore(QuizSubmission submission) throws SQLException {
        int score = 0;
        try {
            String query = """
                SELECT COUNT(*) as CorrectCount
                FROM QuizSubmissionDetails sd
                JOIN QuizQuestions q ON sd.QuestionID = q.QuestionID
                WHERE sd.QuizSubmissionID = ? 
                AND CAST(sd.AnswerID AS VARCHAR) = q.CorrectAnswer
            """;
            
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, submission.getSubmissionID());
            rs = ps.executeQuery();
            
            if (rs.next()) {
                score = rs.getInt("CorrectCount");
            }
        } finally {
            closeResources();
        }
        return score;
    }
    
    public Quiz getQuizByCourseId(int courseId) throws SQLException {
        try {
            String query = "SELECT * FROM Quizzes WHERE CourseID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, courseId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizID(rs.getInt("QuizID"));
                quiz.setTitle(rs.getString("Title"));
                quiz.setQuizDescription(rs.getString("QuizDescription"));
                quiz.setCourseID(rs.getInt("CourseID"));
                quiz.setCreatedDate(rs.getTimestamp("CreatedDate"));
                quiz.setDuration(rs.getString("Duration"));
                quiz.setTimeLimit(rs.getInt("TimeLimit"));
                quiz.setPassingScore(rs.getInt("PassingScore"));
                quiz.setQuestions(getQuestionsByQuizId(quiz.getQuizID()));
                return quiz;
            }
        } finally {
            closeResources();
        }
        return null;
    }
    
    public int createQuiz(Quiz quiz) throws SQLException {
        int quizId = 0;
        try {
            String query = "INSERT INTO Quizzes (Title, QuizDescription, CourseID, Duration, TimeLimit, PassingScore, CreatedDate) VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, quiz.getTitle());
            ps.setString(2, quiz.getQuizDescription());
            ps.setInt(3, quiz.getCourseID()); 
            ps.setString(4, quiz.getDuration());
            ps.setInt(5, quiz.getTimeLimit());
            ps.setInt(6, quiz.getPassingScore());
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                quizId = rs.getInt(1);
            }
        } finally {
            closeResources();
        }
        return quizId;
    }
    
    public boolean updateQuiz(Quiz quiz) throws SQLException {
        try {
            String query = "UPDATE Quizzes SET Title = ?, QuizDescription = ?, CourseID = ?, Duration = ?, TimeLimit = ?, PassingScore = ? WHERE QuizID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, quiz.getTitle());
            ps.setString(2, quiz.getQuizDescription());
            ps.setInt(3, quiz.getCourseID());
            ps.setString(4, quiz.getDuration());
            ps.setInt(5, quiz.getTimeLimit());
            ps.setInt(6, quiz.getPassingScore());
            ps.setInt(7, quiz.getQuizID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } finally {
            closeResources();
        }
    }
    
    public boolean deleteQuiz(int quizId) throws SQLException {
        try {
            // Xóa các chi tiết nộp bài liên quan đến quiz
            String deleteSubmissionDetailsQuery = "DELETE sd FROM QuizSubmissionDetails sd JOIN QuizSubmissions s ON sd.QuizSubmissionID = s.SubmissionID WHERE s.QuizID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(deleteSubmissionDetailsQuery);
            ps.setInt(1, quizId);
            ps.executeUpdate();
            
            // Xóa các bài nộp liên quan đến quiz
            String deleteSubmissionsQuery = "DELETE FROM QuizSubmissions WHERE QuizID = ?";
            ps = conn.prepareStatement(deleteSubmissionsQuery);
            ps.setInt(1, quizId);
            ps.executeUpdate();
            
            // Xóa các câu hỏi liên quan đến quiz
            String deleteQuestionsQuery = "DELETE FROM QuizQuestions WHERE QuizID = ?";
            ps = conn.prepareStatement(deleteQuestionsQuery);
            ps.setInt(1, quizId);
            ps.executeUpdate();
            
            // Xóa quiz
            String deleteQuizQuery = "DELETE FROM Quizzes WHERE QuizID = ?";
            ps = conn.prepareStatement(deleteQuizQuery);
            ps.setInt(1, quizId);
            int rowsAffected = ps.executeUpdate();
            
            return rowsAffected > 0;
        } finally {
            closeResources();
        }
    }
    
    public QuizQuestion getQuestionById(int questionId) throws SQLException {
        try {
            String query = "SELECT * FROM QuizQuestions WHERE QuestionID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, questionId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                QuizQuestion question = new QuizQuestion();
                question.setQuestionID(rs.getInt("QuestionID"));
                question.setQuizID(rs.getInt("QuizID"));
                question.setQuestionText(rs.getString("QuestionText"));
                question.setQuestionType(rs.getString("QuestionType"));
                question.setOption1(rs.getString("Option1"));
                question.setOption2(rs.getString("Option2"));
                question.setOption3(rs.getString("Option3"));
                question.setOption4(rs.getString("Option4"));
                question.setCorrectAnswer(rs.getString("CorrectAnswer"));
                return question;
            }
        } finally {
            closeResources();
        }
        return null;
    }
    
    public boolean createQuestion(QuizQuestion question) throws SQLException {
        try {
            // Thêm câu hỏi
            String query = "INSERT INTO QuizQuestions (QuizID, QuestionText, QuestionType, Option1, Option2, Option3, Option4, CorrectAnswer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, question.getQuizID());
            ps.setString(2, question.getQuestionText());
            ps.setString(3, question.getQuestionType() != null ? question.getQuestionType() : "multiple-choice");
            ps.setString(4, question.getOption1());
            ps.setString(5, question.getOption2());
            ps.setString(6, question.getOption3());
            ps.setString(7, question.getOption4());
            ps.setString(8, question.getCorrectAnswer());
            int rowsAffected = ps.executeUpdate();
            
            return rowsAffected > 0;
        } finally {
            closeResources();
        }
    }
    
    public boolean updateQuestion(QuizQuestion question) throws SQLException {
        try {
            // Cập nhật câu hỏi
            String query = "UPDATE QuizQuestions SET QuestionText = ?, QuestionType = ?, Option1 = ?, Option2 = ?, Option3 = ?, Option4 = ?, CorrectAnswer = ? WHERE QuestionID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, question.getQuestionText());
            ps.setString(2, question.getQuestionType());
            ps.setString(3, question.getOption1());
            ps.setString(4, question.getOption2());
            ps.setString(5, question.getOption3());
            ps.setString(6, question.getOption4());
            ps.setString(7, question.getCorrectAnswer());
            ps.setInt(8, question.getQuestionID());
            int rowsAffected = ps.executeUpdate();
            
            return rowsAffected > 0;
        } finally {
            closeResources();
        }
    }
    
    public boolean deleteQuestion(int questionId) throws SQLException {
        try {
            // Xóa câu hỏi
            String deleteQuestionQuery = "DELETE FROM QuizQuestions WHERE QuestionID = ?";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(deleteQuestionQuery);
            ps.setInt(1, questionId);
            int rowsAffected = ps.executeUpdate();
            
            return rowsAffected > 0;
        } finally {
            closeResources();
        }
    }
    
    public boolean saveQuizResult(int userId, int quizId, int score) throws SQLException {
        try {
            String query = "INSERT INTO QuizSubmissions (StudentID, QuizID, SubmissionDate) VALUES (?, ?, GETDATE())";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, quizId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } finally {
            closeResources();
        }
    }
    
    /**
     * Xóa tất cả các quiz và câu hỏi liên quan đến khóa học
     * @param courseID ID của khóa học cần xóa quiz
     * @return true nếu xóa thành công, false nếu có lỗi
     */
    public boolean deleteQuizzesByCourse(int courseID) {
        if (courseID <= 0) {
            System.err.println("Error: Invalid courseID " + courseID);
            return false;
        }
        
        // Xóa tất cả các câu hỏi thuộc các quiz của khóa học
        String deleteQuestionsSQL = "DELETE FROM QuizQuestions WHERE QuizID IN " +
                                  "(SELECT QuizID FROM Quizzes WHERE CourseID = ?)"; 
        
        // Xóa tất cả các quiz thuộc khóa học
        String deleteQuizzesSQL = "DELETE FROM Quizzes WHERE CourseID = ?";
        
        Connection conn = null;
        try {
            conn = new DBContext().getConnection();
            
            // Bắt đầu transaction
            conn.setAutoCommit(false);
            
            // Xóa các câu hỏi trước
            try (PreparedStatement ps = conn.prepareStatement(deleteQuestionsSQL)) {
                ps.setInt(1, courseID);
                int questionsDeleted = ps.executeUpdate();
                System.out.println("Đã xóa " + questionsDeleted + " câu hỏi thuộc khóa học ID " + courseID);
            }
            
            // Sau đó xóa các quiz
            try (PreparedStatement ps = conn.prepareStatement(deleteQuizzesSQL)) {
                ps.setInt(1, courseID);
                int quizzesDeleted = ps.executeUpdate();
                System.out.println("Đã xóa " + quizzesDeleted + " quiz thuộc khóa học ID " + courseID);
            }
            
            // Commit transaction
            conn.commit();
            return true;
            
        } catch (Exception e) {
            System.err.println("Error deleting quizzes for course " + courseID + ": " + e.getMessage());
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
    
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            // Log error
        }
    }
}
