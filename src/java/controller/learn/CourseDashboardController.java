package controller.learn;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.CourseDAO;
import dao.CourseSectionDAO;
import dao.LectureDAO;
import dao.QuizzesDAO;
import dao.LectureProgressDAO;

import model.Course;
import model.CourseSection;
import model.Lecture;
import model.LectureProgress;
import model.Quizzes;
import model.User;

@WebServlet("/learn/course-dashboard")
public class CourseDashboardController extends HttpServlet {
    private CourseDAO courseDAO;
    private CourseSectionDAO courseSectionDAO;
    private LectureDAO lectureDAO;

    @Override
    public void init() {
        courseDAO = new CourseDAO();
        courseSectionDAO = new CourseSectionDAO();
        lectureDAO = new LectureDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRoleID() != 1) {
            response.sendRedirect("login");
            return;
        }

        int courseID = Integer.parseInt(request.getParameter("courseID"));

        Course course = courseDAO.getCourseById(courseID);
        List<CourseSection> sections = courseSectionDAO.getSectionsByCourseId(courseID);
        List<Lecture> lectures = lectureDAO.getAllLecturesByCourseID(courseID);

        request.setAttribute("course", course);
        request.setAttribute("sections", sections);
        request.setAttribute("lectures", lectures);

        request.getRequestDispatcher("course-dashboard.jsp").forward(request, response);
    }
}
