package controller;

import dao.QuizDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Course;
import model.Quiz;
import model.QuizQuestion;
import model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "QuizController", urlPatterns = {"/quiz"})
public class QuizController extends HttpServlet {
    private final QuizDAO quizDAO = new QuizDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }
        
        try {
            switch (action) {
                case "create":
                    showCreateForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    handleDelete(request, response);
                    break;
                case "take-quiz":
                    showTakeQuizForm(request, response);
                    break;
                case "view":
                default:
                    viewQuiz(request, response);
                    break;
            }
        } catch (NumberFormatException e) {
            setErrorMessage(request, "ID không hợp lệ");
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            System.err.println("Error in QuizController doGet: " + e.getMessage());
            e.printStackTrace();
            setErrorMessage(request, "Đã xảy ra lỗi khi xử lý yêu cầu");
            response.sendRedirect("error.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "create":
                    handleCreate(request, response);
                    break;
                case "edit":
                    handleEdit(request, response);
                    break;
                case "add-question":
                    handleAddQuestion(request, response);
                    break;
                case "submit-quiz":
                    handleQuizSubmission(request, response);
                    break;
                default:
                    response.sendRedirect("quiz");
                    break;
            }
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Dữ liệu nhập vào không hợp lệ");
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            System.err.println("Error in QuizController doPost: " + e.getMessage());
            e.printStackTrace();
            setErrorMessage(request, "Đã xảy ra lỗi khi xử lý yêu cầu");
            response.sendRedirect("error.jsp");
        }
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String courseIdStr = request.getParameter("courseId");
        if (courseIdStr == null || courseIdStr.trim().isEmpty()) {
            setErrorMessage(request, "Không tìm thấy khóa học");
            response.sendRedirect("add-course");
            return;
        }
        
        try {
            int courseId = Integer.parseInt(courseIdStr);
            
            // Nếu courseId là số âm, đây là courseId tạm thời từ quá trình tạo khóa học mới
            if (courseId < 0) {
                // Lấy đối tượng course từ session
                HttpSession session = request.getSession();
                Course currentCourse = (Course) session.getAttribute("currentCourse");
                
                if (currentCourse != null && currentCourse.getCourseID() == courseId) {
                    // Đảm bảo courseId trong session và parameter là giống nhau
                    request.setAttribute("courseId", courseId);
                    request.getRequestDispatcher("/views/quiz/quiz-create-basic.jsp").forward(request, response);
                    return;
                }
            }
            
            // Xử lý bình thường nếu không phải courseId tạm thời
            request.setAttribute("courseId", courseId);
            request.getRequestDispatcher("/views/quiz/quiz-create-basic.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            setErrorMessage(request, "ID khóa học không hợp lệ");
            response.sendRedirect("add-course");
        }
    }
    
    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Validate input
        List<String> errors = validateQuizInput(request);
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            showCreateForm(request, response);
            return;
        }
        
        try {
            // Parse and create quiz
            Quiz quiz = parseQuizFromRequest(request);
            
            // Kiểm tra nếu courseId là số âm (tạm thời)
            if (quiz.getCourseID() < 0) {
                // Lấy đối tượng course từ session để xác nhận courseId hợp lệ
                HttpSession session = request.getSession();
                Course currentCourse = (Course) session.getAttribute("currentCourse");
                
                if (currentCourse == null || currentCourse.getCourseID() != quiz.getCourseID()) {
                    setErrorMessage(request, "Không tìm thấy thông tin khóa học");
                    response.sendRedirect("add-course");
                    return;
                }
                
                // Lưu quiz vào session để sử dụng sau này
                session.setAttribute("currentQuiz", quiz);
            }
            
            int quizId = quizDAO.createQuiz(quiz);
            
            if (quizId > 0) {
                setSuccessMessage(request, "Tạo bài kiểm tra thành công");
                
                // Nếu là courseId tạm thời, chuyển hướng đến trang add-course-details
                if (quiz.getCourseID() < 0) {
                    response.sendRedirect("add-course?step=2&action=add-quiz");
                } else {
                    response.sendRedirect("quiz?action=edit&quizId=" + quizId);
                }
            } else {
                setErrorMessage(request, "Có lỗi xảy ra khi tạo bài kiểm tra");
                showCreateForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setErrorMessage(request, "Lỗi: " + e.getMessage());
            showCreateForm(request, response);
        }
    }
    
    private List<String> validateQuizInput(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String timeLimitStr = request.getParameter("timeLimit");
        String passingScoreStr = request.getParameter("passingScore");
        
        if (title == null || title.trim().isEmpty()) {
            errors.add("Tiêu đề không được để trống");
        } else if (title.length() > 100) {
            errors.add("Tiêu đề không được quá 100 ký tự");
        }
        
        if (description == null || description.trim().isEmpty()) {
            errors.add("Mô tả không được để trống");
        }
        
        try {
            int timeLimit = Integer.parseInt(timeLimitStr);
            if (timeLimit < 1 || timeLimit > 180) {
                errors.add("Thời gian làm bài phải từ 1 đến 180 phút");
            }
        } catch (NumberFormatException e) {
            errors.add("Thời gian làm bài không hợp lệ");
        }
        
        try {
            int passingScore = Integer.parseInt(passingScoreStr);
            if (passingScore < 0 || passingScore > 100) {
                errors.add("Điểm đạt phải từ 0 đến 100");
            }
        } catch (NumberFormatException e) {
            errors.add("Điểm đạt không hợp lệ");
        }
        
        return errors;
    }
    
    private Quiz parseQuizFromRequest(HttpServletRequest request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getParameter("title").trim());
        quiz.setDescription(request.getParameter("description").trim());
        quiz.setTimeLimit(Integer.parseInt(request.getParameter("timeLimit")));
        quiz.setPassingScore(Integer.parseInt(request.getParameter("passingScore")));
        quiz.setCourseID(Integer.parseInt(request.getParameter("courseId")));
        return quiz;
    }
    
    private void setErrorMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("messageType", "error");
    }
    
    private void setSuccessMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("messageType", "success");
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int quizId = Integer.parseInt(request.getParameter("quizId"));
        Quiz quiz = quizDAO.getQuizById(quizId);
        
        if (quiz != null) {
            request.setAttribute("quiz", quiz);
            request.getRequestDispatcher("/views/quiz/quiz-manage-questions.jsp").forward(request, response);
        } else {
            response.sendRedirect("quiz");
        }
    }
    
    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy thông tin quiz từ form
            int quizId = Integer.parseInt(request.getParameter("quizId"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            int timeLimit = Integer.parseInt(request.getParameter("timeLimit"));
            int passingScore = Integer.parseInt(request.getParameter("passingScore"));
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            
            // Cập nhật quiz
            Quiz quiz = new Quiz();
            quiz.setQuizID(quizId);
            quiz.setTitle(title);
            quiz.setDescription(description);
            quiz.setTimeLimit(timeLimit);
            quiz.setPassingScore(passingScore);
            quiz.setCourseID(courseId);
            
            boolean success = quizDAO.updateQuiz(quiz);
            
            // Lấy quiz đã cập nhật từ database
            quiz = quizDAO.getQuizById(quizId);
            request.setAttribute("quiz", quiz);
            
            if (success) {
                setSuccessMessage(request, "Cập nhật bài kiểm tra thành công");
            } else {
                setErrorMessage(request, "Có lỗi xảy ra khi cập nhật bài kiểm tra");
            }
        } catch (SQLException e) {
            setErrorMessage(request, "Lỗi cơ sở dữ liệu: " + e.getMessage());
            System.err.println("SQL Error in handleEdit: " + e.getMessage());
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Lỗi định dạng dữ liệu: " + e.getMessage());
            System.err.println("Format Error in handleEdit: " + e.getMessage());
        } catch (Exception e) {
            setErrorMessage(request, "Có lỗi xảy ra: " + e.getMessage());
            System.err.println("Error in handleEdit: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/views/quiz/quiz-manage-questions.jsp").forward(request, response);
    }
    
    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int quizId = Integer.parseInt(request.getParameter("quizId"));
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            
            boolean success = quizDAO.deleteQuiz(quizId);
            
            if (success) {
                setSuccessMessage(request, "Xóa bài kiểm tra thành công");
            } else {
                setErrorMessage(request, "Có lỗi xảy ra khi xóa bài kiểm tra");
            }
            
            response.sendRedirect("course-details?courseId=" + courseId);
        } catch (SQLException e) {
            setErrorMessage(request, "Lỗi cơ sở dữ liệu: " + e.getMessage());
            System.err.println("SQL Error in handleDelete: " + e.getMessage());
            response.sendRedirect(request.getHeader("Referer"));
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Lỗi định dạng dữ liệu: " + e.getMessage());
            System.err.println("Format Error in handleDelete: " + e.getMessage());
            response.sendRedirect(request.getHeader("Referer"));
        } catch (Exception e) {
            setErrorMessage(request, "Có lỗi xảy ra: " + e.getMessage());
            System.err.println("Error in handleDelete: " + e.getMessage());
            response.sendRedirect(request.getHeader("Referer"));
        }
    }
    
    private void handleAddQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int quizId = Integer.parseInt(request.getParameter("quizId"));
            String questionText = request.getParameter("questionText");
            String option1 = request.getParameter("option1");
            String option2 = request.getParameter("option2");
            String option3 = request.getParameter("option3");
            String option4 = request.getParameter("option4");
            String correctAnswer = request.getParameter("correctAnswer");
            
            QuizQuestion question = new QuizQuestion();
            question.setQuizID(quizId);
            question.setQuestionText(questionText);
            question.setOption1(option1);
            question.setOption2(option2);
            question.setOption3(option3);
            question.setOption4(option4);
            question.setCorrectAnswer(correctAnswer);
            
            boolean success = quizDAO.createQuestion(question);
            
            // Lấy quiz đã cập nhật từ database
            Quiz quiz = quizDAO.getQuizById(quizId);
            request.setAttribute("quiz", quiz);
            
            if (success) {
                setSuccessMessage(request, "Thêm câu hỏi thành công");
            } else {
                setErrorMessage(request, "Có lỗi xảy ra khi thêm câu hỏi");
            }
        } catch (SQLException e) {
            setErrorMessage(request, "Lỗi cơ sở dữ liệu: " + e.getMessage());
            System.err.println("SQL Error in handleAddQuestion: " + e.getMessage());
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Lỗi định dạng dữ liệu: " + e.getMessage());
            System.err.println("Format Error in handleAddQuestion: " + e.getMessage());
        } catch (Exception e) {
            setErrorMessage(request, "Có lỗi xảy ra: " + e.getMessage());
            System.err.println("Error in handleAddQuestion: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/views/quiz/quiz-manage-questions.jsp").forward(request, response);
    }
    
    private void handleEditQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            
            // Lấy quiz đã cập nhật từ database
            Quiz quiz = quizDAO.getQuizById(quizId);
            request.setAttribute("quiz", quiz);
            
            if (success) {
                setSuccessMessage(request, "Cập nhật câu hỏi thành công");
            } else {
                setErrorMessage(request, "Có lỗi xảy ra khi cập nhật câu hỏi");
            }
        } catch (SQLException e) {
            setErrorMessage(request, "Lỗi cơ sở dữ liệu: " + e.getMessage());
            System.err.println("SQL Error in handleEditQuestion: " + e.getMessage());
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Lỗi định dạng dữ liệu: " + e.getMessage());
            System.err.println("Format Error in handleEditQuestion: " + e.getMessage());
        } catch (Exception e) {
            setErrorMessage(request, "Có lỗi xảy ra: " + e.getMessage());
            System.err.println("Error in handleEditQuestion: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/views/quiz/quiz-manage-questions.jsp").forward(request, response);
    }
    
    private void handleDeleteQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int questionId = Integer.parseInt(request.getParameter("questionId"));
            int quizId = Integer.parseInt(request.getParameter("quizId"));
            
            boolean success = quizDAO.deleteQuestion(questionId);
            
            // Lấy quiz đã cập nhật từ database
            Quiz quiz = quizDAO.getQuizById(quizId);
            request.setAttribute("quiz", quiz);
            
            if (success) {
                setSuccessMessage(request, "Xóa câu hỏi thành công");
            } else {
                setErrorMessage(request, "Có lỗi xảy ra khi xóa câu hỏi");
            }
        } catch (SQLException e) {
            setErrorMessage(request, "Lỗi cơ sở dữ liệu: " + e.getMessage());
            System.err.println("SQL Error in handleDeleteQuestion: " + e.getMessage());
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Lỗi định dạng dữ liệu: " + e.getMessage());
            System.err.println("Format Error in handleDeleteQuestion: " + e.getMessage());
        } catch (Exception e) {
            setErrorMessage(request, "Có lỗi xảy ra: " + e.getMessage());
            System.err.println("Error in handleDeleteQuestion: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/views/quiz/quiz-manage-questions.jsp").forward(request, response);
    }
    
    private void showTakeQuizForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int quizId = Integer.parseInt(request.getParameter("quizId"));
        Quiz quiz = quizDAO.getQuizById(quizId);
        
        if (quiz != null) {
            request.setAttribute("quiz", quiz);
            request.getRequestDispatcher("/views/quiz/take-quiz.jsp").forward(request, response);
        } else {
            response.sendRedirect("quiz");
        }
    }
    
    private void handleQuizSubmission(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int quizId = Integer.parseInt(request.getParameter("quizId"));
        Quiz quiz = quizDAO.getQuizById(quizId);
        
        if (quiz != null) {
            // Xử lý bài làm
            int score = 0;
            int totalQuestions = quiz.getQuestions().size();
            List<Integer> selectedAnswers = new ArrayList<>();
            List<Integer> questionIds = new ArrayList<>();
            
            for (QuizQuestion question : quiz.getQuestions()) {
                String userAnswer = request.getParameter("question-" + question.getQuestionID());
                if (userAnswer != null) {
                    selectedAnswers.add(Integer.parseInt(userAnswer));
                    questionIds.add(question.getQuestionID());
                    
                    if (userAnswer.equals(question.getCorrectAnswer())) {
                        score++;
                    }
                }
            }
            
            // Lưu kết quả
            User user = (User) request.getSession().getAttribute("user");
            if (selectedAnswers.size() > 0) {
                quizDAO.submitQuiz(user.getUserID(), quizId, selectedAnswers, questionIds);
            }
            quizDAO.saveQuizResult(user.getUserID(), quizId, score);
            
            // Hiển thị kết quả
            request.setAttribute("score", score);
            request.setAttribute("totalQuestions", totalQuestions);
            request.setAttribute("quiz", quiz);
            request.getRequestDispatcher("/views/quiz/quiz-result.jsp").forward(request, response);
        } else {
            response.sendRedirect("quiz");
        }
    }
    
    private void viewQuiz(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        Quiz quiz = quizDAO.getQuizByCourseId(courseId);
        
        if (quiz != null) {
            request.setAttribute("quiz", quiz);
            request.getRequestDispatcher("/views/quiz/view.jsp").forward(request, response);
        } else {
            // Nếu chưa có quiz, chuyển đến trang tạo quiz
            response.sendRedirect("quiz?action=create&courseId=" + courseId);
        }
    }
}
