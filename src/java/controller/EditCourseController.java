package controller;

import dao.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import model.Course;
import model.User;

@WebServlet(name = "EditCourseController", urlPatterns = {"/edit-course"})
public class EditCourseController extends HttpServlet {

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
        
        try {
            int courseId = Integer.parseInt(request.getParameter("id"));
            Course course = courseDAO.getCourseById(courseId);
            
            // Kiểm tra quyền sở hữu khóa học
            if (course == null || course.getInstructorID() != user.getUserID()) {
                response.sendRedirect("instructor-courses");
                return;
            }
            
            if ("POST".equals(request.getMethod())) {
                // Cập nhật thông tin khóa học
                course.setCourseName(request.getParameter("courseName"));
                course.setDescription(request.getParameter("description"));
                course.setLevelName(request.getParameter("levelName"));
                course.setPrice(Double.parseDouble(request.getParameter("price")));
                course.setImageURL(request.getParameter("imageURL"));
                
                courseDAO.updateCourse(course);
                
                request.setAttribute("message", "Cập nhật khóa học thành công!");
                request.setAttribute("messageType", "success");
                
                // Lấy lại thông tin khóa học sau khi cập nhật
                course = courseDAO.getCourseById(courseId);
            }
            
            request.setAttribute("course", course);
            request.getRequestDispatcher("/views/edit-course.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("instructor-courses");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
