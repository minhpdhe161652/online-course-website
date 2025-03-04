package controller.learn;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.LectureDAO;
import model.Course;
import model.Lecture;
import model.User;

@WebServlet("/learn/view-lecture")
public class LectureController extends HttpServlet {
    private LectureDAO lectureDAO;

    @Override
    public void init() {
        lectureDAO = new LectureDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Course course = (Course) session.getAttribute("course");

        if (user == null || user.getRoleID() != 1) {
            response.sendRedirect("login");
            return;
        }
        int sectionID = Integer.parseInt(request.getParameter("sectionID"));
        int lectureID = Integer.parseInt(request.getParameter("lectureId"));
        int courseID = Integer.parseInt(request.getParameter("courseID"));

        Lecture lecture = lectureDAO.getLectureBySectionID(sectionID);
        
        request.setAttribute("lecture", lecture);
        request.setAttribute("lectureContent", lecture.getLectureDescription());
        request.setAttribute("courseID", courseID);
        request.getRequestDispatcher("view-lecture.jsp").forward(request, response);
    }
}
