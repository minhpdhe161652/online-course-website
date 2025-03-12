package controller;

import dao.QuizDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Quiz;
import model.QuizQuestion;
import java.io.IOException;

@WebServlet(name = "EditQuestionController", urlPatterns = {"/edit-question"})
public class EditQuestionController extends HttpServlet {
    private final QuizDAO quizDAO = new QuizDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int questionId = Integer.parseInt(request.getParameter("questionId"));
            int quizId = Integer.parseInt(request.getParameter("quizId"));
            
            QuizQuestion question = quizDAO.getQuestionById(questionId);
            Quiz quiz = quizDAO.getQuizById(quizId);
            
            if (question != null && quiz != null) {
                request.setAttribute("question", question);
                request.setAttribute("quiz", quiz);
                request.getRequestDispatcher("/views/quiz/edit-question.jsp").forward(request, response);
            } else {
                response.sendRedirect("quiz?action=edit&quizId=" + quizId);
            }
        } catch (Exception e) {
            System.err.println("Error in EditQuestionController doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        try {
            int questionId = Integer.parseInt(request.getParameter("questionId"));
            int quizId = Integer.parseInt(request.getParameter("quizId"));
            String questionText = request.getParameter("questionText");
            String option1 = request.getParameter("option1");
            String option2 = request.getParameter("option2");
            String option3 = request.getParameter("option3");
            String option4 = request.getParameter("option4");
            String correctAnswer = request.getParameter("correctAnswer");
            
            QuizQuestion question = new QuizQuestion();
            question.setQuestionID(questionId);
            question.setQuizID(quizId);
            question.setQuestionText(questionText);
            question.setOption1(option1);
            question.setOption2(option2);
            question.setOption3(option3);
            question.setOption4(option4);
            question.setCorrectAnswer(correctAnswer);
            
            boolean success = quizDAO.updateQuestion(question);
            
            if (success) {
                request.setAttribute("message", "Cập nhật câu hỏi thành công");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Có lỗi xảy ra khi cập nhật câu hỏi");
                request.setAttribute("messageType", "error");
            }
            
            // Chuyển hướng về trang quản lý bài kiểm tra
            response.sendRedirect("quiz?action=edit&quizId=" + quizId);
        } catch (Exception e) {
            System.err.println("Error in EditQuestionController doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
