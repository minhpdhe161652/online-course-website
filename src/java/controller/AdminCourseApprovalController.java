package controller;

import dao.CourseApprovalDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;

@WebServlet(name = "AdminCourseApprovalController", urlPatterns = {"/admin/course-approval"})
public class AdminCourseApprovalController extends HttpServlet {
    private CourseApprovalDAO courseApprovalDAO;
    
    @Override
    public void init() {
        courseApprovalDAO = new CourseApprovalDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Course> pendingCourses = courseApprovalDAO.getPendingCourses();
            request.setAttribute("pendingCourses", pendingCourses);
            request.getRequestDispatcher("/views/admin/course-approval.jsp").forward(request, response);
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        
        try {
            boolean success = false;
            String message = "";
            
            if ("approve".equals(action)) {
                success = courseApprovalDAO.approveCourse(courseId);
                message = success ? "Khóa học đã được phê duyệt thành công" : "Không thể phê duyệt khóa học";
            } else if ("reject".equals(action)) {
                String reason = request.getParameter("reason");
                success = courseApprovalDAO.rejectCourse(courseId, reason);
                message = success ? "Khóa học đã bị từ chối" : "Không thể từ chối khóa học";
            }
            
            request.setAttribute("message", message);
            request.setAttribute("success", success);
            doGet(request, response);
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
