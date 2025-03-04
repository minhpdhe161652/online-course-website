
package controller;

import dao.EnrollmentDAO;
import dto.EnrolledCourseDTO;
import model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "EnrolledCourseController", urlPatterns = {"/enrolled-course"})
public class EnrolledCourseController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            if (user == null || user.getRoleID() != 1) {
                response.sendRedirect("login");
                return;
            }
            
            EnrollmentDAO dao = new EnrollmentDAO();
            List<EnrolledCourseDTO> enrollments = dao.getEnrolledCoursesByUserId(user.getUserID());
            System.out.println("Enrollments: " + enrollments);

            request.setAttribute("enrollments", enrollments);
            request.getRequestDispatcher("enrolled-course.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error occurred: " + e.getMessage());
            request.getRequestDispatcher("enrolled-course.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
