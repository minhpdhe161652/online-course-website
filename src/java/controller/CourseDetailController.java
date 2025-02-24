package controller;

import dao.CourseDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.User;
import java.util.List;
import model.CourseFeedback;

@WebServlet(name = "CourseDetailController", urlPatterns = {"/course-detail"})
public class CourseDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get course ID from request parameter
            int courseId = Integer.parseInt(request.getParameter("id"));
            
            // Get course details
            CourseDAO courseDAO = new CourseDAO();
            Course course = courseDAO.getCourseById(courseId);
            
            if (course != null) {
                // Get instructor details
                UserDAO userDAO = new UserDAO();
                User instructor = userDAO.getUserById(course.getInstructorId());
                
                // Get course feedbacks using CourseDAO instead of CourseFeedbackDAO
                List<CourseFeedback> feedbacks = courseDAO.getFeedbacksByCourseId(courseId);
                
                // Set attributes for JSP
                request.setAttribute("course", course);
                request.setAttribute("instructor", instructor);
                request.setAttribute("feedbacks", feedbacks);
                
                // Forward to JSP page
                request.getRequestDispatcher("CourseDetail.jsp").forward(request, response);
            } else {
                response.sendRedirect("home"); // Redirect to home if course not found
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("home"); // Redirect to home if invalid course ID
        }
    }
}
