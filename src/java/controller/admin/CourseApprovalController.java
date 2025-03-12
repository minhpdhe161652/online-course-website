package controller.admin;

import dao.CourseDAO;
import dao.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.Notification;
import model.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "CourseApprovalController", urlPatterns = {"/admin/course/*"})
public class CourseApprovalController extends HttpServlet {
    private final CourseDAO courseDAO = new CourseDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null) {
            // Hiển thị trang danh sách khóa học chờ duyệt
            List<Course> pendingCourses = courseDAO.getPendingCourses();
            request.setAttribute("pendingCourses", pendingCourses);
            request.getRequestDispatcher("/views/admin/course-approval.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/course");
            return;
        }

        int courseId;
        try {
            courseId = Integer.parseInt(request.getParameter("courseId"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID khóa học không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/admin/course");
            return;
        }

        Course course = courseDAO.getCourseById(courseId);
        if (course == null) {
            request.setAttribute("error", "Không tìm thấy khóa học");
            response.sendRedirect(request.getContextPath() + "/admin/course");
            return;
        }

        User admin = (User) request.getSession().getAttribute("user");
        if (admin == null || admin.getRoleID() != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        switch (action) {
            case "/approve":
                handleApprove(course, admin, request, response);
                break;
            case "/reject":
                handleReject(course, admin, request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/course");
                break;
        }
    }

    private void handleApprove(Course course, User admin, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        course.setIsPublished(true);
        course.setIsCancelled(false);
        course.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        
        if (courseDAO.updateCourse(course)) {
            // Gửi thông báo cho instructor
            Notification notification = new Notification();
            notification.setUserId(course.getInstructorID());
            notification.setMessage("Khóa học của bạn đã được duyệt: " + course.getTitle());
            notification.setType("course_approved");
            notification.setTarget("course");
            notification.setTargetId(course.getCourseID());
            notification.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            notification.setIsRead(false);
            notificationDAO.addNotification(notification);
            
            request.getSession().setAttribute("success", "Khóa học đã được duyệt thành công");
        } else {
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi duyệt khóa học");
        }
        response.sendRedirect(request.getContextPath() + "/admin/course");
    }

    private void handleReject(Course course, User admin, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String reason = request.getParameter("reason");
        if (reason == null || reason.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Vui lòng nhập lý do từ chối");
            response.sendRedirect(request.getContextPath() + "/admin/course");
            return;
        }
        
        course.setIsPublished(false);
        course.setIsCancelled(true);
        course.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        
        if (courseDAO.updateCourse(course)) {
            // Gửi thông báo cho instructor
            Notification notification = new Notification();
            notification.setUserId(course.getInstructorID());
            notification.setMessage("Khóa học của bạn đã bị từ chối: " + course.getTitle() + "\nLý do: " + reason);
            notification.setType("course_rejected");
            notification.setTarget("course");
            notification.setTargetId(course.getCourseID());
            notification.setTimeStamp(new Timestamp(System.currentTimeMillis()));
            notification.setIsRead(false);
            notificationDAO.addNotification(notification);
            
            request.getSession().setAttribute("success", "Đã từ chối khóa học");
        } else {
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi từ chối khóa học");
        }
        response.sendRedirect(request.getContextPath() + "/admin/course");
    }
}
