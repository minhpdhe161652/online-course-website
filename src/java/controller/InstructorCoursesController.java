package controller;

import dao.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import model.Course;
import model.User;

@WebServlet(name = "InstructorCoursesController", urlPatterns = {"/instructor-courses"})
public class InstructorCoursesController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Kiểm tra quyền truy cập
        if (user == null || user.getRoleID() != 2) {
            response.sendRedirect("login");
            return;
        }
        
        CourseDAO courseDAO = new CourseDAO();
        String action = request.getParameter("action");
        
        try {
            if ("delete".equals(action)) {
                // Xử lý xóa khóa học
                int courseId = Integer.parseInt(request.getParameter("courseId"));
                Course course = courseDAO.getCourseById(courseId);
                
                // Kiểm tra quyền sở hữu khóa học
                if (course != null && course.getInstructorID() == user.getUserID()) {
                    courseDAO.deleteCourse(courseId);
                    request.setAttribute("message", "Xóa khóa học thành công!");
                    request.setAttribute("messageType", "success");
                } else {
                    request.setAttribute("message", "Bạn không có quyền xóa khóa học này!");
                    request.setAttribute("messageType", "error");
                }
            }
            
            // Lấy danh sách khóa học của giảng viên
            List<Course> courses = courseDAO.getCoursesByInstructor(user.getUserID());
            request.setAttribute("courses", courses);
            
            request.getRequestDispatcher("/views/instructor-courses.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("message", "ID khóa học không hợp lệ!");
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("/views/instructor-courses.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error");
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
