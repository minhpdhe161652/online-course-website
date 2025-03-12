package controller;

import dao.ChapterDAO;
import dao.CourseDAO;
import dao.NotificationDAO;
import dao.QuizDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.Chapter;
import model.Course;
import model.Lesson;
import model.Notification;
import model.QuizQuestion;
import model.Quiz;
import model.User;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Admin
 */
@WebServlet(name = "AddCourseController", urlPatterns = {"/add-course"})
public class AddCourseController extends HttpServlet {
    private final CourseDAO courseDAO = new CourseDAO();
    private final ChapterDAO chapterDAO = new ChapterDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get current user
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        String step = request.getParameter("step");
        if (step == null) {
            step = "1";
        }
        
        try {
            switch (step) {
                case "1":
                    // Tạo một courseID tạm thời nếu chưa có và lưu vào session
                    HttpSession session = request.getSession();
                    Course currentCourse = (Course) session.getAttribute("currentCourse");
                    
                    if (currentCourse == null) {
                        currentCourse = new Course();
                        // Tạo một courseID tạm thời âm để tránh xung đột với ID thật trong DB
                        // Sử dụng (int) để chuyển đổi an toàn từ long sang int
                        currentCourse.setCourseID((int) -(System.currentTimeMillis() % Integer.MAX_VALUE));
                        session.setAttribute("currentCourse", currentCourse);
                    }
                    
                    List<Category> categories = courseDAO.getAllCategories();
                    request.setAttribute("categories", categories);
                    request.getRequestDispatcher("/views/add-course-basic.jsp").forward(request, response);
                    break;
                    
                case "2":
                    // Kiểm tra xem đã hoàn thành bước 1 chưa
                    currentCourse = (Course) request.getSession().getAttribute("currentCourse");
                    if (currentCourse == null) {
                        response.sendRedirect("add-course");
                        return;
                    }
                    
                    request.setAttribute("course", currentCourse);
                    
                    // Lấy danh sách chương hiện có
                    List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
                    request.setAttribute("chapters", chapters);
                    
                    // Lấy quiz nếu đã có
                    try {
                        Quiz quiz = quizDAO.getQuizByCourseId(currentCourse.getCourseID());
                        if (quiz != null) {
                            // Lấy lại danh sách câu hỏi mới nhất
                            List<QuizQuestion> questions = quizDAO.getQuestionsByQuizId(quiz.getQuizID());
                            quiz.setQuestions(questions);
                            request.setAttribute("quiz", quiz);
                            
                            // Debug
                            System.out.println("Lấy được " + (questions != null ? questions.size() : 0) + " câu hỏi cho quiz ID " + quiz.getQuizID());
                        }
                    } catch (SQLException e) {
                        System.err.println("Lỗi khi lấy quiz: " + e.getMessage());
                        // Không throw lỗi, chỉ log và tiếp tục
                    }
                    
                    // Kiểm tra action để hiển thị lesson nếu cần
                    String action = request.getParameter("action");
                    if ("viewLesson".equals(action)) {
                        handleViewLesson(request, response);
                    } else if ("add-question".equals(action)) {
                        // Lấy thông tin quiz từ quizId trong request
                        String quizIdParam = request.getParameter("quizId");
                        if (quizIdParam != null && !quizIdParam.trim().isEmpty()) {
                            try {
                                int quizId = Integer.parseInt(quizIdParam);
                                Quiz quiz = quizDAO.getQuizById(quizId);
                                
                                if (quiz != null && quiz.getCourseID() == currentCourse.getCourseID()) {
                                    request.setAttribute("quiz", quiz);
                                    request.setAttribute("action", "add-question");
                                    request.getRequestDispatcher("/views/add-question.jsp").forward(request, response);
                                    return;
                                }
                            } catch (Exception e) {
                                System.err.println("Lỗi khi lấy quiz: " + e.getMessage());
                                // Không throw lỗi, chỉ log và tiếp tục
                            }
                        }
                        // Nếu không thành công, chuyển về trang chi tiết khóa học
                        request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
                    } else {
                        request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
                    }
                    break;
                    
                default:
                    response.sendRedirect("add-course?step=1");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error in doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            if (step.equals("2")) {
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/views/add-course-basic.jsp").forward(request, response);
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Kiểm tra quyền truy cập
        if (user == null || user.getRoleID() != 2) { // 2: Expert/Instructor
            response.sendRedirect("login");
            return;
        }

        String step = request.getParameter("step");
        if (step == null) {
            step = "1";
        }

        try {
            switch (step) {
                case "1":
                    handleBasicInfo(request, response, user);
                    break;
                case "2":
                    String action = request.getParameter("action");
                    if (action == null) {
                        action = "view";
                    }
                    
                    switch (action) {
                        case "add-chapter":
                            handleAddChapter(request, response, user);
                            break;
                        case "add-lesson":
                            handleAddLesson(request, response, user);
                            break;
                        case "editChapter":
                            handleEditChapter(request, response);
                            break;
                        case "deleteChapter":
                            handleDeleteChapter(request, response, user);
                            break;
                        case "viewLesson":
                            handleViewLesson(request, response);
                            break;
                        case "editLesson":
                            handleEditLesson(request, response);
                            break;
                        case "deleteLesson":
                            handleDeleteLesson(request, response);
                            break;
                        case "closeLesson":
                            // Chỉ cần xóa thuộc tính viewingLesson và hiển thị lại trang
                            request.removeAttribute("viewingLesson");
                            Course currentCourse = (Course) request.getSession().getAttribute("currentCourse");
                            List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
                            request.setAttribute("chapters", chapters);
                            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
                            break;
                        case "add-quiz":
                            handleQuizCreation(request, response);
                            break;
                        case "moveChapter":
                            handleMoveChapter(request, response);
                            break;
                        case "finish":
                            handleFinishCourse(request, response, user);
                            break;
                        case "add-question":
                            handleAddQuestion(request, response);
                            break;
                        case "cancel-course":
                            handleCancelCourse(request, response, user);
                            break;
                        default:
                            response.sendRedirect("add-course?step=2");
                            break;
                    }
                    break;
                default:
                    response.sendRedirect("add-course");
                    break;
            }
        } catch (SQLException e) {
            System.err.println("Error in doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            if (step.equals("2")) {
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/views/add-course-basic.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            if (step.equals("2")) {
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/views/add-course-basic.jsp").forward(request, response);
            }
        }
    }
    
    private void handleBasicInfo(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException, SQLException {
        try {
            // Lấy khóa học tạm thời từ session (nếu có)
            HttpSession session = request.getSession();
            Course currentCourse = (Course) session.getAttribute("currentCourse");
            
            // Nếu chưa có khóa học tạm thời, tạo mới và đặt ID tạm thời
            if (currentCourse == null) {
                currentCourse = new Course();
                currentCourse.setCourseID((int) -(System.currentTimeMillis() % Integer.MAX_VALUE));
            }
            
            // Validate required fields
            String courseName = request.getParameter("courseName");
            String description = request.getParameter("description");
            String levelName = request.getParameter("levelName");
            String subcategoryIDStr = request.getParameter("subcategoryID");
            String priceStr = request.getParameter("price");
            String imageURL = request.getParameter("imageURL");
            
            List<String> errors = new ArrayList<>();
            
            // Validate courseName
            if (courseName == null || courseName.trim().isEmpty()) {
                errors.add("Tên khóa học không được để trống");
            }
            
            // Validate description
            if (description == null || description.trim().isEmpty()) {
                errors.add("Mô tả khóa học không được để trống");
            }
            
            // Validate levelName
            if (levelName == null || levelName.trim().isEmpty()) {
                errors.add("Cấp độ khóa học không được để trống");
            }
            
            // Validate subcategoryID
            int subcategoryID;
            try {
                subcategoryID = Integer.parseInt(subcategoryIDStr);
            } catch (NumberFormatException e) {
                errors.add("Danh mục không hợp lệ");
                subcategoryID = 0;
            }
            
            // Validate price
            BigDecimal price = null;
            try {
                if (priceStr != null && !priceStr.trim().isEmpty()) {
                    price = new BigDecimal(priceStr.trim());
                    if (price.compareTo(BigDecimal.ZERO) < 0) {
                        errors.add("Giá khóa học không được âm");
                    }
                } else {
                    price = BigDecimal.ZERO;
                }
            } catch (NumberFormatException e) {
                errors.add("Giá khóa học không hợp lệ");
                price = BigDecimal.ZERO;
            }
            
            // Validate imageURL
            if (imageURL == null || imageURL.trim().isEmpty()) {
                errors.add("URL hình ảnh không được để trống");
            }
            
            // If there are validation errors
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("message", "Vui lòng kiểm tra lại thông tin!");
                request.setAttribute("messageType", "error");
                
                // Cập nhật thông tin khóa học tạm thời
                currentCourse.setCourseName(courseName);
                currentCourse.setDescription(description);
                currentCourse.setLevelName(levelName);
                currentCourse.setSubcategoryID(subcategoryID);
                currentCourse.setPrice(price);
                currentCourse.setImageURL(imageURL);
                
                // Lưu lại khóa học tạm thời vào session
                session.setAttribute("currentCourse", currentCourse);
                request.setAttribute("course", currentCourse);
                
                List<Category> categories = courseDAO.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/views/add-course-basic.jsp").forward(request, response);
                return;
            }
            
            // Cập nhật thông tin khóa học tạm thời
            currentCourse.setCourseName(courseName.trim());
            currentCourse.setDescription(description.trim());
            currentCourse.setLevelName(levelName);
            currentCourse.setSubcategoryID(subcategoryID);
            currentCourse.setPrice(price);
            currentCourse.setImageURL(imageURL.trim());
            
            // Lưu vào database
            int courseID = courseDAO.saveBasicInfo(currentCourse, user.getUserID());
            
            if (courseID > 0) {
                // Cập nhật ID thật từ database
                currentCourse.setCourseID(courseID);
                currentCourse.setBasicInfoCompleted(true);
                
                // Lưu khóa học vào session cho bước 2
                session.setAttribute("currentCourse", currentCourse);
                
                // Tạo thông báo cho admin
                Notification notification = new Notification();
                notification.setUserId(user.getUserID());
                notification.setMessage("Khóa học mới \"" + courseName + "\" đã được tạo và đang chờ duyệt");
                notification.setType("course_created");
                notification.setTarget("course");
                notification.setTargetId(courseID);
                notification.setTimeStamp(new Timestamp(System.currentTimeMillis()));
                notificationDAO.addNotification(notification);
                
                response.sendRedirect("add-course?step=2");
            } else {
                request.setAttribute("message", "Có lỗi xảy ra khi lưu thông tin khóa học!");
                request.setAttribute("messageType", "error");
                request.setAttribute("course", currentCourse);
                List<Category> categories = courseDAO.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/views/add-course-basic.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in handleBasicInfo: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            List<Category> categories = courseDAO.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/views/add-course-basic.jsp").forward(request, response);
        }
    }
    
    private void handleAddChapter(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            // Debug information
            System.out.println("=== Debug handleAddChapter ===");
            System.out.println("User: " + (user != null ? user.getUsername() : "null"));
            
            Course currentCourse = (Course) request.getSession().getAttribute("currentCourse");
            System.out.println("Current Course: " + (currentCourse != null ? currentCourse.getCourseName() : "null"));
            
            // Validate current course
            if (currentCourse == null) {
                request.setAttribute("message", "Phiên làm việc đã hết hạn. Vui lòng thực hiện lại!");
                request.setAttribute("messageType", "error");
                request.getRequestDispatcher("/views/add-course-basic.jsp").forward(request, response);
                return;
            }
            
            // Get and validate input parameters
            String chapterName = request.getParameter("chapterName");
            String description = request.getParameter("description");
            
            System.out.println("Chapter Name: " + chapterName);
            System.out.println("Description: " + description);
            
            if (chapterName == null || chapterName.trim().isEmpty()) {
                request.setAttribute("message", "Tên chương không được để trống!");
                request.setAttribute("messageType", "error");
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
                return;
            }
            
            // Create and set chapter data
            Chapter chapter = new Chapter();
            chapter.setCourseID(currentCourse.getCourseID());
            chapter.setChapterName(chapterName.trim());
            chapter.setDescription(description != null ? description.trim() : "");
            chapter.setOrderIndex(chapterDAO.getChapterCount(currentCourse.getCourseID()) + 1);
            
            // Add chapter to database
            int chapterID = chapterDAO.addChapter(chapter);
            System.out.println("Added Chapter ID: " + chapterID);
            
            if (chapterID > 0) {
                request.setAttribute("message", "Thêm chương thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Có lỗi xảy ra khi thêm chương!");
                request.setAttribute("messageType", "error");
            }
            
            // Get updated chapter list
            List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
            request.setAttribute("chapters", chapters);
            
        } catch (Exception e) {
            System.err.println("Error in handleAddChapter: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }
        
        request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
    }
    
    private void handleAddLesson(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            // Validate input
            String lessonName = request.getParameter("lessonName");
            String description = request.getParameter("description");
            String videoURL = request.getParameter("videoURL");
            String content = request.getParameter("content");
            String chapterIDStr = request.getParameter("chapterID");
            
            List<String> errors = new ArrayList<>();
            
            // Validate lessonName
            if (lessonName == null || lessonName.trim().isEmpty()) {
                errors.add("Tên bài giảng không được để trống");
            }
            
            // Validate content
            if (content == null || content.trim().isEmpty()) {
                errors.add("Nội dung bài giảng không được để trống");
            }
            
            // Validate chapterID
            int chapterID;
            try {
                chapterID = Integer.parseInt(chapterIDStr);
            } catch (NumberFormatException e) {
                errors.add("Chương không hợp lệ");
                chapterID = 0;
            }
            
            // Get current course from session
            Course currentCourse = (Course) request.getSession().getAttribute("currentCourse");
            if (currentCourse == null) {
                throw new ServletException("Không tìm thấy thông tin khóa học");
            }
            
            // If there are validation errors
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("message", "Vui lòng kiểm tra lại thông tin!");
                request.setAttribute("messageType", "error");
                
                // Preserve form data
                Lesson lesson = new Lesson();
                lesson.setLessonName(lessonName);
                lesson.setDescription(description);
                lesson.setVideoURL(videoURL);
                lesson.setContent(content);
                lesson.setChapterID(chapterID);
                request.setAttribute("lesson", lesson);
                
                // Get chapters for dropdown
                List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
                request.setAttribute("chapters", chapters);
                
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
                return;
            }
            
            // Create Lesson object
            Lesson lesson = new Lesson();
            lesson.setLessonName(lessonName.trim());
            lesson.setDescription(description != null ? description.trim() : "");
            lesson.setVideoURL(videoURL != null && !videoURL.trim().isEmpty() ? videoURL.trim() : null);
            lesson.setContent(content.trim());
            lesson.setChapterID(chapterID);
            
            // Save to database
            boolean success = chapterDAO.addLesson(lesson);
            
            // Get updated chapters list
            List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
            request.setAttribute("chapters", chapters);
            
            if (success) {
                // Create notification
                Notification notification = new Notification();
                notification.setUserId(user.getUserID());
                notification.setMessage("Bài giảng mới \"" + lessonName + "\" đã được thêm vào khóa học");
                notification.setType("lesson_added");
                notification.setTarget("lesson");
                notification.setTimeStamp(new Timestamp(System.currentTimeMillis()));
                notificationDAO.addNotification(notification);
                
                request.setAttribute("message", "Thêm bài giảng thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Có lỗi xảy ra khi thêm bài giảng!");
                request.setAttribute("messageType", "error");
                request.setAttribute("lesson", lesson);
            }
            
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in handleAddLesson: " + e.getMessage());
            e.printStackTrace();
            
            Course currentCourse = (Course) request.getSession().getAttribute("currentCourse");
            List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
            request.setAttribute("chapters", chapters);
            
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }
    
    private void handleQuizCreation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Course currentCourse = (Course) session.getAttribute("currentCourse");
            
            if (currentCourse == null) {
                request.setAttribute("message", "Không tìm thấy thông tin khóa học");
                request.setAttribute("messageType", "error");
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
                return;
            }
            
            // Kiểm tra xác thực courseID tạm thời
            int courseID = currentCourse.getCourseID();
            if (courseID == 0) {
                // Nếu chưa có courseID, tạo một courseID tạm thời
                courseID = (int) -(System.currentTimeMillis() % Integer.MAX_VALUE);
                currentCourse.setCourseID(courseID);
                session.setAttribute("currentCourse", currentCourse);
            }
            
            // Lấy thông tin từ form
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String timeLimitStr = request.getParameter("timeLimit");
            String passingScoreStr = request.getParameter("passingScore");
            
            // Validate thông tin
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập tiêu đề bài kiểm tra");
            }
            
            if (title.length() < 5 || title.length() > 100) {
                throw new IllegalArgumentException("Tiêu đề bài kiểm tra phải từ 5-100 ký tự");
            }
            
            // Validate và chuyển đổi thởi gian làm bài
            int timeLimit;
            try {
                timeLimit = Integer.parseInt(timeLimitStr);
                if (timeLimit < 5 || timeLimit > 180) {
                    throw new IllegalArgumentException("Thời gian làm bài phải từ 5-180 phút");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Thời gian làm bài không hợp lệ");
            }
            
            // Validate và chuyển đổi điểm đạt
            int passingScore = 70; // Giá trị mặc định
            if (passingScoreStr != null && !passingScoreStr.trim().isEmpty()) {
                try {
                    passingScore = Integer.parseInt(passingScoreStr);
                    if (passingScore < 0 || passingScore > 100) {
                        throw new IllegalArgumentException("Điểm đạt phải từ 0-100%");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Điểm đạt không hợp lệ");
                }
            }
            
            // Tạo quiz mới
            Quiz quiz = new Quiz();
            quiz.setTitle(title.trim());
            quiz.setQuizDescription(description != null ? description.trim() : "");
            quiz.setTimeLimit(timeLimit);
            quiz.setPassingScore(passingScore);
            quiz.setDuration(String.valueOf(timeLimit));
            quiz.setCourseID(courseID); // Sử dụng courseID tạm thời
            quiz.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
            
            // Lưu quiz vào database
            int quizID;
            try {
                quizID = quizDAO.createQuiz(quiz);
                if (quizID <= 0) {
                    throw new Exception("Có lỗi xảy ra khi tạo bài kiểm tra");
                }
                quiz.setQuizID(quizID);
            } catch (Exception e) {
                System.err.println("Error creating quiz: " + e.getMessage());
                throw new Exception("Lỗi khi lưu bài kiểm tra: " + e.getMessage());
            }
            
            System.out.println("Created quiz with ID: " + quizID);
            
            // Cập nhật danh sách chương
            List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
            request.setAttribute("chapters", chapters);
            
            // Lưu quiz vào session để sử dụng khi thêm câu hỏi
            session.setAttribute("currentQuiz", quiz);
            
            request.setAttribute("message", "Tạo bài kiểm tra thành công! Bây giờ bạn có thể thêm câu hỏi.");
            request.setAttribute("messageType", "success");
            
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error in handleQuizCreation: " + e.getMessage());
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "error");
        } catch (Exception e) {
            System.err.println("Error in handleQuizCreation: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
        } finally {
            // Cập nhật danh sách chương
            HttpSession session = request.getSession();
            Course currentCourse = (Course) session.getAttribute("currentCourse");
            List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
            request.setAttribute("chapters", chapters);
            request.setAttribute("course", currentCourse);
            
            // Lấy quiz mới
            try {
                Quiz quiz = quizDAO.getQuizByCourseId(currentCourse.getCourseID());
                if (quiz != null) {
                    // Lấy lại danh sách câu hỏi mới nhất
                    List<QuizQuestion> questions = quizDAO.getQuestionsByQuizId(quiz.getQuizID());
                    quiz.setQuestions(questions);
                    request.setAttribute("quiz", quiz);
                    
                    // Debug
                    System.out.println("Lấy được " + (questions != null ? questions.size() : 0) + " câu hỏi cho quiz ID " + quiz.getQuizID());
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi lấy quiz: " + e.getMessage());
                // Không throw lỗi, chỉ log và tiếp tục
            }
            
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }
    
    private void handleFinishCourse(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Course currentCourse = (Course) session.getAttribute("currentCourse");
            
            // Kiểm tra xem có khóa học đang được tạo không
            if (currentCourse == null) {
                request.setAttribute("message", "Không tìm thấy thông tin khóa học đang tạo");
                request.setAttribute("messageType", "error");
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
                return;
            }

            // Debug thông tin khóa học hiện tại
            System.out.println("DEBUG - Thông tin khóa học hiện tại:");
            System.out.println("CourseID: " + currentCourse.getCourseID());
            System.out.println("CourseName: " + currentCourse.getCourseName());
            
            // Cập nhật trạng thái khóa học
            currentCourse.setBasicInfoCompleted(true);
            currentCourse.setIsPublished(false); // Chưa publish cho đến khi admin duyệt
            currentCourse.setIsCancelled(false);
            currentCourse.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

            // Đảm bảo các trường bắt buộc không bị null
            if (currentCourse.getPrice() == null) {
                currentCourse.setPrice(BigDecimal.ZERO); // Mặc định giá là 0 VNĐ
            }
            
            if (currentCourse.getInstructorID() <= 0) {
                currentCourse.setInstructorID(user.getUserID()); // Người tạo là instructor
            }
            
            // Debug thông tin khóa học trước khi cập nhật
            System.out.println("DEBUG - Thông tin khóa học trước khi cập nhật:");
            System.out.println("CourseID: " + currentCourse.getCourseID());
            System.out.println("CourseName: " + currentCourse.getCourseName());
            System.out.println("BasicInfoCompleted: " + currentCourse.isBasicInfoCompleted());
            System.out.println("IsPublished: " + currentCourse.getIsPublished());
            System.out.println("IsCancelled: " + currentCourse.getIsCancelled());
            System.out.println("Price: " + currentCourse.getPrice());
            System.out.println("InstructorID: " + currentCourse.getInstructorID());

            // Lưu khóa học vào database (vẫn giữ ID tạm thời)
            System.out.println("Đang cập nhật khóa học với ID tạm thời: " + currentCourse.getCourseID());
            
            // Lưu thông tin khóa học vào bảng Courses hoặc cập nhật nếu đã tồn tại
            boolean success = false;
            if (currentCourse.getCourseID() < 0) {
                // Khóa học chưa được lưu vào database, lưu mới
                try {
                    // Đảm bảo các trường bắt buộc có giá trị hợp lệ
                    if (currentCourse.getCourseName() == null || currentCourse.getCourseName().trim().isEmpty()) {
                        throw new IllegalArgumentException("Tên khóa học không được để trống");
                    }
                    
                    int courseID = courseDAO.saveBasicInfo(currentCourse, user.getUserID());
                    if (courseID > 0) {
                        // Cập nhật ID thật từ database
                        System.out.println("Lưu thành công, ID mới: " + courseID);
                        currentCourse.setCourseID(courseID);
                        success = true;
                    } else {
                        System.out.println("Không thể lưu khóa học, courseID <= 0");
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi lưu thông tin khóa học: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                // Khóa học đã có trong database, cập nhật
                try {
                    // Kiểm tra lại các trường bắt buộc trước khi cập nhật
                    if (currentCourse.getCourseName() == null || currentCourse.getCourseName().trim().isEmpty()) {
                        throw new IllegalArgumentException("Tên khóa học không được để trống");
                    }
                    
                    success = courseDAO.updateCourse(currentCourse);
                    System.out.println("Kết quả cập nhật khóa học: " + (success ? "Thành công" : "Thất bại"));
                } catch (Exception e) {
                    System.err.println("Lỗi khi cập nhật khóa học: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            if (success) {
                System.out.println("Hoàn thành khóa học thành công, ID: " + currentCourse.getCourseID());
                session.removeAttribute("currentCourse");
                // Lưu thông báo vào session thay vì request khi sử dụng sendRedirect
                session.setAttribute("message", "Khóa học đã được hoàn thành và đang chờ duyệt");
                session.setAttribute("messageType", "success");
                response.sendRedirect(request.getContextPath() + "/instructor-courses");
            } else {
                System.out.println("Không thể hoàn thành khóa học");
                request.setAttribute("message", "Có lỗi xảy ra khi hoàn thành khóa học. Vui lòng thử lại sau.");
                request.setAttribute("messageType", "error");
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Lỗi trong handleFinishCourse: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra khi hoàn thành khóa học: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }
    
    private void handleViewLesson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String lessonIdStr = request.getParameter("lessonId");
            if (lessonIdStr == null || lessonIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("ID bài giảng không hợp lệ");
            }

            int lessonId = Integer.parseInt(lessonIdStr);
            Lesson lesson = chapterDAO.getLessonById(lessonId);
            
            if (lesson == null) {
                throw new IllegalArgumentException("Không tìm thấy bài giảng");
            }

            // Lấy danh sách chương để hiển thị menu
            Course currentCourse = (Course) request.getSession().getAttribute("currentCourse");
            List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
            request.setAttribute("chapters", chapters);
            
            // Đặt bài giảng đang xem
            request.setAttribute("viewingLesson", lesson);
            
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in handleViewLesson: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }

    private void handleEditChapter(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int chapterId = Integer.parseInt(request.getParameter("chapterId"));
            String chapterName = request.getParameter("chapterName");
            String description = request.getParameter("description");
            
            Chapter chapter = new Chapter();
            chapter.setChapterID(chapterId);
            chapter.setChapterName(chapterName);
            chapter.setDescription(description);
            
            boolean success = chapterDAO.updateChapter(chapter);
            
            if (success) {
                request.setAttribute("message", "Cập nhật chương thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Có lỗi xảy ra khi cập nhật chương!");
                request.setAttribute("messageType", "error");
            }
            
            // Redirect back to course details page
            Course currentCourse = (Course) request.getSession().getAttribute("currentCourse");
            if (currentCourse != null) {
                request.setAttribute("course", currentCourse);
                List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
                request.setAttribute("chapters", chapters);
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            } else {
                response.sendRedirect("courses");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }

    private void handleEditLesson(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int lessonId = Integer.parseInt(request.getParameter("lessonId"));
            String lessonName = request.getParameter("lessonName");
            String description = request.getParameter("description");
            String videoURL = request.getParameter("videoURL");
            String content = request.getParameter("content");
            
            Lesson lesson = new Lesson();
            lesson.setLessonID(lessonId);
            lesson.setLessonName(lessonName);
            lesson.setDescription(description);
            lesson.setVideoURL(videoURL);
            lesson.setContent(content);
            
            boolean success = chapterDAO.updateLesson(lesson);
            
            if (success) {
                request.setAttribute("message", "Cập nhật bài giảng thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Có lỗi xảy ra khi cập nhật bài giảng!");
                request.setAttribute("messageType", "error");
            }
            
            // Redirect back to course details page
            Course currentCourse = (Course) request.getSession().getAttribute("currentCourse");
            if (currentCourse != null) {
                request.setAttribute("course", currentCourse);
                List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
                request.setAttribute("chapters", chapters);
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            } else {
                response.sendRedirect("courses");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }

    private void handleDeleteLesson(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int lessonId = Integer.parseInt(request.getParameter("lessonId"));
            
            boolean success = chapterDAO.deleteLesson(lessonId);
            
            if (success) {
                request.setAttribute("message", "Xóa bài giảng thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Có lỗi xảy ra khi xóa bài giảng!");
                request.setAttribute("messageType", "error");
            }
            
            // Redirect back to course details page
            Course currentCourse = (Course) request.getSession().getAttribute("currentCourse");
            if (currentCourse != null) {
                request.setAttribute("course", currentCourse);
                List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
                request.setAttribute("chapters", chapters);
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            } else {
                response.sendRedirect("courses");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }

    private void handleDeleteChapter(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            int chapterId = Integer.parseInt(request.getParameter("chapterId"));
            
            boolean success = chapterDAO.deleteChapter(chapterId);
            
            if (success) {
                request.setAttribute("message", "Xóa chương thành công!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Có lỗi xảy ra khi xóa chương!");
                request.setAttribute("messageType", "error");
            }
            
            // Redirect back to course details page
            Course currentCourse = (Course) request.getSession().getAttribute("currentCourse");
            if (currentCourse != null) {
                request.setAttribute("course", currentCourse);
                List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
                request.setAttribute("chapters", chapters);
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
            } else {
                response.sendRedirect("courses");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }
    
    private void handleMoveChapter(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Course currentCourse = (Course) session.getAttribute("currentCourse");
            
            if (currentCourse == null) {
                request.setAttribute("message", "Không tìm thấy thông tin khóa học");
                request.setAttribute("messageType", "error");
                request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
                return;
            }
            
            int chapterId = Integer.parseInt(request.getParameter("chapterId"));
            String direction = request.getParameter("direction");
            
            // Lấy danh sách chương hiện tại
            List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
            
            // Tìm vị trí của chương cần di chuyển
            int currentIndex = -1;
            for (int i = 0; i < chapters.size(); i++) {
                if (chapters.get(i).getChapterID() == chapterId) {
                    currentIndex = i;
                    break;
                }
            }
            
            if (currentIndex != -1) {
                // Di chuyển lên
                if ("up".equals(direction) && currentIndex > 0) {
                    Chapter current = chapters.get(currentIndex);
                    Chapter previous = chapters.get(currentIndex - 1);
                    
                    // Hoán đổi thứ tự
                    int currentOrder = current.getOrderIndex();
                    current.setOrderIndex(previous.getOrderIndex());
                    previous.setOrderIndex(currentOrder);
                    
                    // Cập nhật trong database
                    chapterDAO.updateChapterOrder(current.getChapterID(), current.getOrderIndex());
                    chapterDAO.updateChapterOrder(previous.getChapterID(), previous.getOrderIndex());
                }
                // Di chuyển xuống
                else if ("down".equals(direction) && currentIndex < chapters.size() - 1) {
                    Chapter current = chapters.get(currentIndex);
                    Chapter next = chapters.get(currentIndex + 1);
                    
                    // Hoán đổi thứ tự
                    int currentOrder = current.getOrderIndex();
                    current.setOrderIndex(next.getOrderIndex());
                    next.setOrderIndex(currentOrder);
                    
                    // Cập nhật trong database
                    chapterDAO.updateChapterOrder(current.getChapterID(), current.getOrderIndex());
                    chapterDAO.updateChapterOrder(next.getChapterID(), next.getOrderIndex());
                }
            }
            
            response.sendRedirect("add-course?step=2");
            
        } catch (Exception e) {
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }
    
    private void handleAddQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Course currentCourse = (Course) session.getAttribute("currentCourse");
            if (currentCourse == null) {
                throw new IllegalArgumentException("Không tìm thấy thông tin khóa học");
            }
            
            // Lấy thông tin quiz từ quizId trong request
            Quiz quiz = null;
            try {
                String quizIdParam = request.getParameter("quizId");
                if (quizIdParam == null || quizIdParam.trim().isEmpty()) {
                    throw new IllegalArgumentException("Thiếu thông tin bài kiểm tra");
                }
                
                int quizId = Integer.parseInt(quizIdParam);
                quiz = quizDAO.getQuizById(quizId);
                
                if (quiz == null) {
                    throw new IllegalArgumentException("Không tìm thấy bài kiểm tra. Vui lòng tạo bài kiểm tra trước.");
                }
                
                // Kiểm tra xem quiz có thuộc khóa học hiện tại không
                if (quiz.getCourseID() != currentCourse.getCourseID()) {
                    throw new IllegalArgumentException("Bài kiểm tra không thuộc khóa học hiện tại");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Thiếu thông tin bài kiểm tra");
            } catch (SQLException e) {
                throw new IllegalArgumentException("Không tìm thấy bài kiểm tra. Vui lòng tạo bài kiểm tra trước.");
            }
            
            // Lấy thông tin câu hỏi từ form
            String questionText = request.getParameter("questionText");
            String questionType = request.getParameter("questionType");
            
            // Validate thông tin câu hỏi
            if (questionText == null || questionText.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập nội dung câu hỏi");
            }
            
            if (questionText.length() < 10 || questionText.length() > 1000) {
                throw new IllegalArgumentException("Nội dung câu hỏi phải từ 10-1000 ký tự");
            }
            
            if (questionType == null || questionType.trim().isEmpty()) {
                questionType = "Multiple Choice"; // Lỗi mặc định
            }
            
            // Tạo đối tượng QuizQuestion
            QuizQuestion question = new QuizQuestion();
            question.setQuizID(quiz.getQuizID());
            question.setQuestionText(questionText.trim());
            question.setQuestionType(questionType);
            
            // Xuất hiện các lựa chọn và đáp án đúng
            String option1 = request.getParameter("option1");
            String option2 = request.getParameter("option2");
            String option3 = request.getParameter("option3");
            String option4 = request.getParameter("option4");
            String correctAnswer = request.getParameter("correctAnswer");
            
            // Validate các lựa chọn
            if (option1 == null || option1.trim().isEmpty() ||
                option2 == null || option2.trim().isEmpty() ||
                option3 == null || option3.trim().isEmpty() ||
                option4 == null || option4.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng điền đầy đủ các lựa chọn");
            }
            
            // Validate đáp án đúng
            if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn đáp án đúng");
            }
            
            // Gán các lựa chọn và đáp án đúng
            question.setOption1(option1.trim());
            question.setOption2(option2.trim());
            question.setOption3(option3.trim());
            question.setOption4(option4.trim());
            question.setCorrectAnswer(correctAnswer.trim());
            
            // Lưu câu hỏi vào database
            boolean success = quizDAO.createQuestion(question);
            
            if (!success) {
                throw new Exception("Có lỗi xảy ra khi lưu câu hỏi");
            }
            
            // Lấy danh sách câu hỏi từ database
            List<QuizQuestion> quizQuestions = quizDAO.getQuestionsByQuizId(quiz.getQuizID());
            request.setAttribute("quizQuestions", quizQuestions);
            
            // Lấy quiz mới
            try {
                quiz = quizDAO.getQuizByCourseId(currentCourse.getCourseID());
                if (quiz != null) {
                    // Lấy lại danh sách câu hỏi mới nhất
                    List<QuizQuestion> questions = quizDAO.getQuestionsByQuizId(quiz.getQuizID());
                    quiz.setQuestions(questions);
                    request.setAttribute("quiz", quiz);
                    
                    // Debug
                    System.out.println("Lấy được " + (questions != null ? questions.size() : 0) + " câu hỏi cho quiz ID " + quiz.getQuizID());
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi lấy quiz: " + e.getMessage());
                // Không throw lỗi, chỉ log và tiếp tục
            }
            
            // Lấy danh sách chương để hiển thị menu
            List<Chapter> chapters = chapterDAO.getChaptersByCourse(currentCourse.getCourseID());
            request.setAttribute("chapters", chapters);
            request.setAttribute("course", currentCourse);
            
            // Đặt action để giữ tab quiz được active
            request.setAttribute("action", "add-question");
            

            session.setAttribute("message", "Thêm câu hỏi thành công!");
            session.setAttribute("messageType", "success");
            
            response.sendRedirect("add-course?step=2");
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-question.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-question.jsp").forward(request, response);
        }
    }
    
    private void handleCancelCourse(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Course currentCourse = (Course) session.getAttribute("currentCourse");
            
            if (currentCourse == null) {
                response.sendRedirect(request.getContextPath() + "/instructor-courses");
                return;
            }
            
            int courseID = currentCourse.getCourseID();
            System.out.println("Hu1ee7y bu1ecf khu00f3a hu1ecdc vu1edbi ID: " + courseID);
            
            // Nu1ebfu khu00f3a hu1ecdc u0111u00e3 u0111u01b0u1ee3c lu01b0u vu00e0o database (ID > 0), 
            // cu1eadp nhu1eadt tru1ea1ng thu00e1i thu00e0nh u0111u00e3 hu1ee7y
            if (courseID > 0) {
                // Xu00f3a cu00e1c chu01b0u01a1ng, bu00e0i giu1ea3ng vu00e0 quiz liu00ean quan
                boolean chaptersDeleted = chapterDAO.deleteChaptersByCourse(courseID);
                System.out.println("Xu00f3a cu00e1c chu01b0u01a1ng: " + (chaptersDeleted ? "Thu00e0nh cu00f4ng" : "Thu1ea5t bu1ea1i"));
                
                boolean quizzesDeleted = quizDAO.deleteQuizzesByCourse(courseID);
                System.out.println("Xu00f3a cu00e1c quiz: " + (quizzesDeleted ? "Thu00e0nh cu00f4ng" : "Thu1ea5t bu1ea1i"));
                
                // u0110u00e1nh du1ea5u khu00f3a hu1ecdc u0111u00e3 bu1ecb hu1ee7y
                currentCourse.setIsCancelled(true);
                boolean courseUpdated = courseDAO.updateCourse(currentCourse);
                System.out.println("Cu1eadp nhu1eadt tru1ea1ng thu00e1i khu00f3a hu1ecdc: " + (courseUpdated ? "Thu00e0nh cu00f4ng" : "Thu1ea5t bu1ea1i"));
            }
            
            // Xu00f3a khu00f3a hu1ecdc khu1ecfi session
            session.removeAttribute("currentCourse");
            
            // Chuyu1ec3n hu01b0u1edbng vu1ec1 trang quu1ea3n lu00fd khu00f3a hu1ecdc
            session.setAttribute("message", "Hu1ee7y bu1ecf khu00f3a hu1ecdc thu00e0nh cu00f4ng");
            session.setAttribute("messageType", "success");
            response.sendRedirect(request.getContextPath() + "/instructor-courses");
            
        } catch (Exception e) {
            System.err.println("Lu1ed7i khi hu1ee7y bu1ecf khu00f3a hu1ecdc: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("message", "Cu00f3 lu1ed7i xu1ea3y ra khi hu1ee7y bu1ecf khu00f3a hu1ecdc: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/add-course-details.jsp").forward(request, response);
        }
    }
}
